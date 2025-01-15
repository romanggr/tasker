package org.example.tasker_back.integration;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainersSetup {

    private static final MongoDBContainer MONGO_CONTAINER;

    static {
        MONGO_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:latest"));
        MONGO_CONTAINER.start();
    }

    public static MongoDBContainer getMongoContainer() {
        return MONGO_CONTAINER;
    }
}