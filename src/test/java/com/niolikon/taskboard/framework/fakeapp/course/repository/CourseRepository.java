package com.niolikon.taskboard.framework.fakeapp.course.repository;

import com.niolikon.taskboard.framework.fakeapp.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
