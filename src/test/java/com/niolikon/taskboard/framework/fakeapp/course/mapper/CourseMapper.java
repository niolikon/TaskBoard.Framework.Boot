package com.niolikon.taskboard.framework.fakeapp.course.mapper;

import com.niolikon.taskboard.framework.fakeapp.course.dto.CourseView;
import com.niolikon.taskboard.framework.fakeapp.course.model.Course;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseView toCourseView(Course course);

    List<CourseView> toCourseViewList(List<Course> course);
}
