package ru.clevertec.ecl.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.clevertec.ecl.config.ServerProperties;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.SequenceIdDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.clevertec.ecl.constants.Constants.URL_SEQUENCE_ORDERS;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderInterceptor implements HandlerInterceptor {

    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;
    private final ServerProperties serverProperties;

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
            final Integer sequenceId = getSequenceId();
            final boolean requiredServer = checkServer(request, sequenceId);
            if (!requiredServer) {
                final String newURL = getNewURL(request, sequenceId);
                final OrderDTO orderDTO = restTemplate.postForObject(newURL, null, OrderDTO.class);
                String json = mapper.writeValueAsString(orderDTO);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write(json);
                return false;
            }
        }

        return true;
        // Если он возвращает false, postHandle и соответствующий метод интерфейса не могут быть выполнены, а метод afterCompletion выполняется напрямую, и запрос прерывается.
    }


    private boolean checkServer(HttpServletRequest request, Integer id) {
        final int localPort = request.getLocalPort();
        return ((id % 3 == 1 && localPort == 9001)
                || (id % 3 == 2 && localPort == 9002)
                || (id % 3 == 0 && localPort == 9003));
    }

    private String getNewPort(int id) {
        return serverProperties.getPorts().get(id % serverProperties.getPorts().size());
    }

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

    private Integer getSequenceId() throws NotFoundException {
        final Integer maxSequenceId = serverProperties.getPorts().values().stream()
                .map(port -> CompletableFuture.supplyAsync(() -> restTemplate.getForObject(buildUrlSequence(port), Integer.class)))
                .map(CompletableFuture::join)
                .max(Integer::compareTo).orElseThrow(() -> new NotFoundException("max sequence Id not found"));

        serverProperties.getPorts().values()
                .forEach(port -> restTemplate.put(buildUrlSequence(port),
                        new SequenceIdDTO(maxSequenceId - 1)));

        return maxSequenceId;
    }

    private String buildUrlSequence(String port) {
        return String.format(URL_SEQUENCE_ORDERS, port);
    }



/*
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.info("Interceptor postHandle: запрошенный интерфейс запрошен (для рендеринга интерфейсного интерфейса) ...");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        int status = response.getStatus();
        long startTime = Long.valueOf(request.getAttribute("startTime").toString());
        long endTime = System.currentTimeMillis();


        log.info("Interceptor afterCompletion: завершается весь процесс запроса (включая рендеринг внешнего интерфейса), " +
                "время, необходимое для этого:" + (endTime - startTime) + " Код состояния:" + status);
    }*/

}
