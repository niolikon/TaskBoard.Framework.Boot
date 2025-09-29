package com.niolikon.taskboard.framework.test.scenarios;

import com.niolikon.taskboard.framework.fakeapp.documents.model.DocumentAuditDoc;
import com.niolikon.taskboard.framework.fakeapp.documents.model.DocumentDoc;

import java.time.Instant;
import java.util.List;

public class MultipleDocumentMongoTestScenario {

    public static final String TITLE1 = "Doc 1";
    public static final String TITLE2 = "Doc 2";

    public static List<Object> getDataset() {
        DocumentDoc doc1 = DocumentDoc.builder()
                .title(TITLE1)
                .description("First")
                .createdAt(Instant.parse("2024-02-01T10:00:00Z"))
                .build();

        DocumentDoc doc2 = DocumentDoc.builder()
                .title(TITLE2)
                .description("Second")
                .createdAt(Instant.parse("2024-02-02T10:00:00Z"))
                .build();

        DocumentAuditDoc a1 = DocumentAuditDoc.builder().document(doc1).type("CREATE").timestamp(Instant.parse("2024-02-01T10:00:01Z")).build();
        DocumentAuditDoc a2 = DocumentAuditDoc.builder().document(doc2).type("CREATE").timestamp(Instant.parse("2024-02-02T10:00:01Z")).build();
        DocumentAuditDoc a3 = DocumentAuditDoc.builder().document(doc2).type("UPDATE").timestamp(Instant.parse("2024-02-03T11:00:00Z")).build();

        return List.of(doc1, doc2, a1, a2, a3);
    }
}
