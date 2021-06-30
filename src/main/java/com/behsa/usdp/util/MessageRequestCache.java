package com.behsa.usdp.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class MessageRequestCache {
    private LoadingCache<Integer, String> cache;
    @Value("${send.message.response.time.out.mills}")
    private Long expireAfterWrite;

    @PostConstruct
    public void init() {
        CacheLoader<Integer, String> loader;
        loader = new CacheLoader<Integer, String>() {
            @Override
            public String load(Integer key) throws ExecutionException {
                return cache.get(key);
            }
        };

        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expireAfterWrite, TimeUnit.MILLISECONDS)
                .build(loader);
    }

    public void put(Integer key, String value) {
        this.cache.put(key, value);
    }

    public String get(Integer key) {
        return this.cache.getIfPresent(key);
    }
}
