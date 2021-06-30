package com.behsa.usdp.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

public class StringSanitizeDeserializer implements JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String object = json.getAsString();
        if ("null".equals(object) || StringUtils.isEmpty(object)) {
            object = null;
        }
        return object;
    }
}
