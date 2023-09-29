package com.ead.course.clients;

import com.ead.course.UserDto;
import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public CourseClient(RestTemplate restTemplate, UtilsService utilsService) {
        this.restTemplate = restTemplate;
        this.utilsService = utilsService;
    }


    public Page<UserDto> getAllUserByCourse(UUID courseId, Pageable pageable) {
        List<UserDto> users = null;
        String url = utilsService.generateUrl(courseId, pageable);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        try {
            ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType =
                    new ParameterizedTypeReference<ResponsePageDto<UserDto>>() {
                    };

            ResponseEntity<ResponsePageDto<UserDto>> result =
                    restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            users = Objects.requireNonNull(result.getBody()).getContent();

            log.debug("Response Number of Elements: {} ", users.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /users %s ", e);
        }
        log.info("Ending request /users {} ", courseId);
        assert users != null;
        return new PageImpl<>(users);
    }

}
