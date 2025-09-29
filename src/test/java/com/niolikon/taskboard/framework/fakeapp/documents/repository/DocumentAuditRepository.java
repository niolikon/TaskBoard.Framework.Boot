package com.niolikon.taskboard.framework.fakeapp.documents.repository;

import com.niolikon.taskboard.framework.fakeapp.documents.model.DocumentAuditDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentAuditRepository extends MongoRepository<DocumentAuditDoc, String> {
}
