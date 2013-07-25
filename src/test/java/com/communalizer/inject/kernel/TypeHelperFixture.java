package com.communalizer.inject.kernel;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeHelperFixture {
  @Test(expected = IllegalArgumentException.class)
  public void TypeHelper_CheckClassCompatibilityWithIncompatibleClasses_Throws() {
    // Act
    TypeHelper.checkClassCompatibility(String.class, Integer.class);
  }

  @Test(expected = RuntimeException.class)
  public void TypeHelper_CheckGenericWithTypeParameterCountMismatch_Throws() {
    // Arrange
    Component<List<String>, Map<String, Integer>> foo = new Component<List<String>, Map<String, Integer>>() {};

    Type t1 = ((ParameterizedType) foo.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    Type t2 = ((ParameterizedType) foo.getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    // Act
    TypeHelper.checkGenericTypeCompatibility((ParameterizedType) t1, (ParameterizedType) t2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void TypeHelper_CheckGenericWithIncompatibleInnerTypes_Throws() {
    // Arrange
    Component<List<String>, ArrayList<Integer>> foo = new Component<List<String>, ArrayList<Integer>>() {};

    Type t1 = ((ParameterizedType) foo.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    Type t2 = ((ParameterizedType) foo.getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    // Act
    TypeHelper.checkGenericTypeCompatibility((ParameterizedType) t1, (ParameterizedType) t2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void TypeHelper_CheckGenericWithIncompatibleRawTypes_Throws() {
    // Arrange
    Component<List<String>, Class<String>> foo = new Component<List<String>, Class<String>>() {};

    Type t1 = ((ParameterizedType) foo.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    Type t2 = ((ParameterizedType) foo.getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    // Act
    TypeHelper.checkGenericTypeCompatibility((ParameterizedType) t1, (ParameterizedType) t2);
  }
}
