package ru.clevertec.ecl.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.clevertec.ecl.config.ServerProperties;
import ru.clevertec.ecl.wrapper.RequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class IntegrationDataInterceptor implements HandlerInterceptor {


    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;
    private final ServerProperties serverProperties;

    private final String REQUEST_FROM_CLIENT = "request_from_client";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        ContentCachingRequestWrapper currentRequest = new ContentCachingRequestWrapper(request);
        if (HttpMethod.PUT.toString().equals(currentRequest.getMethod())) {
            if (!Objects.nonNull(currentRequest.getParameter(REQUEST_FROM_CLIENT))
                    || currentRequest.getParameter(REQUEST_FROM_CLIENT).equals("true")) {
                final String inputJson = new RequestWrapper(currentRequest).getBodyString();
                final Object inputObject = mapper.readValue(inputJson, Object.class);
                serverProperties.getPorts().values().stream()
                        .filter(port -> currentRequest.getLocalPort() != Integer.parseInt(port))
                        .forEach(port -> restTemplate.put(getUrl(currentRequest, port), inputObject));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (HttpMethod.POST.toString().equals(request.getMethod())) {
            final String inputJson = new RequestWrapper(request).getBodyString();
            final Object inputObject = mapper.readValue(inputJson, Object.class);
            if (!Objects.nonNull(request.getParameter("request_from_client"))
                    || request.getParameter("request_from_client").equals("true")) {
                serverProperties.getPorts().values().stream()
                        .filter(port -> request.getLocalPort() != Integer.parseInt(port))
                        .forEach(port -> restTemplate.postForObject(getUrl(request, port), inputObject, Object.class));
            }
        }
    }

    private String getUrl(HttpServletRequest request, String port) {
        final String newUrl = request.getRequestURL().toString().replace(String.valueOf(request.getLocalPort()), port)
                + "?request_from_client=false";
        log.info("!!! newUrl: {}", newUrl);
        return newUrl;
    }

}