package com.example.bolmal.auth.service.port;

import com.example.bolmal.auth.domain.Refresh;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshRepository {

    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);

    Refresh save(Refresh refresh);
}
