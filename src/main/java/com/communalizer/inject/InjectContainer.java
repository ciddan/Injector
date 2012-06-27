package com.communalizer.inject;

import com.communalizer.inject.kernel.Registration;
import com.communalizer.inject.kernel.ResolutionToken;
import org.core4j.Enumerable;
import org.core4j.Func1;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
                return reflectInstance(registration);

            default:
                @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
                T instance = (T) registration.getComponent().getOrCreateInstance();

                return instance;
        }

    }

    @SuppressWarnings("unchecked")
    /**
     * Attempts to create an instance of the requested {@link Registration} by reflection.
     */
    private <T> T reflectInstance(Registration registration) {
        Class<T> clazz;
        Type referencedType = registration.getComponent().getReferencedType();

        if (referencedType instanceof ParameterizedType) {
            clazz = ((Class<T>) ((ParameterizedType) referencedType).getRawType());
        } else {
            clazz =  (Class<T>) referencedType;
        }

        try {
            Constructor<T> constructor = selectGreediestMatchingConstructor(clazz);
            Type[] dependencies = constructor.getGenericParameterTypes();

            if (dependencies != null && dependencies.length > 0) {
                Object[] initArgs = new Object[dependencies.length];

                for (int i = 0; i < dependencies.length; i++) {
                    ResolutionToken token = ResolutionToken.getToken(dependencies[i]);
                    initArgs[i] = resolve(token);
                }

                return constructor.newInstance(initArgs);
            }

            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * Evaluates all available constructors and selects the largest satisfiable constructor
     */
    private <T> Constructor<T> selectGreediestMatchingConstructor(Class<T> type) {
        Enumerable<Constructor<T>> constructors =
                Enumerable.create((Constructor<T>[]) type.getConstructors())
                        .orderBy(new Func1<Constructor<T>, Comparable>() {
                            @Override
                            public Comparable apply(Constructor<T> tConstructor) {
                                return tConstructor.getTypeParameters().length;
                            }
                        })
                        .reverse();

        for (Constructor<T> constructor : constructors) {
            if (isSatisfiable(constructor)) {
                return constructor;
            }
        }

        return null;
    }

    /**
     * Evaluates a {@link Constructor} to see whether or not all necessary dependencies are registered with the
     * container.
     * @param constructor           The {@link Constructor} to inspect.
     * @param <T>                   Type of the {@link Constructor}.
     * @return                      A boolean indicating whether all dependencies can be satisfied.
     */
    private <T> boolean isSatisfiable(Constructor<T> constructor) {
        Type[] dependencies = constructor.getGenericParameterTypes();

        for (Type dependency : dependencies) {
            ResolutionToken token = ResolutionToken.getToken(dependency);

            if (!registry.containsKey(token.getKey())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Map<String, Registration> getRegistry() {
        return registry;
    }
}
