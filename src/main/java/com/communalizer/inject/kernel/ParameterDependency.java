package com.communalizer.inject.kernel;


public class ParameterDependency<T> implements ExplicitDependency<T> {
    private final String propertyName;

    private T instance;
    private Factory<T> factory;
    private ResolutionToken<T> token;

    public String getIdentifier() {
        return propertyName;
    }

    public ParameterDependency(String propertyName, T dependency) {
        verifyPropertyName(propertyName);

        this.propertyName = propertyName;
        this.instance = dependency;
    }

    public ParameterDependency(String parameterName, Factory<T> factory) {
        verifyPropertyName(parameterName);

        this.propertyName = parameterName;
        this.factory = factory;
    }

    public ParameterDependency(String parameterName, ResolutionToken<T> token) {
        verifyPropertyName(parameterName);

        this.propertyName = parameterName;
        this.token = token;
    }

    private void verifyPropertyName(String parameterName) {
        if (parameterName == null) {
            throw new IllegalArgumentException("Parameter: parameterName cannot be null.");
        }
    }

    @Override
    public T getInstance() {
        return instance;
    }

    @Override
    public T getFactoryArtifact() {
        return factory.create();
    }

    @Override
    public ResolutionToken<T> getResolutionToken() {
        return token;
    }

    @Override
    public DependencyProviderType getProviderType() {
        if (instance != null) {
            return DependencyProviderType.INSTANCE;
        }

        if (factory != null) {
            return DependencyProviderType.FACTORY;
        }

        return DependencyProviderType.RESOLUTION_TOKEN;
    }
}
