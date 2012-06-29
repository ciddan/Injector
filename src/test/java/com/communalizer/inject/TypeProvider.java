package com.communalizer.inject;

import com.communalizer.inject.kernel.TypeToken;

public class TypeProvider<T> {
    private final TypeToken<T> providedType;

    public TypeProvider(TypeToken<T> providedType) {
        if (providedType == null) {
            throw new IllegalArgumentException("Parameter: providedType cannot be null.");
        }

        this.providedType = providedType;
    }


}
