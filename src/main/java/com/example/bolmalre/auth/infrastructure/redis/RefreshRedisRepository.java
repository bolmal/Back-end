package com.example.bolmalre.auth.infrastructure.redis;

import com.example.bolmalre.auth.infrastructure.port.RefreshRedisEntity;
import org.springframework.data.repository.CrudRepository;


public interface RefreshRedisRepository extends CrudRepository<RefreshRedisEntity,String> {

}
