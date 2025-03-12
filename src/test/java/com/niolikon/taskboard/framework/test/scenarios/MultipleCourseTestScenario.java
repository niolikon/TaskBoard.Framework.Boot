package com.niolikon.taskboard.framework.test.scenarios;

import com.niolikon.taskboard.framework.fakeapp.course.model.Course;
import com.niolikon.taskboard.framework.fakeapp.course.model.Professor;
import com.niolikon.taskboard.framework.fakeapp.course.model.Student;

import java.util.List;

public class MultipleCourseTestScenario {
    public static final String COURSE1_TITLE = "Test Course 1 Title";
    public static final String COURSE2_TITLE = "Test Course 2 Title";
    public static final String PROFESSOR_NAME = "Test Professor Name";
    public static final String PROFESSOR_SURNAME = "Test Professor Surname";
    public static final String STUDENT1_NAME = "Test Student 1 Name";
    public static final String STUDENT1_SURNAME = "Test Student 1 Surname";
    public static final String STUDENT2_NAME = "Test Student 2 Name";
    public static final String STUDENT2_SURNAME = "Test Student 2 Surname";
    public static final String STUDENT3_NAME = "Test Student 3 Name";
    public static final String STUDENT3_SURNAME = "Test Student 3 Surname";

    public static List<Object> getDataset() {
        Professor professor = Professor.builder()
                .name(PROFESSOR_NAME)
                .surname(PROFESSOR_SURNAME)
                .build();

        Course course1 = Course.builder()
                .title(COURSE1_TITLE)
                .build();

        Course course2 = Course.builder()
                .title(COURSE2_TITLE)
                .build();

        Student student1 = Student.builder()
                .name(STUDENT1_NAME)
                .surname(STUDENT1_SURNAME)
                .build();

        Student student2 = Student.builder()
                .name(STUDENT2_NAME)
                .surname(STUDENT2_SURNAME)
                .build();

        Student student3 = Student.builder()
                .name(STUDENT3_NAME)
                .surname(STUDENT3_SURNAME)
                .build();

        professor.getCourses().addAll(List.of(course1, course2));

        student1.getCourses().add(course1);
        student2.getCourses().addAll(List.of(course1, course2));
        student3.getCourses().add(course2);

        course1.setProfessor(professor);
        course1.getStudents().addAll(List.of(student1, student2));
        course2.setProfessor(professor);
        course2.getStudents().addAll(List.of(student2,student3));

        return List.of(
                professor,
                course1,
                course2,
                student1,
                student2,
                student3
        );
    }
}
