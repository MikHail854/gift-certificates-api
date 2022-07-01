package ru.clevertec.ecl.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.clevertec.ecl.config.Cluster;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.SequenceIdDTO;
import ru.clevertec.ecl.wrapper.RequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.clevertec.ecl.constants.Constants.URL_CREATE_ORDER_INTERCEPTOR;
import static ru.clevertec.ecl.constants.Constants.URL_SEQUENCE_ORDERS;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderInterceptor implements HandlerInterceptor {

    private final Cluster cluster;
    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;

    private final ApplicationContext applicationContext;

//    @PostConstruct
//    public void main() {
//        for (int i = 0; i < applicationContext.getBeanDefinitionNames().length; i++) {
//            System.out.println("!!! " + applicationContext.getBeanDefinitionNames()[i]);
//        }
//    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (HttpMethod.GET.toString().equals(request.getMethod())) {
            final boolean requiredServer = checkServer(request, getIdFromPath(request));
            if (!requiredServer) {
                final int id = getIdFromPath(request);
                final String newURL = getNewURL(request, id);
                response.sendRedirect(newURL);
                return false;
            }
        } else if (HttpMethod.POST.toString().equals(request.getMethod())) {
            final String saveToCommitLog = request.getParameter("save_to_commit_log");
            if (Objects.isNull(saveToCommitLog)) {
                final Integer sequenceId = getSequenceId();
                final boolean requiredServer = checkServer(request, sequenceId);
                if (!requiredServer) {
                    ContentCachingRequestWrapper currentRequest = new ContentCachingRequestWrapper(request);
                    final String inputJson = new RequestWrapper(currentRequest).getBodyString();
                    final Object inputObject = mapper.readValue(inputJson, Object.class);
                    final String newServerPort = getNewPort(sequenceId);
                    final OrderDTO orderDTO = restTemplate.postForObject(String.format(URL_CREATE_ORDER_INTERCEPTOR, newServerPort), inputObject, OrderDTO.class);
                    String json = mapper.writeValueAsString(orderDTO);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    response.getWriter().write(json);
                    return false;
                }
            }
        }

        return true;
        // Если он возвращает false, postHandle и соответствующий метод интерфейса не могут быть выполнены, а метод afterCompletion выполняется напрямую, и запрос прерывается.
    }


    private boolean checkServer(HttpServletRequest request, Integer id) {
        final int localPort = request.getLocalPort();

        return Objects.nonNull(cluster.getNodes().get(id % cluster.getNodes().size()).getReplicas().stream()
                .filter(replica -> Integer.parseInt(replica.getPort()) == localPort)
                .findFirst().orElse(null));
    }

    private String getNewPort(int id) {
        return cluster.getNodes().get(id % cluster.getNodes().size()).getReplicas().stream()
                .filter(Cluster.Replica::getIsAlive)
                .findFirst()
                .map(Cluster.Replica::getPort)
                .orElseThrow(() -> new RuntimeException("no active ports found"));
    }

    @SneakyThrows
    private String getNewURL(HttpServletRequest request, int id) {
        final StringBuffer requestURL = request.getRequestURL();
        final String newServerPort = getNewPort(id);

        String newURL = requestURL.toString().replace(String.valueOf(request.getLocalPort()), newServerPort);
        if (request.getQueryString() != null) {
            newURL = newURL + "?" + request.getQueryString();
        }
        return newURL;
    }


    private Integer getIdFromPath(HttpServletRequest request) {

        StringBuffer requestURL = request.getRequestURL();

        if (request.getQueryString() != null) {
            requestURL.append(request.getQueryString());
        }

        Pattern pattern = Pattern.compile("(\\d+)[^\\d]*$");
        Matcher matcher = pattern.matcher(requestURL.toString());
        //если цифр в строке нет, то будет null.
        String id = matcher.find() ? matcher.group(1) : null;
        assert id != null;
        return Integer.parseInt(id);
    }

    private Integer getSequenceId() {
        final Integer maxSequenceId = cluster.getNodes().values().stream()
                .map(node -> CompletableFuture.supplyAsync(() -> node.getReplicas().stream()
                        .filter(Cluster.Replica::getIsAlive)
                        .map(replica -> CompletableFuture.supplyAsync(
                                () -> restTemplate.getForObject(buildUrlSequence(replica.getPort()), Integer.class)))
                        .map(CompletableFuture::join)
                        .max(Integer::compareTo)
                        .orElseThrow(() -> new RuntimeException("max sequence ID not found"))
                ))
                .map(CompletableFuture::join)
                .max(Integer::compareTo)
                .orElseThrow(() -> new RuntimeException("max sequence ID not found"));

        setSequence(maxSequenceId);

        return maxSequenceId;
    }

    private void setSequence(int maxSequenceId) {
        cluster.getNodes().values()
                .forEach(node -> node.getReplicas()
                        .forEach(replica -> restTemplate.put(buildUrlSequence(replica.getPort()), new SequenceIdDTO(maxSequenceId - 1))));
    }

    private String buildUrlSequence(String port) {
        return String.format(URL_SEQUENCE_ORDERS, port);
    }

}
