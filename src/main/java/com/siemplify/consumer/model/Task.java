package com.siemplify.consumer.model;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Types;
import java.time.OffsetDateTime;

@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_time")
    private OffsetDateTime creationTime;

    @Column(name = "modification_time")
    private OffsetDateTime modificationTime;

    @Column(name = "task_text")
    private String taskText;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private TaskStatus taskStatus;

    @Column(name = "consumer_id")
    private String consumerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(OffsetDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public OffsetDateTime getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(OffsetDateTime modificationTime) {
        this.modificationTime = modificationTime;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }
}
