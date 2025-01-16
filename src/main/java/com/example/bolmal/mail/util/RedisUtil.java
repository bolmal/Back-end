package com.example.bolmal.mail.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;


@RequiredArgsConstructor
@Service
public class RedisUtil {
    private final StringRedisTemplate template;

    /**

     key 값을 이용하여 value를 가져오는 메서드

     * */
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    /**

     key에 해당하는 데이터가 있는지 조회하는 메서드

     */
    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    /**

     key/value로 데이터를 저장하는 메서드
     + 만료시간을 설정 할 수 있다

     */
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        template.delete(key);
    }
}