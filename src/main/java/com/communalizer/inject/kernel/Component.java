package com.communalizer.inject.kernel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public abstract class Component<TBase, TImpl> {
  private Type baseType;
  private Type referencedType;
  private Factory<TBase> factory;
  private TBase explicitInstance;

  private final List<Class> wrappedReferencedTypes = new ArrayList<Class>();

  public Type getBaseType() {
    return baseType;
  }

  public Type getReferencedType() {
    return referencedType;
  }

  public Factory<TBase> getFactory() {
    return this.factory;
  }

  public void setFactory(Factory<TBase> factory) {
    if (factory != null) {
      analyzeFactory(factory);

      this.factory = factory;
    }
  }

  public TBase getExplicitInstance() {
    return this.explicitInstance;
  }

  public void setExplicitInstance(TBase instance) {
    if (instance != null) {
      analyzeInstance(instance);

      this.explicitInstance = instance;
    }
  }

  public ComponentType getComponentType() {
    if (this.explicitInstance != null) {
      return ComponentType.INSTANCE;
    }

    if (this.factory != null) {
      return ComponentType.FACTORY;
    }

    return ComponentType.REFLECTION;
  }


  public Component() {
    extractTypes();
  }

  public Component(Factory<TBase> factory) {
    extractTypes();
    setFactory(factory);
  }

  public Component(TBase instance) {
    extractTypes();
    setExplicitInstance(instance);
  }

  private void extractTypes() {
    this.baseType = extractType(0);
    this.referencedType = extractType(1);

    if (this.baseType instanceof ParameterizedType) {
      checkGenericTypes();
    } else {
      TypeHelper.checkClassCompatibility((Class) this.baseType, (Class) this.referencedType);
    }
  }

  private void analyzeFactory(Factory<TBase> factory) {
    if (referencedType instanceof ParameterizedType) {
      Type t = factory.getClass().getMethods()[0].getGenericReturnType();

      ParameterizedType factoryReturnType = (ParameterizedType) t;
      ParameterizedType referencedType = (ParameterizedType) this.referencedType;

      TypeHelper.checkGenericTypeCompatibility(factoryReturnType, referencedType);
    } else {
      TypeHelper.checkClassCompatibility(
        factory.getClass().getMethods()[0].getReturnType(),
        (Class) this.referencedType
      );
    }
  }

  private void analyzeInstance(TBase instance) {
    // If passing an instance of a generic type, the only type-checking possible is that of
    // the raw types, since all generic type information of the instance has been erased.
    if (referencedType instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) referencedType;

      TypeHelper.checkClassCompatibility(
        instance.getClass(),
        (Class) pt.getRawType()
      );
    } else {
      TypeHelper.checkClassCompatibility(
        instance.getClass(),
        (Class) this.referencedType
      );
    }
  }

  public TBase getOrCreateInstance() {
    if (factory != null) {
      return factory.create();
    }

    if (explicitInstance != null) {
      return this.explicitInstance;
    }

    return null;
  }

  private Type extractType(int pos) {
    Type t = getClass().getGenericSuperclass();
    if (!(t instanceof ParameterizedType)) {
      throw new IllegalArgumentException("Type parameters: TBase and TImpl must be specified.");
    }

    ParameterizedType pt = (ParameterizedType) t;

    return pt.getActualTypeArguments()[pos];
  }

  private void checkGenericTypes() {
    ParameterizedType baseGenericTypes = (ParameterizedType) this.baseType;
    ParameterizedType referencedGenericTypes = (ParameterizedType) this.referencedType;

    TypeHelper.checkGenericTypeCompatibility(baseGenericTypes, referencedGenericTypes);
  }

  public String generateKey() {
    String baseKey = this.baseType.toString().replace("class ", "").replace("interface ", "");
    String refKey = this.referencedType.toString().replace("class ", "").replace("interface ", "");

    return String.format("%s->%s", baseKey, refKey);
  }

  public TypeToken<TBase> getBaseTypeToken() {
    return TypeToken.getToken(this.baseType);
  }

  public TypeToken<TImpl> getReferencedTypeToken() {
    return TypeToken.getToken(this.referencedType);
  }
}
