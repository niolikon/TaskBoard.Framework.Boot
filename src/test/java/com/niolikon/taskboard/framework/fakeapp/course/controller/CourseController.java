package com.niolikon.taskboard.framework.fakeapp.course.controller;

import com.niolikon.taskboard.framework.fakeapp.course.dto.CourseView;
import com.niolikon.taskboard.framework.fakeapp.course.service.ICourseService;

import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/Courses")
public class CourseController {

    private final ICourseService courseService;

    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseView>> readAll(@AuthenticationPrincipal Jwt jwt) {
        String ownerUid = jwt.getSubject();
        List<CourseView> userCourses = courseService.readAll(ownerUid);
        return ok().body(userCourses);
    }
}
