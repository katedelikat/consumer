package com.siemplify.consumer.api;

import com.siemplify.consumer.dto.ConsumerIdWrapper;
import com.siemplify.consumer.dto.TaskErrorPerc;
import com.siemplify.consumer.dto.TaskExecutionTime;
import com.siemplify.consumer.model.Consumer;
import com.siemplify.consumer.model.Task;
import com.siemplify.consumer.model.TaskStatusCount;
import com.siemplify.consumer.repository.TaskRepository;
import com.siemplify.consumer.services.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ConsumerController.PREFIX_URL)
public class ConsumerController {

    public static final String PREFIX_URL = "/api/consumer";

    @Value("${consumer.id}")
    private String consumerId;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {

        try {
            return ResponseEntity.ok("Ok");
        } catch (Exception e) {
            return new ResponseEntity("Test failed", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves @limit latest task for given @consumerIds.
     * @Consumer ids are passed in the bodyof the method.
     * Example:
     * POST http://127.0.0.1:9000/api/consumer/tasks/latest/10
     * {
     *     "consumer_ids": ["ABC", "XYZ"]
     * }
     * @param limit
     * @param consumerIds
     * @return
     */
    @PostMapping("/tasks/latest/{limit}")
    public ResponseEntity<List<Task>> getLatestTasks(@PathVariable("limit") Integer limit, @RequestBody ConsumerIdWrapper consumerIds) {

        try {
            List<Task> latest = taskRepo.getLatestTasks(consumerIds.getConsumerIds(), limit);
            return ResponseEntity.ok(latest);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve latest tasks", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This api changes the configuration of consumer.
     * Currently only bulk size is supported.
     * Consumer checks if the configuration was changed every 1 minute
     * Example:
     * PUT http://127.0.0.1:9000/api/consumer/config
     * [
     *     {
     *         "id": "ABC",
     *         "bulkSize": 1
     *     }
     * ]
     * @param consumerConfig
     * @return
     */
    @PutMapping("/config")
    public ResponseEntity<String> setConsumerConfig(@RequestBody List<Consumer> consumerConfig) {

        try {
            consumerService.updateConsumerConfig(consumerConfig);
            return ResponseEntity.ok("Done");
        } catch (Exception e) {
            return new ResponseEntity("Failed to update consumer config", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This api retrieves summary of tasks by their status, aka how many tasks are there for each status
     * Example:
     * GET http://127.0.0.1:9000/api/consumer/tasks/summary/ABC
     * @param consumerId
     * @return
     */
    @GetMapping("/tasks/summary/{consumerId}")
    public ResponseEntity<List<TaskStatusCount>> getTaskSummary(@PathVariable("consumerId") String consumerId) {

        try {
            List<TaskStatusCount> summary = taskRepo.getTaskSummaryByStatus(consumerId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve tasks summary", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This api return average execution of time of tasks for giver consumer.
     * Execution time is calculated since the task si submitted until executed successfully.
     * Example:
     * GET http://127.0.0.1:9000/api/consumer/tasks/time/ABC
     * @param consumerId
     * @return
     */
    @GetMapping("/tasks/time/{consumerId}")
    public ResponseEntity<TaskExecutionTime> getAvgProcessingTime(@PathVariable("consumerId") String consumerId) {

        try {
            Double avgTime = taskRepo.getAvgExecutionTime(consumerId);
            TaskExecutionTime taskExecutionTime = new TaskExecutionTime(consumerId, Math.round(avgTime * 100.0) / 100.0);
            return ResponseEntity.ok(taskExecutionTime);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve avg execution time", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This API returns error rate of task processing for given consumer.
     * Example:
     * GET http://127.0.0.1:9000/api/consumer/tasks/errors/ABC
     * @param consumerId
     * @return
     */
    @GetMapping("/tasks/errors/{consumerId}")
    public ResponseEntity<TaskErrorPerc> getErrorPercentage(@PathVariable("consumerId") String consumerId) {

        try {
            Double errorRate = taskRepo.getErrorRate(consumerId);
            TaskErrorPerc taskErrorPerc = new TaskErrorPerc(consumerId, Math.round(errorRate * 100.0) / 100.0);
            return ResponseEntity.ok(taskErrorPerc);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve percentage rate", HttpStatus.BAD_REQUEST);
        }
    }
}
