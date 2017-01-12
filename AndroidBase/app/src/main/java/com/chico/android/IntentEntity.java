package com.chico.android;

/**
 * Created on 2017/1/11.
 * Author Chico Chen
 */

public class IntentEntity {
    private String name;
    private Class<?> className;

    public IntentEntity(String name, Class<?> className) {
        this.name = name;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClassName() {
        return className;
    }

    public void setClassName(Class<?> className) {
        this.className = className;
    }
}
