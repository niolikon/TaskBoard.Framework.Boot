package com.niolikon.taskboard.framework.test;

import com.niolikon.taskboard.framework.fakeapp.config.FakeAppMongoTestConfig;
import com.niolikon.taskboard.framework.fakeapp.documents.model.DocumentAuditDoc;
import com.niolikon.taskboard.framework.fakeapp.documents.model.DocumentDoc;
import com.niolikon.taskboard.framework.fakeapp.documents.repository.DocumentAuditRepository;
import com.niolikon.taskboard.framework.fakeapp.documents.repository.DocumentRepository;
import com.niolikon.taskboard.framework.test.annotations.WithIsolatedMongoTestScenario;
import com.niolikon.taskboard.framework.test.containers.MongoTestContainersConfig;
import com.niolikon.taskboard.framework.test.extensions.IsolatedMongoTestScenarioExtension;
import com.niolikon.taskboard.framework.test.scenarios.MultipleDocumentMongoTestScenario;
import com.niolikon.taskboard.framework.test.scenarios.SingleDocumentMongoTestScenario;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
@ContextConfiguration(classes = { FakeAppMongoTestConfig.class, MongoTestContainersConfig.class })
@ExtendWith(IsolatedMongoTestScenarioExtension.class)
class IsolatedMongoTestScenarioIntegrationIT {

    @Autowired private DocumentRepository documentRepository;
    @Autowired private DocumentAuditRepository documentAuditRepository;

    @Test
    @WithIsolatedMongoTestScenario(dataClass = SingleDocumentMongoTestScenario.class)
    void givenSingleDocumentScenario_whenTesting_thenProperDocsAreAvailable() {
        List<DocumentDoc> docs = documentRepository.findAll();
        List<DocumentAuditDoc> audits = documentAuditRepository.findAll();

        AssertionsForInterfaceTypes.assertThat(docs).hasSize(1);
        DocumentDoc doc = docs.get(0);
        assertThat(doc.getTitle()).isEqualTo(SingleDocumentMongoTestScenario.TITLE);
        assertThat(doc.getDescription()).isEqualTo(SingleDocumentMongoTestScenario.DESCRIPTION);

        AssertionsForInterfaceTypes.assertThat(audits).hasSize(1);
        assertThat(audits.get(0).getDocument().getId()).isEqualTo(doc.getId());
    }

    @Test
    @WithIsolatedMongoTestScenario(dataClass = MultipleDocumentMongoTestScenario.class)
    void givenMultipleDocumentScenario_whenTesting_thenProperDocsAreAvailable() {
        List<DocumentDoc> docs = documentRepository.findAll();
        List<DocumentAuditDoc> audits = documentAuditRepository.findAll();

        AssertionsForInterfaceTypes.assertThat(docs).hasSize(2);
        AssertionsForInterfaceTypes.assertThat(audits).hasSize(3);

        // check titles
        AssertionsForInterfaceTypes.assertThat(docs.stream().map(DocumentDoc::getTitle).toList())
                .containsExactlyInAnyOrder(MultipleDocumentMongoTestScenario.TITLE1, MultipleDocumentMongoTestScenario.TITLE2);
    }
}
