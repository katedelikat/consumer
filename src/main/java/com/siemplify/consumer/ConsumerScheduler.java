package com.siemplify.consumer;

import com.siemplify.consumer.model.Task;
import com.siemplify.consumer.model.TaskStatus;
import com.siemplify.consumer.repository.TaskRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsumerScheduler implements InitializingBean {

    @Value("${consumer.bulk.size: 2}")
    private Integer bulkSize;

    @Value("${consumer.id}")
    private String consumerId;

    @Autowired
    private TaskRepository taskRepo;

    @Scheduled(fixedRate = 5000)
    public void consumeTasks() {
        if (consumerId != null) {
            // mark tasks to be processed
            taskRepo.updateTaskStatusByLimit(consumerId, bulkSize);
            // select all marked
            List<Task> tasks = taskRepo.getTasksByConsumerId(consumerId);
            // set all marked to done
            tasks.forEach(item -> System.out.println(item.getTaskText()));
            taskRepo.setTasksStatus(consumerId, TaskStatus.DONE);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (consumerId == null) {
            System.err.println("Missing consumer ID");
        }
    }
}