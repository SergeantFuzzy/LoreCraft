package com.sergeantfuzzy.lorecraft.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VarsService {
    private final Map<UUID, Map<String, String>> vars = new ConcurrentHashMap<>();


    public void set(UUID playerId, String key, String value) {
        vars.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>()).put(key, value);
    }
    public Optional<String> get(UUID playerId, String key) {
        return Optional.ofNullable(vars.getOrDefault(playerId, Collections.emptyMap()).get(key));
    }
    public Map<String, String> all(UUID playerId) {
        return Collections.unmodifiableMap(vars.getOrDefault(playerId, Collections.emptyMap()));
    }
}