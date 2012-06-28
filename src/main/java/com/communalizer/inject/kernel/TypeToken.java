package com.communalizer.inject.kernel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeToken<T> {
    private final Type type;

    public TypeToken() {
        this.type = extractType(0);
    }

    private TypeToken(Type type) {
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

    public static <T> TypeToken<T> getToken(Class<T> type) {
        return new TypeToken<T>(type) {};
    }

    public String getKey() {
        return this.type.toString().replace("class ", "").replace("interface ", "");
    }
}
