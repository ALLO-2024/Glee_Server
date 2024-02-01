package com.allo.server.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, String> redisBlackListTemplate;

    public void set(String roleName, Long id, String o, Integer minutes) {
        redisTemplate.opsForValue().set(roleName + id, o, minutes, TimeUnit.MINUTES);
    }

    public String get(String roleName, Long id) {
        return redisTemplate.opsForValue().get(roleName + id);
    }

    public boolean delete(String roleName, Long id) {
        return redisTemplate.delete(roleName + id);
    }

    public boolean hasKey(String roleName, Long id) {
        return redisTemplate.hasKey(roleName + id);
    }

    public void setBlackList(String key, String o, Integer milliSeconds) {
        redisBlackListTemplate.opsForValue().set(key, o, milliSeconds, TimeUnit.MILLISECONDS);
    }

    public String getBlackList(String key) {
        return redisBlackListTemplate.opsForValue().get(key);
    }

    public boolean deleteBlackList(String key) {
        return redisBlackListTemplate.delete(key);
    }

    public boolean hasKeyBlackList(String key) {
        return redisBlackListTemplate.hasKey(key);
    }
}