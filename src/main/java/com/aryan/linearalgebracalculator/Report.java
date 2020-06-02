package com.aryan.linearalgebracalculator;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(include=JsonTypeInfo.As.PROPERTY, use=JsonTypeInfo.Id.NAME)
public class Report implements LinearAlgebraObject {
    private String name;
    private String message;
    private HashMap<String, LinearAlgebraObject> content;

    public Report(String name, HashMap<String, ? extends LinearAlgebraObject> content) {
        this.name = name;
        this.content = (HashMap<String, LinearAlgebraObject>) (content);
        this.message = null;
    }

    public Report(String name, String message) {
        this.name = name;
        this.content = null;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, LinearAlgebraObject> getContent() {
        return content;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return name;
    }
}