package com.communalizer.inject;

import com.communalizer.inject.kernel.Registration;
import com.communalizer.inject.kernel.ResolutionToken;

import java.util.HashMap;
import java.util.Map;

public class InjectContainer implements Container {
    private final Map<String, Registration> registry = new HashMap<String, Registration>();

    @Override
    public void register(Registration registration) {
        if (registry.containsKey(registration.getKey())) {
            throw new RuntimeException(
                String.format(
                    "A registration with key: %s already exists. If registering multiple implementations of the same " +
                    "interface or base type, consider naming your registrations.",
                    registration.getKey()
                )
            );
        }

        registry.put(registration.getKey(), registration);
    }

    @Override
    public void register(Registration... registrations) {
        for (Registration registration : registrations) {
            register(registration);
        }
    }

    @Override
    public <T> T resolve(ResolutionToken<T> token) {
        if (token == null) {
            throw new IllegalArgumentException("Parameter: token cannot be null.");
        }

        Registration registration = registry.get(token.getKey());
        if (registration == null) {
            throw new RuntimeException(String.format("Could not find a registration matching type token: %s.", token.getKey()));
        }

        switch (registration.getComponent().getComponentType()) {
            case REFLECTION:
                return null;

            default:
                @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
                T instance = (T) registration.getComponent().getOrCreateInstance();

                return instance;
        }

    }

    @Override
    public Map<String, Registration> getRegistry() {
        return registry;
    }
}
