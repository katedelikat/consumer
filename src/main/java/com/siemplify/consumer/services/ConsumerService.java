package com.siemplify.consumer.services;

import com.siemplify.consumer.model.Consumer;
import com.siemplify.consumer.model.Task;
import com.siemplify.consumer.model.TaskStatus;
import com.siemplify.consumer.repository.ConsumerRepository;
import com.siemplify.consumer.repository.TaskRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ConsumerService implements InitializingBean {

    @Value("${consumer.bulk.size: 2}")
    private Integer bulkSize;

    @Value("${consumer.id}")
    private String consumerId;

    @Autowired
    private ConsumerRepository consumerRepo;

    @Autowired
    private TaskRepository taskRepo;

    private ScheduledExecutorService consumerExecutorService = null;
    private ScheduledExecutorService configurationExecutorService = Executors.newSingleThreadScheduledExecutor();
    private Consumer currentConfig;



    @Override
    public void afterPropertiesSet() throws Exception {
        if (consumerId == null) {
            System.err.println("Missing consumer ID");
        }
        Optional<Consumer> consumerMaybe = consumerRepo.findById(consumerId);
        Consumer consumer = null;
        if (!consumerMaybe.isPresent()) {
            // create default
            Consumer entity = new Consumer();
            entity.setId(consumerId);
            entity.setBulkSize(bulkSize);
            consumer = consumerRepo.save(entity);
        } else {
            if (!consumerMaybe.get().validate()) {
                System.err.println("Invalid configuration for consumer " + consumerId);
            } else {
                consumer = consumerMaybe.get();
            }
        }
        if (consumer != null) {
            currentConfig = consumer;
            startConsumeScheduler(consumer);
            // scheduler for picking up changed confuguration
            startConfigScheduler(consumer);
        } else {
            System.err.println("Invalid configuration");
        }
    }

    private void startConfigScheduler(Consumer consumer) {

        Runnable checkConfigurationTask = () -> {
            Optional<Consumer> newConfigMaybe = consumerRepo.findById(currentConfig.getId());
            if (!newConfigMaybe.isPresent()) {
                System.out.println("No configuration present - shutting down");
                shutDownConsumeExecutor();
            } else {
                Consumer newConfig = newConfigMaybe.get();
                if (!newConfig.equals(currentConfig)) {
                    System.out.println("Configuration changed");
                    shutDownConsumeExecutor();
                    currentConfig = newConfig;
                    startConsumeScheduler(currentConfig);
                }
            }
        };

        configurationExecutorService.scheduleAtFixedRate(checkConfigurationTask, 1, 1, TimeUnit.MINUTES);
    }

    private void startConsumeScheduler(Consumer consumer) {

        Runnable consumeTask = () -> {
            taskRepo.updateTasksToBeProcessed(consumer.getId(), consumer.getBulkSize());
            List<Task> tasks = taskRepo.getTasksByConsumerId(consumer.getId());
            tasks.forEach(item -> {
                if (!StringUtils.hasLength(item.getTaskText())) {
                    taskRepo.setTaskStatus(item.getId(), TaskStatus.ERROR);
                } else {
                    System.out.println(item.getTaskText());
                    taskRepo.setTaskStatus(item.getId(), TaskStatus.DONE);
                }
            });
        };

        consumerExecutorService = Executors.newSingleThreadScheduledExecutor();
        consumerExecutorService.scheduleAtFixedRate(consumeTask, 5, 5, TimeUnit.SECONDS);
    }

    public void updateConsumerConfig(List<Consumer> config) {
        config.forEach(item -> {
            if (item.validate()) {
                consumerRepo.save(item);
            }
        });
    }

    private void shutDownConsumeExecutor() {
        consumerExecutorService.shutdown();
        try {
            if (!consumerExecutorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                consumerExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            consumerExecutorService.shutdownNow();
        }
    }
}
