package br.com.joaopedroafluz.notificationservice.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
@UtilityClass
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public static <T> T fromJson(String json, Class<T> resultClass) {
        try {
            return OBJECT_MAPPER.readValue(json, resultClass);
        } catch (IOException e) {
            var message = String.format("Error in converting %s to: %s", json, resultClass.getSimpleName());
            throw new IllegalArgumentException(message, e);
        }
    }

}