package com.niolikon.taskboard.framework.test;

import com.niolikon.taskboard.framework.test.containers.PostgreSQLTestContainersConfig;
import com.niolikon.taskboard.framework.test.extensions.IsolatedDataJpaTestScenarioExtension;
import com.niolikon.taskboard.framework.test.scenarios.MultipleCourseTestScenario;
import com.niolikon.taskboard.framework.test.scenarios.SingleCourseTestScenario;
import com.niolikon.taskboard.framework.test.annotations.WithIsolatedDataJpaTestScenario;
import com.niolikon.taskboard.framework.fakeapp.config.FakeAppTestDataJpaConfig;
import com.niolikon.taskboard.framework.fakeapp.course.model.Course;
import com.niolikon.taskboard.framework.fakeapp.course.model.Professor;
import com.niolikon.taskboard.framework.fakeapp.course.model.Student;
import com.niolikon.taskboard.framework.fakeapp.course.repository.CourseRepository;
import com.niolikon.taskboard.framework.fakeapp.course.repository.ProfessorRepository;
import com.niolikon.taskboard.framework.fakeapp.course.repository.StudentRepository;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {FakeAppTestDataJpaConfig.class, PostgreSQLTestContainersConfig.class})
@ExtendWith(IsolatedDataJpaTestScenarioExtension.class)
class IsolatedDataJpaTestScenarioIntegrationIT {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Test
    @WithIsolatedDataJpaTestScenario(dataClass = SingleCourseTestScenario.class)
    void givenSingleCourseTestScenario_whenTesting_thenProperEntitiesAreAvailable() {
        List<Course> courses = courseRepository.findAll();
        List<Professor> professors = professorRepository.findAll();
        List<Student> students = studentRepository.findAll();

        AssertionsForInterfaceTypes.assertThat(courses).hasSize(1);
        Course course = courses.get(0);
        assertThat(course.getTitle()).isEqualTo( SingleCourseTestScenario.COURSE_TITLE );
        AssertionsForInterfaceTypes.assertThat(course.getStudents()).hasSize(2);

        AssertionsForInterfaceTypes.assertThat(professors).hasSize(1);
        Professor professor = professors.get(0);
        assertThat(professor.getName()).isEqualTo( SingleCourseTestScenario.PROFESSOR_NAME );
        assertThat(professor.getSurname()).isEqualTo( SingleCourseTestScenario.PROFESSOR_SURNAME );
        AssertionsForInterfaceTypes.assertThat(professor.getCourses()).hasSize(1);

        AssertionsForInterfaceTypes.assertThat(students).hasSize(2);
        Student student1 = students.get(0);
        Student student2 = students.get(1);
        assertThat(student1.getName()).isEqualTo( SingleCourseTestScenario.STUDENT1_NAME );
        assertThat(student1.getSurname()).isEqualTo( SingleCourseTestScenario.STUDENT1_SURNAME );
        AssertionsForInterfaceTypes.assertThat(student1.getCourses()).hasSize(1);
        assertThat(student2.getName()).isEqualTo( SingleCourseTestScenario.STUDENT2_NAME );
        assertThat(student2.getSurname()).isEqualTo( SingleCourseTestScenario.STUDENT2_SURNAME );
        AssertionsForInterfaceTypes.assertThat(student2.getCourses()).hasSize(1);
    }

    @Test
    @WithIsolatedDataJpaTestScenario(dataClass = MultipleCourseTestScenario.class)
    void givenMultipleCourseTestScenario_whenTesting_thenProperEntitiesAreAvailable() {
        List<Course> courses = courseRepository.findAll();
        List<Professor> professors = professorRepository.findAll();
        List<Student> students = studentRepository.findAll();

        AssertionsForInterfaceTypes.assertThat(courses).hasSize(2);
        Course course1 = courses.get(0);
        Course course2 = courses.get(1);
        assertThat(course1.getTitle()).isEqualTo( MultipleCourseTestScenario.COURSE1_TITLE );
        assertThat(course2.getTitle()).isEqualTo( MultipleCourseTestScenario.COURSE2_TITLE );
        AssertionsForInterfaceTypes.assertThat(course1.getStudents()).hasSize(2);
        AssertionsForInterfaceTypes.assertThat(course2.getStudents()).hasSize(2);

        AssertionsForInterfaceTypes.assertThat(professors).hasSize(1);
        Professor professor = professors.get(0);
        assertThat(professor.getName()).isEqualTo( MultipleCourseTestScenario.PROFESSOR_NAME );
        assertThat(professor.getSurname()).isEqualTo( MultipleCourseTestScenario.PROFESSOR_SURNAME );
        AssertionsForInterfaceTypes.assertThat(professor.getCourses()).hasSize(2);

        AssertionsForInterfaceTypes.assertThat(students).hasSize(3);
        Student student1 = students.get(0);
        Student student2 = students.get(1);
        Student student3 = students.get(2);
        assertThat(student1.getName()).isEqualTo( MultipleCourseTestScenario.STUDENT1_NAME );
        assertThat(student1.getSurname()).isEqualTo( MultipleCourseTestScenario.STUDENT1_SURNAME );
        AssertionsForInterfaceTypes.assertThat(student1.getCourses()).hasSize(1);
        assertThat(student2.getName()).isEqualTo( MultipleCourseTestScenario.STUDENT2_NAME );
        assertThat(student2.getSurname()).isEqualTo( MultipleCourseTestScenario.STUDENT2_SURNAME );
        AssertionsForInterfaceTypes.assertThat(student2.getCourses()).hasSize(2);
        assertThat(student3.getName()).isEqualTo( MultipleCourseTestScenario.STUDENT3_NAME );
        assertThat(student3.getSurname()).isEqualTo( MultipleCourseTestScenario.STUDENT3_SURNAME );
        AssertionsForInterfaceTypes.assertThat(student3.getCourses()).hasSize(1);
    }
}
