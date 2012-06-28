package com.communalizer.inject.kernel;


public class ExplicitDependency<T> {
    private final String propertyName;

    private T instance;
    private Factory<T> factory;
    private ResolutionToken<T> token;

    public String getParameterName() {
        return propertyName;
    }

    public ExplicitDependency(String propertyName, T dependency) {
        verifyPropertyName(propertyName);

        this.propertyName = propertyName;
        this.instance = dependency;
    }

    public ExplicitDependency(String parameterName, Factory<T> factory) {
        verifyPropertyName(parameterName);

        this.propertyName = parameterName;
        this.factory = factory;
    }

    public ExplicitDependency(String parameterName, ResolutionToken<T> token) {
        verifyPropertyName(parameterName);

        this.propertyName = parameterName;
        this.token = token;
    }

    private void verifyPropertyName(String parameterName) {
        if (parameterName == null) {
            throw new IllegalArgumentException("Parameter: parameterName cannot be null.");
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
