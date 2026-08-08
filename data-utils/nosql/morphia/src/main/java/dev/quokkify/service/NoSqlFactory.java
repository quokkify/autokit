package dev.quokkify.service;

import java.util.Objects;

import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

/**
 * Use this class for get methods for work with mongo database.
 */
public class NoSqlFactory {

  private final MongoClient mongoClient;
  private final String dbName;
  private final ThreadLocal<Datastore> datastoreThreadLocal = new ThreadLocal<>();

  public NoSqlFactory(MongoClient mongoClient, String dbName) {
    this.mongoClient = mongoClient;
    this.dbName = dbName;
  }

  /**
   * Return thread safe {@link dev.morphia.Datastore} for work with mongo.
   *
   * @return {@link dev.morphia.Datastore}
   */
  public Datastore getThreadLocalDatastore() {
    if (Objects.isNull(datastoreThreadLocal.get())) {
      datastoreThreadLocal.set(Morphia.createDatastore(mongoClient, dbName));
    }
    return datastoreThreadLocal.get();
  }
}
