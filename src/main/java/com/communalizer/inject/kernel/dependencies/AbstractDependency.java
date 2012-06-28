package com.communalizer.inject.kernel.dependencies;

import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.ResolutionToken;

public abstract class AbstractDependency<T> implements ExplicitDependency<T> {
    protected T instance;
    protected Factory<T> factory;
    protected ResolutionToken<T> resolutionToken;

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
        return resolutionToken;
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
