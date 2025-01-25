package com.example.bolmalre.auth.service.port;

import com.example.bolmalre.auth.domain.Refresh;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshRepository {

    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);

    Refresh save(Refresh refresh);
}
