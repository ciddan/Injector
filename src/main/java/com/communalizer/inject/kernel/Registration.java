package com.communalizer.inject.kernel;

import com.communalizer.inject.kernel.dependencies.ExplicitDependency;

import java.util.HashMap;
import java.util.Map;

public class Registration<TBase, TImpl> {
  private Component<TBase, TImpl> component;
  private String name;
  private final Map<String, ExplicitDependency> dependencies = new HashMap<String, ExplicitDependency>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Factory<TBase> getFactory() {
    return this.component.getFactory();
  }

  public void setFactory(Factory<TBase> factory) {
    if (factory != null) {
      this.component.setFactory(factory);
    }
  }

  public TBase getInstance() {
    return component.getExplicitInstance();
  }

  public void setInstance(TBase instance) {
    component.setExplicitInstance(instance);
  }

  public Component<TBase, TImpl> getComponent() {
    return component;
  }

  public Registration(Component<TBase, TImpl> component) {
    if (component == null) {
      throw new IllegalArgumentException("component");
    }

    this.component = component;
  }

  public String getKey() {
    if (this.name == null || this.name.equals("")) {
      return this.component.generateKey();
    } else {
      return String.format("%s-%s", component.generateKey(), this.name);
    }
  }

  public String getResolutionKey() {
    if (this.name == null || this.name.equals("")) {
      return this.component.getBaseTypeToken().getKey();
    } else {
      return
        String.format(
          "%s-%s",
          this.component.getBaseTypeToken().getKey(), this.name
        );
    }
  }

  public <T> void addDependency(ExplicitDependency<T> dependency) {
    this.dependencies.put(dependency.getIdentifier(), dependency);
  }

  @SuppressWarnings("unchecked")
  public ExplicitDependency getDependency(String key) {
    return this.dependencies.get(key);
  }

  public boolean hasExplicitDependencies() {
    return this.dependencies.size() >= 1;
  }
}
