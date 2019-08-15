package no.progconsult.aws.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class JsonUtil {

    static Map<Object, Object> jsonToMap(String json) {
        try {
            Map<Object, Object> map = new ObjectMapper().readValue(json, new TypeReference<HashMap<Object, Object>>() {});
            return map;
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    static <T extends Object> String mapToJson(Map<T, Object> map) {
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

