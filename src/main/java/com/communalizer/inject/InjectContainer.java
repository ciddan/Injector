package com.communalizer.inject;

import com.communalizer.inject.kernel.*;
import com.communalizer.inject.kernel.dependencies.ExplicitDependency;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import org.core4j.Enumerable;
import org.core4j.Func1;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class InjectContainer implements Container {
    //private final Map<String, Registration> registry = new HashMap<String, Registration>();
    private final Map<String, TypeProvider<?>> registry = new HashMap<String, TypeProvider<?>>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> void register(Registration<T, ?> registration) {
        TypeToken<T> providedTypeToken =
            (TypeToken<T>) registration.getComponent().getReferencedTypeToken();

        TypeProvider<T> provider =
            (TypeProvider<T>) registry.get(providedTypeToken.getKey());

        if (provider != null) {
            provider.addRegistration(registration);
        } else {
            registry.put(providedTypeToken.getKey(), new TypeProvider<T>(providedTypeToken));
        }
    }

    @Override
    public <T> void register(Registration<T, ?>... registrations) {
        for (Registration<T, ?> registration : registrations) {
            register(registration);
        }
    }

    @Override
    public void register(RegistrationBuilder builder) {
        register(builder.build());
    }

    @Override
    public void register(RegistrationBuilder... builders) {
        for (RegistrationBuilder builder : builders) {
            register(builder);
        }
    }

    @Override
    public <T> T resolve(ResolutionToken<T> token) {
        /*
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
        */
        
        return null;
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
            Paranamer paranamer = new BytecodeReadingParanamer();
            Constructor<T> constructor = selectGreediestMatchingConstructor(clazz);

            Type[] dependencies = constructor.getGenericParameterTypes();

            if (dependencies != null && dependencies.length > 0) {
                Object[] initArgs = new Object[dependencies.length];

                for (int i = 0; i < dependencies.length; i++) {
                    if (registration.hasExplicitDependencies()) {
                        String parameterName = paranamer.lookupParameterNames(constructor)[i];
                        ExplicitDependency dep = registration.getDependency(parameterName);

                        if (dep != null) {
                            switch (dep.getProviderType()) {
                                case INSTANCE:
                                    initArgs[i] = dep.getInstance();
                                break;

                                case FACTORY:
                                    initArgs[i] = dep.getFactoryArtifact();
                                break;

                                case RESOLUTION_TOKEN:
                                    initArgs[i] = resolve(dep.getResolutionToken());
                                break;
                            }


                        } else {
                            ResolutionToken token = ResolutionToken.getToken(dependencies[i]);
                            initArgs[i] = resolve(token);
                        }
                    } else {
                        ResolutionToken token = ResolutionToken.getToken(dependencies[i]);
                        initArgs[i] = resolve(token);
                    }
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
