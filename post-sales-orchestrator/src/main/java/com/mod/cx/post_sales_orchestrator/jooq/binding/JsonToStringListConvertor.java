package com.mod.cx.post_sales_orchestrator.jooq.binding;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.Converter;
import org.jooq.JSON;

import java.util.List;

public class JsonToStringListConvertor implements Converter<JSON, List<String>> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public List<String> from(JSON databaseObject) {
        if(databaseObject == null || databaseObject.data() == null) return null;

        try {
            return MAPPER.readValue(databaseObject.data(), new TypeReference<>() {});
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON from database",e);
        }
    }

    @Override
    public JSON to(List<String> userObject) {
        if(userObject == null) return null;

        try {
            return JSON.valueOf(MAPPER.writeValueAsString(userObject));
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to serialize list to json",e);
        }
    }

    @Override
    public Class<JSON> fromType() {
        return JSON.class;
    }

    @Override
    public Class<List<String>> toType() {
        return (Class<List<String>>) (Class) List.class;
    }
}
