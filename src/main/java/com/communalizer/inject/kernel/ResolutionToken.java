package com.communalizer.inject.kernel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("UnusedDeclaration")
public abstract class ResolutionToken<T> {
    private final Type type;
    private final String name;

    public ResolutionToken() {
        this("");
    }

    public ResolutionToken(String name) {
        this.type = extractType(0);
        this.name = name;
    }

    private ResolutionToken(Type type) {
        this.name = null;
        this.type = type;
    }

    private Type extractType(int pos) {
        Type t = getClass().getGenericSuperclass();
        if (!(t instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Type parameter: T must be specified.");
        }

        ParameterizedType pt = (ParameterizedType) t;

        return pt.getActualTypeArguments()[pos];
    }

    public static <T> ResolutionToken<T> getToken(Class<T> type) {
        return new ResolutionToken<T>(type) {};
    }

    public String getKey() {
        String typeName = this.type.toString().replace("class ", "").replace("interface ", "");

        if (this.name == null || this.name.equals("")) {
            return typeName;
        } else {
            return String.format("%s-%s", typeName, this.name);
        }
    }
}
