package com.siemplify.consumer.model;

public class TaskStatusCount {

    private TaskStatus taskStatus;
    private Long count;

    public TaskStatusCount(TaskStatus taskStatus, Long count) {
        this.taskStatus = taskStatus;
        this.count = count;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
