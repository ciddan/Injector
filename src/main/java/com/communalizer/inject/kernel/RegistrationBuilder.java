package com.communalizer.inject.kernel;

public class RegistrationBuilder {
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

        return registration;
    }
}
