package com.communalizer.inject.kernel;

import com.communalizer.inject.kernel.dependencies.ExplicitDependency;
import com.communalizer.inject.kernel.dependencies.ParameterDependency;
import com.communalizer.inject.kernel.dependencies.TypeDependency;

import java.util.ArrayList;
import java.util.List;

public class RegistrationBuilder {
  private List<ExplicitDependency> dependencies = new ArrayList<ExplicitDependency>();
  private Component component;
  private Factory factory;
  private Object instance;
  private String name;

  public static RegistrationBuilder registration() {
    return new RegistrationBuilder();
  }

  public RegistrationBuilder component(Component component) {
    this.component = component;

    return this;
  }

  public RegistrationBuilder factory(Factory factory) {
    this.factory = factory;

    return this;
  }

  public RegistrationBuilder instance(Object instance) {
    this.instance = instance;

    return this;
  }

  public RegistrationBuilder named(String name) {
    this.name = name;

    return this;
  }

  public <T> RegistrationBuilder dependsOn(String parameterName, T instance) {
    this.dependencies.add(new ParameterDependency<T>(parameterName, instance));

    return this;
  }

  public <T> RegistrationBuilder dependsOn(String parameterName, Factory<T> factory) {
    this.dependencies.add(new ParameterDependency<T>(parameterName, factory));

    return this;
  }

  public <T> RegistrationBuilder dependsOn(String parameterName, TypeToken<T> token) {
    this.dependencies.add(new ParameterDependency<T>(parameterName, token));

    return this;
  }

  public <T> RegistrationBuilder dependsOn(String parameterName, TypeToken<T> token, String componentName) {
    this.dependencies.add(new ParameterDependency<T>(parameterName, token, componentName));

    return this;
  }

  public <T> RegistrationBuilder dependsOn(TypeToken<T> typeToken, T instance) {
    this.dependencies.add(new TypeDependency<T>(typeToken,  instance));

    return this;
  }

  public <T> RegistrationBuilder dependsOn(TypeToken<T> typeToken, Factory<T> factory) {
    this.dependencies.add(new TypeDependency<T>(typeToken,  factory));

    return this;
  }

  @SuppressWarnings("unchecked")
  public Registration build() {
    Registration registration = new Registration(component);

    registration.setName(name);
    registration.setFactory(factory);
    registration.setInstance(instance);

    for (ExplicitDependency dependency : dependencies) {
      registration.addDependency(dependency);
    }

    return registration;
  }
}
