package io.automation.steps;

import java.util.List;

import io.automation.entity.nosql.MongoEntityInterface;
import io.automation.service.NoSqlFactory;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import dev.morphia.DeleteOptions;
import dev.morphia.UpdateOptions;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.UpdateOperator;
import io.qameta.allure.Step;

/**
 * Class for work with mongo database, this class presents base CRUD operations for you.
 */
public class MongoDatabaseSteps {

  private final NoSqlFactory noSqlFactory;

  public MongoDatabaseSteps(NoSqlFactory noSqlFactory) {
    this.noSqlFactory = noSqlFactory;
  }

  public NoSqlFactory getNoSqlFactory() {
    return noSqlFactory;
  }

  /**
   * Save entity to database. Use this method for save one entity.
   *
   * @param entity entity for save, extends from {@link MongoEntityInterface}
   * @param <T>    extends from {@link MongoEntityInterface}
   */
  @Step("Save entity to mongo database")
  public <T extends MongoEntityInterface> void save(T entity) {
    noSqlFactory.getThreadLocalDatastore().save(entity);
  }

  /**
   * Save entities list to database. Use this method for save more than one entity.
   *
   * @param entities list of entities for save.
   * @param <T>      extents from {@link MongoEntityInterface}
   */
  @Step("Save entities to mongo database")
  public <T extends MongoEntityInterface> void save(List<T> entities) {
    noSqlFactory.getThreadLocalDatastore().save(entities);
  }

  /**
   * Update entity in database. Use this method for change data in database.
   *
   * @param entity    entity with data for update,
   *                  extends from {@link MongoEntityInterface}
   * @param operators update operator. For example:
   *                  <pre>
   *                                   {@code mongoDatabaseSteps.find(Hotel.class)
   *                                   .update(UpdateOperators.set("address.city", "Ottawa"))
   *                                   .execute();}
   *                                   </pre>
   * @param <T>       extents from {@link MongoEntityInterface}
   * @return result for update {@link com.mongodb.client.result.UpdateResult}
   */
  @Step("Update entity in mongo database")
  public <T extends MongoEntityInterface> UpdateResult update(T entity, List<UpdateOperator> operators) {
    return noSqlFactory.getThreadLocalDatastore().find(entity.getClass())
        .filter(Filters.eq("_id", entity.getId()))
        .update(new UpdateOptions().multi(false), operators.toArray(new UpdateOperator[0]));
  }

  /**
   * Delete entity form database.
   *
   * @param entity entity for delete, extends from {@link MongoEntityInterface}
   * @param <T>    extents from {@link MongoEntityInterface}
   * @return result for delete {@link com.mongodb.client.result.DeleteResult}
   */
  @Step("Delete entity in mongo database")
  public <T extends MongoEntityInterface> DeleteResult delete(T entity) {
    return noSqlFactory.getThreadLocalDatastore().find(entity.getClass())
        .delete(new DeleteOptions().multi(true));
  }

  /**
   * Create query for select. For example:
   * <pre>
   * {@code
   * mongoDatabaseSteps
   *  .selectDsl(Hotel.class)
   *  .filter(Filters.gte("price", 1000));
   * }
   * </pre>
   *
   * @param entity object with query values
   * @param <T>    extents from {@link MongoEntityInterface}
   * @return query for execute {@link dev.morphia.query.Query}
   */
  @Step("Create query for request in mongo database")
  public <T extends MongoEntityInterface> Query<T> selectDsl(Class<T> entity) {
    return noSqlFactory.getThreadLocalDatastore().find(entity);
  }
}
