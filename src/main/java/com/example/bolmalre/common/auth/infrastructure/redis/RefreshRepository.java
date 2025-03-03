package com.example.bolmalre.common.auth.infrastructure.redis;

import com.example.bolmalre.common.auth.infrastructure.port.RefreshRedisEntity;
import org.springframework.data.repository.CrudRepository;


public interface RefreshRepository extends CrudRepository<RefreshRedisEntity,String> {

    default boolean existsByRefresh(String refresh) {
        return findById(refresh).isPresent();
    }

    default void deleteByRefresh(String refresh) {
        deleteById(refresh);
    }

}
