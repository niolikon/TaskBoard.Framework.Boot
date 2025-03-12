package com.niolikon.taskboard.framework.fakeapp.course.service;

import com.niolikon.taskboard.framework.fakeapp.course.mapper.CourseMapper;
import com.niolikon.taskboard.framework.fakeapp.course.dto.CourseView;
import com.niolikon.taskboard.framework.fakeapp.course.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService implements ICourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public List<CourseView> readAll(String ownerUid) {
        return courseMapper.toCourseViewList(courseRepository.findAll());
    }
}
