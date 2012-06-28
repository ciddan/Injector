package com.communalizer.inject.kernel;

import com.communalizer.inject.kernel.dependencies.ExplicitDependency;
import com.communalizer.inject.kernel.dependencies.ParameterDependency;

import java.util.ArrayList;
import java.util.List;

public class RegistrationBuilder {
    private List<ExplicitDependency> dependencies = new ArrayList<ExplicitDependency>();
    private Component component;
    private Factory factory;
    private Object instance;
    private String name;

    public static RegistrationBuilder registration() {
        return new RegistrationBuilder();
    }

    public RegistrationBuilder component(Component component) {
        this.component = component;

        return this;
    }

    public RegistrationBuilder factory(Factory factory) {
        this.factory = factory;

        return this;
    }

    public RegistrationBuilder instance(Object instance) {
        this.instance = instance;

        return this;
    }

    public RegistrationBuilder named(String name) {
        this.name = name;

        return this;
    }

    @SuppressWarnings("unchecked")
    public Registration build() {
        Registration registration = new Registration(component);

        registration.setName(name);
        registration.setFactory(factory);
        registration.setInstance(instance);

        for (ExplicitDependency dependency : dependencies) {
            registration.addDependency(dependency);
        }

        return registration;
    }

    public <T> RegistrationBuilder dependsOn(String parameterName, T value) {
        this.dependencies.add(new ParameterDependency<T>(parameterName, value));

        return this;
    }

    public <T> RegistrationBuilder dependsOn(String parameterName, Factory<T> factory) {
        this.dependencies.add(new ParameterDependency<T>(parameterName, factory));

        return this;
    }

    public <T> RegistrationBuilder dependsOn(String parameterName, ResolutionToken<T> token) {
        this.dependencies.add(new ParameterDependency<T>(parameterName, token));

        return this;
    }
}
