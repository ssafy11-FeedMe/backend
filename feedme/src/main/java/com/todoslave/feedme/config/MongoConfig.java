package com.todoslave.feedme.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableReactiveMongoRepositories(basePackages = "com.todoslave.feedme.repository")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

  @Value("${spring.data.mongodb.uri}")
  private String mongoUri;

  @Value("${spring.data.mongodb.database}")
  private String databaseName;

  @Override
  public MongoClient reactiveMongoClient() {
    return MongoClients.create(mongoUri);
  }

  @Override
  protected String getDatabaseName() {
    return databaseName;
  }
}