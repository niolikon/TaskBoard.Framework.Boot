package com.niolikon.taskboard.framework.fakeapp.course.service;

import com.niolikon.taskboard.framework.fakeapp.course.dto.CourseView;

import java.util.List;

public interface ICourseService {
    List<CourseView> readAll(String ownerUid);
}
