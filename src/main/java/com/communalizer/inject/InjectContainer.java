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
    private <T> T reflectInstance(Registration registration) {
        Type referencedType = registration.getComponent().getReferencedType();

        Class<T> clazz;
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
                    Class<?> dependency = (Class) dependencies[i];

                    ResolutionToken token = ResolutionToken.getToken(dependency);
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

    private <T> boolean isSatisfiable(Constructor<T> constructor) {
        Class<?>[] dependencies = constructor.getParameterTypes();

        for (Class<?> dependency : dependencies) {
            if (!registry.containsKey(dependency.getName())) return false;
        }

        return true;
    }

    @Override
    public Map<String, Registration> getRegistry() {
        return registry;
    }
}
