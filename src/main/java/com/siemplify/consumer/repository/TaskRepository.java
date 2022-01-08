package com.siemplify.consumer.repository;

import com.siemplify.consumer.model.Task;
import com.siemplify.consumer.model.TaskStatus;
import com.siemplify.consumer.model.TaskStatusCount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {


    @Transactional
    @Modifying
    @Query(value =
            "WITH task_sel AS (" +
                    " SELECT id, row_number() OVER (ORDER BY tasks DESC NULLS LAST) AS rn" +
                    " FROM   tasks" +
                    " WHERE consumer_id= :consumerId" +
                    " AND task_status IN ('PENDING', 'IN_PROGRESS')" +
                    " ORDER  BY creation_time ASC NULLS LAST" +
                    " LIMIT  :limit" +
                    ")" +
                    " UPDATE tasks t" +
                    " SET    task_status = 'IN_PROGRESS'," +
                    " modification_time = NOW()" +
                    " FROM   task_sel" +
                    " WHERE  task_sel.id = t.id",
            nativeQuery = true)
    void updateTasksToBeProcessed(@Param("consumerId") String consumerId, @Param("limit") Integer limit);



    @Query(value = "SELECT t FROM Task t WHERE t.consumerId = :consumerId AND task_status='IN_PROGRESS'")
    List<Task> getTasksByConsumerId(@Param("consumerId") String consumerId);



    @Transactional
    @Modifying
    @Query(value = "UPDATE Task t" +
            " SET t.taskStatus = :taskStatus, t.modificationTime = NOW()" +
            " WHERE t.id = :taskId")
    void setTaskStatus(@Param("taskId") Long taskId, @Param("taskStatus") TaskStatus taskStatus);



    @Query(value = "SELECT * FROM tasks t WHERE t.consumer_id IN(:consumerIds) AND t.task_status='DONE' ORDER BY t.creation_time DESC LIMIT :limit",
            nativeQuery = true)
    List<Task> getLatestTasks(@Param("consumerIds") List<String> consumerIds, @Param("limit") Integer limit);



    @Query(value = "SELECT t FROM Task t WHERE t.consumerId = :consumerId AND t.taskStatus!='DONE' ORDER BY t.creationTime ASC")
    List<Task> getUncompletedTasks(@Param("consumerId") String consumerId);



    @Query(value = "SELECT new com.siemplify.consumer.model.TaskStatusCount(t.taskStatus, COUNT(t.taskStatus)) " +
            " FROM Task t " +
            " WHERE t.consumerId = :consumerId " +
            " GROUP BY t.taskStatus")
    List<TaskStatusCount> getTaskSummaryByStatus(@Param("consumerId") String consumerId);



    @Query(value = "SELECT " +
            "cast(errs as decimal) /total FROM" +
            " (SELECT" +
            " (SELECT COUNT(*) from tasks where consumer_id= :consumerId and task_status IN ('DONE', 'ERROR')) as total," +
            " (SELECT COUNT(*) from tasks where consumer_id= :consumerId and task_status IN ('ERROR')) as errs" +
            ") as toterr",
            nativeQuery = true)
    Double getErrorRate(@Param("consumerId") String consumerId);



    @Query(value = "SELECT AVG(extract(epoch from modification_time) - extract(epoch from creation_time)) from tasks" +
            " WHERE consumer_id= :consumerId and task_status = 'DONE'",
            nativeQuery = true)
    Double getAvgExecutionTime(@Param("consumerId") String consumerId);
}
