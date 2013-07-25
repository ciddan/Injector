package com.communalizer.inject.kernel.dependencies;


import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.TypeToken;

public class ParameterDependency<T> extends AbstractDependency<T> {
  private String propertyName = "";

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

  public ParameterDependency(String parameterName, TypeToken<T> token) {
    verifyPropertyName(parameterName);

    this.propertyName = parameterName;
    this.typeToken = token;
  }

  public ParameterDependency(String parameterName, TypeToken<T> token, String explicitComponentName) {
    verifyPropertyName(parameterName);

    this.propertyName = parameterName;
    this.typeToken = token;
    this.componentName = explicitComponentName;
  }

  private void verifyPropertyName(String parameterName) {
    if (parameterName == null) {
      throw new IllegalArgumentException("Parameter: parameterName cannot be null.");
    }
  }
}
