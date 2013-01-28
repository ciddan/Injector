package com.communalizer.inject.kernel;

import java.util.HashMap;
import java.util.Map;

public class TypeProvider<T> {
    private final TypeToken<T> providedType;
    private final Map<String, Registration<T, ?>> registry = new HashMap<String, Registration<T, ?>>();

    public TypeProvider(TypeToken<T> providedType) {
        if (providedType == null) {
            throw new IllegalArgumentException("Parameter: providedType cannot be null.");
        }

        this.providedType = providedType;
    }

    public TypeToken<T> getProvidedType() {
        return this.providedType;
    }

    public Map<String, Registration<T, ?>> getRegistry() {
        return registry;
    }

    public <TImpl> void addRegistration(Registration<T, TImpl> registration) {
        String key = registration.getResolutionKey();
        if (registry.containsKey(key)) {
            throw new RuntimeException(
                String.format(
                    "A registration with key: %s already exists. If you're registering multiple components of the same " +
                    "base- and referenced type, consider naming them.",
                    registration.getResolutionKey()
                )
            );
        }

        registry.put(key, registration);
    }

    public Registration<T, ?> getRegistration(TypeToken<T> token, String name) {
        String key = (name != null && !name.equals(""))
            ? String.format("%s-%s", token.getKey(), name)
            : token.getKey();

        return registry.get(key);
    }
}