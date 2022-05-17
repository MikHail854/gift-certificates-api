package ru.clevertec.ecl.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.clevertec.ecl.config.ServerProperties;
import ru.clevertec.ecl.dto.OrderDTO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static ru.clevertec.ecl.constants.Constants.URL_ORDERS_GET_SEQUENCE;


@Slf4j
@Component
public class RequestGetInterceptor implements HandlerInterceptor {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ApplicationContext applicationContext;

//    @PostConstruct
//    public void main() {
//        for (int i = 0; i < applicationContext.getBeanDefinitionNames().length; i++) {
//            System.out.println("!!! " + applicationContext.getBeanDefinitionNames()[i]);
//        }
//    }

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private HttpServlet httpServlet;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (request.getMethod().equals("GET")) {
            final boolean requiredServer = checkServerForGetMethod(request, getIdFromPath(request));
            if (!requiredServer) {
                final int id = getIdFromPath(request);
                final StringBuilder newURL = getNewURL(request, id);
                response.sendRedirect(String.valueOf(newURL));

                /*final Object jsonObject = changeServer(request);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(jsonObject);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);*/
                return false;
            }
        }
        if ("POST".equalsIgnoreCase(request.getMethod())) {
//            final Integer newId = jdbcTemplate.queryForObject("SELECT nextval('order_data_id_seq');", Integer.class);
            final Integer sequenceId = getSequenceId();
            final boolean requiredServer = checkServerForGetMethod(request, sequenceId);
            if (!requiredServer) {
//                final String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//                log.info("!!! body: {}", body);
//                final String newServerPort = getNewPort(newId);

                final StringBuilder newURL = getNewURL(request, sequenceId);
                log.info("!!! newURL: {}", newURL);
//                restTemplate.postForObject(String.valueOf(newURL), request, OrderDTO.class);
                log.info("!!! restTemplate: {}", restTemplate);
                final OrderDTO orderDTO = restTemplate.postForObject(String.valueOf(newURL), request, OrderDTO.class);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(orderDTO);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);


//                response.sendRedirect(String.valueOf(newURL));
                return false;
//                ServletContext servletContext =  getServletContext();
//                RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(String.valueOf(newURL));
//                requestDispatcher.forward(request, response);

            }

        }

        return true;
        // Если он возвращает false, postHandle и соответствующий метод интерфейса не могут быть выполнены, а метод afterCompletion выполняется напрямую, и запрос прерывается.
    }


    private boolean checkServerForGetMethod(HttpServletRequest request, Integer id) {
//        int id = getIdFromPath(request);

        final int localPort = request.getLocalPort();
        return ((id % 3 == 1 && localPort == 9001)
                || (id % 3 == 2 && localPort == 9002)
                || (id % 3 == 0 && localPort == 9003));
    }

    private String getNewPort(int id) {
        return serverProperties.getPorts().get(id % serverProperties.getPorts().size());
    }

    private StringBuilder getNewURL(HttpServletRequest request, int id) {
        final StringBuffer requestURL = request.getRequestURL();
        final String newServerPort = getNewPort(id);

        StringBuilder newURL = new StringBuilder();
        for (int i = 0; i < requestURL.length(); i++) {
            if (i == 16) { //todo заменение порта не завязываясь на индекс
                newURL.append(requestURL.charAt(i));
                newURL.append(newServerPort);
                i = i + 4;
                continue;
            }
            newURL.append(requestURL.charAt(i));
        }

        if (request.getQueryString() != null) {
            newURL.append("?");
            newURL.append(request.getQueryString());
        }
        return newURL;
    }

//    private Object changeServer(HttpServletRequest request) {
//        final StringBuilder newURL = getNewURL(request);
//        return restTemplate.getForObject(String.valueOf(newURL), Object.class);
//    }

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
        final Integer max = serverProperties.getPorts().values().stream()
                .map(port -> CompletableFuture.supplyAsync(() -> restTemplate.getForObject(buildUrlSequence(port), Integer.class)))
                .map(CompletableFuture::join)
                .max(Integer::compareTo).orElseThrow(() -> new NotFoundException("max sequence Id not found"));
        log.info("!!! max: {}", max);

        //todo
        final Stream<Void> voidStream = serverProperties.getPorts().values().stream()
                .map(port -> CompletableFuture.supplyAsync(() -> restTemplate.patchForObject(buildUrlSequence(port), max - 1, Void.class)))
                .map(CompletableFuture::join);

        return max;
    }

    private String buildUrlSequence(String port) {
        return String.format(URL_ORDERS_GET_SEQUENCE, port);
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
