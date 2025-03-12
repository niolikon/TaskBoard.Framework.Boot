package com.niolikon.taskboard.framework.security.keycloak.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niolikon.taskboard.framework.fakeapp.config.FakeAppTestWebConfig;
import com.niolikon.taskboard.framework.fakeapp.course.controller.CourseController;
import com.niolikon.taskboard.framework.fakeapp.course.dto.CourseView;
import com.niolikon.taskboard.framework.fakeapp.course.service.ICourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@ContextConfiguration(classes = FakeAppTestWebConfig.class)
@AutoConfigureWebMvc
class KeycloakJwtAuthenticationConverterIT {
    private static final String VALID_UUID_OF_A_USER = "uuid-hex-of-valid-user";
    private static final String VALID_AUTHORITY = "ROLE_USER";
    private static final String INVALID_AUTHORITY = "ROLE_SHAMAN";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ICourseService courseService;

    @Test
    void givenUserWithValidCredentials_whenGetCourses_thenReturnJsonArray() throws Exception {
        when(courseService.readAll(VALID_UUID_OF_A_USER))
                .thenReturn(List.of(CourseView.builder().build(), CourseView.builder().build()));

        String jsonResponse = mockMvc.perform(
                        get("/api/Courses")
                                .with(jwt()
                                        .jwt(jwt -> jwt.subject(VALID_UUID_OF_A_USER))
                                        .authorities(new SimpleGrantedAuthority(VALID_AUTHORITY)))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        List<CourseView> results = Arrays.asList(objectMapper.readValue(jsonResponse, CourseView[].class));
        assertThat(results).hasSize(2);
    }


    @Test
    void givenUserWithValidCredentialsButNoRole_whenGetCourses_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(
                        get("/api/Courses")
                                .with(jwt()
                                        .jwt(jwt -> jwt.subject(VALID_UUID_OF_A_USER))
                                        .authorities(new SimpleGrantedAuthority(INVALID_AUTHORITY)))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void givenUserWithNoValidCredentials_whenGetCourses_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(
                        get("/api/Courses")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}
