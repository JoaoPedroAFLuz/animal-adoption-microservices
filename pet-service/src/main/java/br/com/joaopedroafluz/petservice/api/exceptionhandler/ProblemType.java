package br.com.joaopedroafluz.petservice.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    BUSINESS_ERROR("Business rule violation.", "/business-error"),
    RESOURCE_NOT_FOUND("Resource not found.", "/resource-not-found"),
    UNAUTHORIZED("Unauthorized.", "/unauthorized"),
    SYSTEM_ERROR("System error.", "/system-error");

    private final String title;
    private final String uri;

    ProblemType(String title, String path) {
        this.title = title;
        this.uri = "https://adoption.com.br" + path;
    }

}
