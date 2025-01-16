package com.example.bolmal.auth.infrastructure.port;


import com.example.bolmal.auth.domain.Refresh;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;
import lombok.*;

@RedisHash(value = "Refresh",timeToLive = 60 * 1000L)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshRedisEntity {

    private String id;  // Redis에서 id는 보통 String 타입입니다.

    @Id
    private String username;
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
