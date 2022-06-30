package ru.clevertec.ecl.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.clevertec.ecl.config.Cluster;
import ru.clevertec.ecl.dto.CommitLogDTO;
import ru.clevertec.ecl.entty.CommitLog;
import ru.clevertec.ecl.service.CommitLogService;
import ru.clevertec.ecl.service.KafkaCommitLogListener;
import ru.clevertec.ecl.wrapper.RequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static ru.clevertec.ecl.constants.Constants.URL_COMMIT_LOG;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommitLogInterceptor implements HandlerInterceptor {

    private final Cluster cluster;
    private final RestTemplate restTemplate;

    private final KafkaCommitLogListener kafkaCommitLogListener;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        if (HttpMethod.POST.toString().equals(request.getMethod())
                || HttpMethod.PUT.toString().equals(request.getMethod())) {

//            kafkaCommitLogListener.commitLogListener(CommitLogDTO.builder()
//                    .method(request.getMethod())
//                    .body(request.get)
//                    .build());
        }

//
//        if (HttpMethod.POST.toString().equals(request.getMethod())
//                || HttpMethod.PUT.toString().equals(request.getMethod())) {
//
//            if (!Objects.nonNull(request.getParameter("request_from_client"))
//                    || request.getParameter("request_from_client").equals("true")) {
//
//                cluster.getNodes().values().stream()
//                        .flatMap(node -> node.getReplicas().stream())
////                        .filter(replica -> request.getLocalPort() != Integer.parseInt(replica.getPort()))
//                        .forEach(replica -> restTemplate.postForObject(String.format(URL_COMMIT_LOG, replica.getPort()), buildObjectCommitLog(request), Object.class));
//            }
//
//        }
    }

    private String getUrl(HttpServletRequest request, String port) {
        return request.getRequestURL().toString().replace(String.valueOf(request.getLocalPort()), port)
                + "?request_from_client=false";
    }

    @SneakyThrows
    private CommitLog buildObjectCommitLog(HttpServletRequest request) {
        return CommitLog.builder()
                .url(request.getRequestURL().toString())
                .body(new RequestWrapper(request).getBodyString())
                .build();
    }

}