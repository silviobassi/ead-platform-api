package com.ead.course.clients;

import com.ead.course.UserDto;
import com.ead.course.dtos.CourseUserDto;
import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
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
public class AuthUserClient {


    final RestTemplate restTemplate;

    final UtilsService utilsService;

    @Value("${ead.api.url.auth-user}")
    String REQUEST_URL_AUTHUSER;

    public AuthUserClient(RestTemplate restTemplate, UtilsService utilsService) {
        this.restTemplate = restTemplate;
        this.utilsService = utilsService;
    }


    public Page<UserDto> getAllUserByCourse(UUID courseId, Pageable pageable) {
        List<UserDto> users = null;
        String url = REQUEST_URL_AUTHUSER + utilsService.generateUrl(courseId, pageable);
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
            log.error("Error request /courses %s ", e);
        }
        log.info("Ending request /courses {} ", courseId);
        assert users != null;
        return new PageImpl<>(users);
    }

    public ResponseEntity<UserDto> getOneUserById(UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/users/" + userId;
        return restTemplate.exchange(url, HttpMethod.GET, null, UserDto.class);
    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/users/" + userId + "/courses/subscription";
        CourseUserDto courseUserDto = new CourseUserDto(courseId, userId);
        restTemplate.postForObject(url, courseUserDto, String.class);
    }

    public void deleteCourseInAuthUser(UUID courseId) {
        String url = REQUEST_URL_AUTHUSER + "/users/courses/" + courseId;
        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}
