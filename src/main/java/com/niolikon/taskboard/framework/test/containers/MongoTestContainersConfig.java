package com.niolikon.taskboard.framework.test.containers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.java.Log;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.testcontainers.containers.MongoDBContainer;

@TestConfiguration
@Log
public class MongoTestContainersConfig {

    // Versione recente e stabile di Mongo
    private static final MongoDBContainer MONGO = new MongoDBContainer("mongo:7");

    static {
        MONGO.start();
    }

    /**
     * Connection string base dal container. Aggiungo esplicitamente un DB name "testdb".
     * In alternativa puoi usare MONGO.getReplicaSetUrl("testdb") se preferisci la replica set.
     */
    @Bean
    @Primary
    public MongoClient mongoClient() {
        String uri = MONGO.getConnectionString() + "/testdb";
        log.info(() -> "Overriding MongoClient with Testcontainers: " + uri);
        return MongoClients.create(uri);
    }

    @Bean
    @Primary
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, "testdb");
    }
}
