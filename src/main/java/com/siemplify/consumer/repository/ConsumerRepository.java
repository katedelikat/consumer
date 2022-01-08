package com.siemplify.consumer.repository;

import com.siemplify.consumer.model.Consumer;
import com.siemplify.consumer.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface ConsumerRepository extends CrudRepository<Consumer, String> {

}
