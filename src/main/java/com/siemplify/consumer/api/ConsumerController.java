package com.siemplify.consumer.api;

import com.siemplify.consumer.dto.ConsumerIdWrapper;
import com.siemplify.consumer.model.Consumer;
import com.siemplify.consumer.model.Task;
import com.siemplify.consumer.model.TaskStatusCount;
import com.siemplify.consumer.repository.TaskRepository;
import com.siemplify.consumer.services.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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

    @PostMapping("/tasks/latest/{limit}")
    public ResponseEntity<List<Task>> getLatestTasks(@PathVariable("limit") Integer limit, @RequestBody @Nullable ConsumerIdWrapper consumerIds) {

        try {
            List<String> requestedConsumerIds = consumerIds != null && consumerIds.getConsumerIds() != null ? consumerIds.getConsumerIds() : List.of(consumerId);
            List<Task> latest = taskRepo.getLatestTasks(requestedConsumerIds, limit);
            return ResponseEntity.ok(latest);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve latest tasks", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/config")
    public ResponseEntity<String> setConsumerConfig(@RequestBody List<Consumer> consumerConfig) {

        try {
            consumerService.updateConsumerConfig(consumerConfig);
            return ResponseEntity.ok("Done");
        } catch (Exception e) {
            return new ResponseEntity("Failed to update consumer config", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/tasks/uncompleted")
    public ResponseEntity<List<Task>> getUncompleted() {

        try {
            List<Task> uncompleted = taskRepo.getUncompletedTasks(consumerId);
            return ResponseEntity.ok(uncompleted);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve uncompleted tasks", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/tasks/summary")
    public ResponseEntity<List<TaskStatusCount>> getTaskSummary() {

        try {
            List<TaskStatusCount> summary = taskRepo.getTaskSummaryByStatus(consumerId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve tasks summary", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/tasks/time")
    public ResponseEntity<Double> getAvgProcessingTime() {

        try {
            Double avgTime = taskRepo.getAvgExecutionTime(consumerId);
            return ResponseEntity.ok(avgTime);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve avg execution time", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/tasks/errors")
    public ResponseEntity<Double> getErrorPercentage() {

        try {
            Double errorRate = taskRepo.getErrorRate(consumerId);
            return ResponseEntity.ok(errorRate);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve percentage rate", HttpStatus.BAD_REQUEST);
        }
    }
}
