package com.example.bolmalre.auth.infrastructure.port;


import com.example.bolmalre.auth.domain.Refresh;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

// 30일로 설정 - 하드코딩으로 되어있어서 수정이 필요함 FIXME
@RedisHash(value = "Refresh", timeToLive = 30L * 24 * 60 * 60)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshRedisEntity {

    private String id;  // Redis에서 id는 보통 String 타입입니다.
    private String username;

    @Id
    private String refresh;
    private String expiration;

    public static RefreshRedisEntity fromModel(Refresh refresh) {
        return RefreshRedisEntity.builder()
                .id(refresh.getId().toString())  // id를 String으로 변환하여 사용
                .username(refresh.getUsername())
                .refresh(refresh.getRefresh())
                .expiration(refresh.getExpiration())
                .build();
    }

    public Refresh toModel() {
        return Refresh.builder()
                .id(Long.valueOf(id))  // Redis의 id는 String이므로 Long으로 변환
                .username(username)
                .refresh(refresh)
                .expiration(expiration)
                .build();
    }
}
