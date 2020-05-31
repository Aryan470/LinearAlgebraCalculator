package com.aryan.linearalgebracalculator;

import java.util.HashMap;

public class Report implements LinearAlgebraObject {
    private String message;

    public Report(String message) {
        this.message = message;
    }

    public <T extends LinearAlgebraObject> Report(HashMap<String, T> objectList) {
        this.message = "";
        for (String key : objectList.keySet()) {
            LinearAlgebraObject thisObject = objectList.get(key);
            if (thisObject instanceof UserFunction) {
                this.message += thisObject;
            } else {
                this.message += String.format("%s = %s", key, thisObject);
            }
            this.message += "\n";
        }
    }

    public String toString() {
        return message;
    }
}