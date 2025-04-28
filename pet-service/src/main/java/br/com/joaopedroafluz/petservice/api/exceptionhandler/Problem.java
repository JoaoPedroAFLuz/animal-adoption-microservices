package br.com.joaopedroafluz.petservice.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problem {

    private final Integer status;
    private final String type;
    private final String title;
    private final String detail;
    private final String userMessage;
    private final OffsetDateTime timestamp;
    private final List<Object> objects;

    @Getter
    @Builder
    public static class Object {

        private final String name;
        private final String userMessage;

    }

}
