package com.niolikon.taskboard.framework.test.scenarios;

import com.niolikon.taskboard.framework.fakeapp.course.model.Course;
import com.niolikon.taskboard.framework.fakeapp.course.model.Professor;
import com.niolikon.taskboard.framework.fakeapp.course.model.Student;

import java.util.List;

public class SingleCourseTestScenario {
    public static final String COURSE_TITLE = "Test Course Title";
    public static final String PROFESSOR_NAME = "Test Professor Name";
    public static final String PROFESSOR_SURNAME = "Test Professor Surname";
    public static final String STUDENT1_NAME = "Test Student 1 Name";
    public static final String STUDENT1_SURNAME = "Test Student 1 Surname";
    public static final String STUDENT2_NAME = "Test Student 2 Name";
    public static final String STUDENT2_SURNAME = "Test Student 2 Surname";

    public static List<Object> getDataset() {
        Course course = Course.builder()
                .title(COURSE_TITLE)
                .build();

        Professor professor = Professor.builder()
                .name(PROFESSOR_NAME)
                .surname(PROFESSOR_SURNAME)
                .build();

        Student student1 = Student.builder()
                .name(STUDENT1_NAME)
                .surname(STUDENT1_SURNAME)
                .build();

        Student student2 = Student.builder()
                .name(STUDENT2_NAME)
                .surname(STUDENT2_SURNAME)
                .build();

        professor.getCourses().add(course);
        student1.getCourses().add(course);
        student2.getCourses().add(course);

        course.setProfessor(professor);
        course.getStudents().add(student1);
        course.getStudents().add(student2);

        return List.of(
                course,
                professor,
                student1,
                student2
        );
    }
}
