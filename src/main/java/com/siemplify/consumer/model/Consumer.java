package com.siemplify.consumer.model;


import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="consumers")
public class Consumer {

    @Id
    private String id;

    @Column(name = "bulk_size")
    private Integer bulkSize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getBulkSize() {
        return bulkSize;
    }

    public void setBulkSize(Integer bulkSize) {
        this.bulkSize = bulkSize;
    }

    @Transient
    public boolean validate() {
        return StringUtils.hasLength(id) && bulkSize != null && bulkSize > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consumer consumer = (Consumer) o;
        return Objects.equals(id, consumer.id) && Objects.equals(bulkSize, consumer.bulkSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bulkSize);
    }
}
