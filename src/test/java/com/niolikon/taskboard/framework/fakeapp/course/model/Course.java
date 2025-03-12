package com.niolikon.taskboard.framework.fakeapp.course.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    private Long id;

    @EqualsAndHashCode.Include
    @Setter
    private String title;

    @ManyToMany(mappedBy = "courses")
    @Setter
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Student> students = new HashSet<>();

    @ManyToOne
    @Setter
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Professor professor = null;
}

