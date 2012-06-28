package com.communalizer.inject.kernel;


public class ExplicitDependency<T> {
    private final String propertyName;

    private T instance;
    private Factory<T> factory;
    private ResolutionToken<T> token;

    public String getPropertyName() {
        return propertyName;
    }

    public ExplicitDependency(String propertyName, T dependency) {
        verifyPropertyName(propertyName);

        this.propertyName = propertyName;
        this.instance = dependency;
    }

    public ExplicitDependency(String propertyName, Factory<T> factory) {
        verifyPropertyName(propertyName);

        this.propertyName = propertyName;
        this.factory = factory;
    }

    public ExplicitDependency(String propertyName, ResolutionToken<T> token) {
        verifyPropertyName(propertyName);

        this.propertyName = propertyName;
        this.token = token;
    }

    private void verifyPropertyName(String propertyName) {
        if (propertyName == null) {
            throw new IllegalArgumentException("Parameter: propertyName cannot be null.");
        }
    }

    public T getInstance() {
        return instance;
    }

    public T getFactoryArtefact() {
        return factory.create();
    }

    public ResolutionToken<T> getResolutionToken() {
        return token;
    }

    public ExplicitDependencyType getProviderType() {
        if (instance != null) {
            return ExplicitDependencyType.INSTANCE;
        }

        if (factory != null) {
            return ExplicitDependencyType.FACTORY;
        }

        return ExplicitDependencyType.RESOLUTION_TOKEN;
    }
}
