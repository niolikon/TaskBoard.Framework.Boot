package com.niolikon.taskboard.framework.fakeapp.documents.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "document_audits")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DocumentAuditDoc {

    @Id
    @EqualsAndHashCode.Exclude
    private String id;

    @DBRef
    @Field("document")
    private DocumentDoc document;

    @Field("type")
    private String type; // e.g. "CREATE", "UPDATE"

    @Field("timestamp")
    private Instant timestamp;
}
