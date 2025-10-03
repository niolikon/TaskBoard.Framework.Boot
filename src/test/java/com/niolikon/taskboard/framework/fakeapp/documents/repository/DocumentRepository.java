package com.niolikon.taskboard.framework.fakeapp.documents.repository;

import com.niolikon.taskboard.framework.fakeapp.documents.model.DocumentDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentDoc, String> {
}
