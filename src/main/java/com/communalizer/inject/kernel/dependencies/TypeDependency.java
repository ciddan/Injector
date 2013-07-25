package com.communalizer.inject.kernel.dependencies;

import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.TypeToken;

public class TypeDependency<T> extends AbstractDependency<T> {
  private final TypeToken<T> typeToken;

  public TypeDependency(TypeToken<T> token, T instance) {
    verifyToken(token);

    this.typeToken = token;
    this.instance = instance;
  }

  public TypeDependency(TypeToken<T> token, Factory<T> factory) {
    verifyToken(token);

    this.typeToken = token;
    this.factory = factory;
  }

  public TypeDependency(TypeToken<T> token) {
    verifyToken(token);

    this.typeToken = token;
  }

  private void verifyToken(TypeToken<T> token) {
    if (token == null) {
      throw new IllegalArgumentException("Parameter: resolutionToken cannot be null.");
    }
  }

  @Override
  public String getIdentifier() {
    return typeToken.getKey();
  }
}
