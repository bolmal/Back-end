package com.example.bolmal.auth.mock;

import com.example.bolmal.auth.domain.Refresh;
import com.example.bolmal.auth.service.port.RefreshRepository;
import com.example.bolmal.member.domain.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class FakeRefreshRepository implements RefreshRepository {

    private final AtomicLong counter = new AtomicLong(0);
    private final List<Refresh> data = new ArrayList<>();


    @Override
    public Boolean existsByRefresh(String refresh) {
        return null;
    }

    @Override
    public void deleteByRefresh(String refresh) {

    }

    @Override
    public Refresh save(Refresh refresh) {

        Refresh refresh1 = Refresh.builder()
                .username(refresh.getUsername())
                .refresh(refresh.getRefresh())
                .expiration(refresh.getExpiration())
                .build();

        data.add(refresh1);
        return refresh1;
    }
}
