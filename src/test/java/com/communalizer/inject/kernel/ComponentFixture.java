package com.communalizer.inject.kernel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class ComponentFixture {
  @Test(expected = IllegalArgumentException.class)
  public void Component_Creation_RequiresGenericTypeInformation() {
    // Act
    new Component() {};
  }

  @Test(expected = IllegalArgumentException.class)
  public void Component_WithIncompatibleTypes_Throws() {
    // Act
    new Component<String, Integer>() {};
  }

  @Test(expected = IllegalArgumentException.class)
  public void Component_WithIncompatibleWrappedTypes_Throws() {
    // Act
    new Component<List<String>, ArrayList<Integer>>() {};
  }

  @Test(expected = IllegalArgumentException.class)
  public void Component_WithCompatibleBaseTypesAndIncompatibleWrappedTypes_Throws() {
    // Act
    new Component<List<String>, ArrayList<Integer>>() {};
  }

  @Test
  public void Component_CreatedWithValidTypeParameters_SavesThem() {
    // Act
    Component<List, ArrayList> component = new Component<List, ArrayList>() {};

    // Assert
    assertThat(component.getBaseType()).isEqualTo(List.class);
    assertThat(component.getReferencedType()).isEqualTo(ArrayList.class);
  }

  @Test
  public void Component_CanBeCreated_ContainingAFactory() {
    // Arrange
    Factory<List> factory = new Factory<List>() {
      @Override
      public ArrayList create() {
        return new ArrayList();
      }
    };

    // Act
    Component<List, ArrayList> component = new Component<List, ArrayList>(factory) {};

    // Assert
    assertThat(component.getFactory()).isNotNull();
    assertThat(component.getFactory()).isSameAs(factory);
  }

  @Test
  public void Component_CanBeCreated_ContainingAnInstance() {
    // Arrange
    List<String> instance = new ArrayList<String>();

    // Act
    Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>(instance) {};

    // Assert
    assertThat(component.getExplicitInstance()).isNotNull();
    assertThat(component.getExplicitInstance()).isSameAs(instance);
  }

  @Test(expected = IllegalArgumentException.class)
  public void Component_CreatedWithFactoryNotCreatingCompatibleTypes_Throws() {
    // Arrange
    Factory<Object> factory = new Factory<Object>() {
      @Override
      public Integer create() {
        return 1;
      }
    };

    // Act
    new Component<Object, String>(factory){};
  }

  @Test(expected = IllegalArgumentException.class)
  public void Component_SettingFactoryAfterCreation_StillChecksFactoryType() {
    // Arrange
    Factory<Object> factory = new Factory<Object>() {
      @Override
      public Integer create() {
        return 1;
      }
    };

    Component<Object, String> component = new Component<Object, String>(){};

    // Act
    component.setFactory(factory);
  }

  @Test
  public void Component_CanGetInstance_FromFactoryCreateFunction() {
    // Arrange
    Factory<List> factory = new Factory<List>() {
      @Override
      public ArrayList create() {
        return new ArrayList();
      }
    };

    Component<List, ArrayList> component = new Component<List, ArrayList>(factory) {};

    // Act
    List actual = component.getOrCreateInstance();

    // Assert
    assertThat(actual).isNotNull();
    assertThat(actual instanceof ArrayList);
  }

  @Test
  public void Component_CanGetInstance_FromExplicitInstance() {
    // Arrange
    List<String> instance = new ArrayList<String>();
    Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>(instance) {};

    // Act
    List<String> actual = component.getOrCreateInstance();

    // Assert
    assertThat(actual).isNotNull();
    assertThat(actual).isSameAs(instance);
  }

  @Test
  public void Component_GetOrCreateInstanceWithNoFactoryOrInstanceSet_ReturnsNull() {
    // Arrange
    Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>() {};

    // Act
    List<String> actual = component.getOrCreateInstance();

    // Assert
    assertThat(actual).isNull();
  }

  @Test
  public void Component_GenerateKey_ReturnsKeyRepresentingBaseAndReferencedType() {
    // Arrange
    String expectedKey = "java.util.List<java.lang.String>->java.util.ArrayList<java.lang.String>";
    Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>() {};

    // Act
    String actual = component.generateKey();

    // Assert
    assertThat(actual).isEqualTo(expectedKey);
  }

  @Test
  public void ComponentType_WithNoInstanceOrFactorySet_ReportsREFLECTION() {
    // Arrange
    Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>() {};

    // Act
    ComponentType type = component.getComponentType();

    // Assert
    assertThat(type).isEqualTo(ComponentType.REFLECTION);
  }

  @Test
  public void ComponentType_WithInstanceSet_ReportsINSTANCE() {
    // Arrange
    List<String> list = new ArrayList<String>();
    Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>(list) {};

    // Act
    ComponentType type = component.getComponentType();

    // Assert
    assertThat(type).isEqualTo(ComponentType.INSTANCE);
  }

  @Test
  public void ComponentType_WithFactorySet_ReportsFACTORY() {
    // Arrange
    Factory<List<String>> factory = new Factory<List<String>>() {
      @Override
      public List<String> create() {
        return new ArrayList<String>();
      }
    };

    Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>(factory) {};

    // Act
    ComponentType type = component.getComponentType();

    // Assert
    assertThat(type).isEqualTo(ComponentType.FACTORY);
  }

  @Test
  public void Component_GetBaseTypeToken_ReturnsTypeTokenRepresentingTBase() {
    // Arrange
    String expectedKey = "java.util.List<java.lang.String>";
    Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>() {};

    // Act
    TypeToken<List<String>> token = component.getBaseTypeToken();

    // Assert
    assertThat(token).isNotNull();
    assertThat(token.getKey()).isEqualTo(expectedKey);
  }

  @Test
  public void Component_GetReferencedTypeToken_ReturnsTypeTokenRepresentingTImpl() {
    // Arrange
    String expectedKey = "java.util.ArrayList<java.lang.String>";
    Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>() {};

    // Act
    TypeToken<ArrayList<String>> token = component.getReferencedTypeToken();

    // Assert
    assertThat(token).isNotNull();
    assertThat(token.getKey()).isEqualTo(expectedKey);
  }
}
