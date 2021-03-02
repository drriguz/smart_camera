package com.riguz.smart_camera.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractResult {
    protected final Map<String, Object> result = new HashMap<>();

    protected void set(String key, Object value) {
        result.put(key, value);
    }

    public Object get(String key) {
        return result.get(key);
    }

    public Map<String, Object> getResult() {
        return Collections.unmodifiableMap(result);
    }
}
