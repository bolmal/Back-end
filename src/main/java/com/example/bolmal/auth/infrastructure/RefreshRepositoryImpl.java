package com.example.bolmal.auth.infrastructure;

import com.example.bolmal.auth.domain.Refresh;
import com.example.bolmal.auth.domain.RefreshEntity;
import com.example.bolmal.auth.service.port.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshRepositoryImpl implements RefreshRepository {

    private final RefreshJpaRepository refreshJpaRepository;

    @Override
    public Boolean existsByRefresh(String refresh) {
        return refreshJpaRepository.existsByRefresh(refresh);
    }

    @Override
    public void deleteByRefresh(String refresh) {
        refreshJpaRepository.deleteByRefresh(refresh);
    }

    @Override
    public Refresh save(Refresh refresh) {
        return refreshJpaRepository.save(RefreshEntity.fromModel(refresh)).toModel();
    }
}
