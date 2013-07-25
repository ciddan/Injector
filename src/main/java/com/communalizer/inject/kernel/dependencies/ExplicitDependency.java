package com.communalizer.inject.kernel.dependencies;

import com.communalizer.inject.kernel.TypeToken;

public interface ExplicitDependency<T> {
  T getInstance();

  T getFactoryArtifact();

  TypeToken<T> getTypeToken();

  DependencyProviderType getProviderType();

  String getIdentifier();

  String getDependencyComponentName();
}
