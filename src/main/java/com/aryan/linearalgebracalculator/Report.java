package com.aryan.linearalgebracalculator;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Report implements LinearAlgebraObject {
    private String name;
    private String message;
    private HashMap<String, LinearAlgebraObject> content;
    public final String type = "report";

    // We can be sure that since the type extends LinearAlgebraObject it is safe
    @SuppressWarnings("unchecked")
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