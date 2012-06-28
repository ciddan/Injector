package com.communalizer.inject.kernel;

public interface ExplicitDependency<T> {
    T getInstance();

    T getFactoryArtifact();

    ResolutionToken<T> getResolutionToken();

    DependencyProviderType getProviderType();

    String getIdentifier();
}
