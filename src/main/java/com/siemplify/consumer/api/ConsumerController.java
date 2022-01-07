package com.siemplify.consumer.api;

import com.siemplify.consumer.model.Task;
import com.siemplify.consumer.model.TaskStatusCount;
import com.siemplify.consumer.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(ConsumerController.PREFIX_URL)
public class ConsumerController {

    public static final String PREFIX_URL = "/api/consumer";

    @Value("${consumer.id}")
    private String consumerId;

    @Autowired
    private TaskRepository taskRepo;

    @GetMapping("/test")
    public ResponseEntity<String> test() {

        try {
            return ResponseEntity.ok("Ok");
        } catch (Exception e) {
            return new ResponseEntity("Test failed", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/tasks/latest/{limit}")
    public ResponseEntity<List<Task>> getLatestTasks(@PathVariable("limit") Integer limit) {

        try {
            List<Task> latest = taskRepo.getLatestTasks(consumerId, limit);
            return ResponseEntity.ok(latest);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve latest tasks", HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity("Failed to retrieve latest tasks", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/tasks/time")
    public ResponseEntity<Double> getAvgProcessingTime() {

        try {
            Double avgTime = taskRepo.getAvgExecutionTime(consumerId);
            return ResponseEntity.ok(avgTime);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve latest tasks", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/tasks/errors")
    public ResponseEntity<Double> getErrorPercentage() {

        try {
            Double errorRate = taskRepo.getErrorRate(consumerId);
            return ResponseEntity.ok(errorRate);
        } catch (Exception e) {
            return new ResponseEntity("Failed to retrieve latest tasks", HttpStatus.BAD_REQUEST);
        }
    }
}
