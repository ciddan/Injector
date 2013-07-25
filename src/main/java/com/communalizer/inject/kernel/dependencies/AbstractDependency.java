package com.communalizer.inject.kernel.dependencies;

import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.TypeToken;

public abstract class AbstractDependency<T> implements ExplicitDependency<T> {
  protected T instance;
  protected Factory<T> factory;
  protected TypeToken<T> typeToken;
  protected String componentName;

  @Override
  public T getInstance() {
    return instance;
  }

  @Override
  public T getFactoryArtifact() {
    return factory.create();
  }

  @Override
  public TypeToken<T> getTypeToken() {
    return typeToken;
  }

  @Override
  public String getDependencyComponentName() {
    return componentName;
  }

  @Override
  public DependencyProviderType getProviderType() {
    if (instance != null) {
      return DependencyProviderType.INSTANCE;
    }

    if (factory != null) {
      return DependencyProviderType.FACTORY;
    }

    return DependencyProviderType.TYPE_TOKEN;
  }
}
