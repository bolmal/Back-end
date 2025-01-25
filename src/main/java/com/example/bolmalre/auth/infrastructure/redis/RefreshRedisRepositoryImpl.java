package com.example.bolmalre.auth.infrastructure.redis;


import com.example.bolmalre.auth.domain.Refresh;
import com.example.bolmalre.auth.infrastructure.port.RefreshRedisEntity;
import com.example.bolmalre.auth.service.port.RefreshRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshRedisRepositoryImpl implements RefreshRepository {

    private final RefreshRedisRepository repository;

    public RefreshRedisRepositoryImpl(@Lazy RefreshRedisRepository repository) {
        this.repository = repository;
    }

    @Override
    public Boolean existsByRefresh(String refresh) {
        return repository.existsById(refresh);
    }

    @Override
    public void deleteByRefresh(String refresh) {
        repository.deleteById(refresh);
    }

    @Override
    public Refresh save(Refresh refresh) {
        return repository.save(RefreshRedisEntity.fromModel(refresh)).toModel();
    }
}
