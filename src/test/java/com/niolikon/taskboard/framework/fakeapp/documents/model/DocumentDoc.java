package com.niolikon.taskboard.framework.fakeapp.documents.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DocumentDoc {

    @Id
    @EqualsAndHashCode.Exclude
    private String id;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("createdAt")
    private Instant createdAt;
}
