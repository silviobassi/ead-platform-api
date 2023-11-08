package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Component
public class CourseClient {

    final RestTemplate restTemplate;

    final UtilsService utilsService;

    @Value("${ead.api.url.course}")
    private String REQUEST_URL_COURSE;

    public CourseClient(RestTemplate restTemplate, UtilsService utilsService) {
        this.restTemplate = restTemplate;
        this.utilsService = utilsService;
    }

    //@Retry(name = "retryInstance")
    @CircuitBreaker(name = "circuitbreakerInstance")
    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable, String token) {


        List<CourseDto> courses = null;
        String url = REQUEST_URL_COURSE + utilsService.generateUrl(userId, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> requestEntity = new HttpEntity<>("parameters", headers);

        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        System.out.println("---Start Request to Course Microservice ----");

        ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType =
                new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {
                };

        ResponseEntity<ResponsePageDto<CourseDto>> result =
                restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
        courses = Objects.requireNonNull(result.getBody()).getContent();
        log.debug("Response Number of Elements: {} ", courses.size());
        log.info("Ending request /courses {} ", userId);
        return new PageImpl<>(courses);
    }

}
