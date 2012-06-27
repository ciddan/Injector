package com.communalizer.inject.kernel;

public class Registration<TBase, TImpl> {
    private Component<TBase, TImpl> component;
    private String name;

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
}