package com.niolikon.taskboard.framework.test.scenarios;

import com.niolikon.taskboard.framework.fakeapp.documents.model.DocumentAuditDoc;
import com.niolikon.taskboard.framework.fakeapp.documents.model.DocumentDoc;

import java.time.Instant;
import java.util.List;

public class SingleDocumentMongoTestScenario {

    public static final String TITLE = "Test Doc Title";
    public static final String DESCRIPTION = "Test Doc Description";

    public static List<Object> getDataset() {
        DocumentDoc doc = DocumentDoc.builder()
                .title(TITLE)
                .description(DESCRIPTION)
                .createdAt(Instant.parse("2024-01-01T12:00:00Z"))
                .build();

        DocumentAuditDoc audit = DocumentAuditDoc.builder()
                .document(doc)
                .type("CREATE")
                .timestamp(Instant.parse("2024-01-01T12:00:01Z"))
                .build();

        return List.of(doc, audit);
    }
}
