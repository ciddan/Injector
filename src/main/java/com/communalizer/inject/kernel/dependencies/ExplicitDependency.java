package com.communalizer.inject.kernel.dependencies;

import com.communalizer.inject.kernel.ResolutionToken;

public interface ExplicitDependency<T> {
    T getInstance();

    T getFactoryArtifact();

    ResolutionToken<T> getResolutionToken();

    DependencyProviderType getProviderType();

    String getIdentifier();
}
