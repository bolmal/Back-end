package com.example.bolmal.auth.infrastructure.redis;

import com.example.bolmal.auth.infrastructure.port.RefreshRedisEntity;
import org.springframework.data.repository.CrudRepository;


public interface RefreshRedisRepository extends CrudRepository<RefreshRedisEntity,String> {

}
