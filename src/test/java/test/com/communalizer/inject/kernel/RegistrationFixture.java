package test.com.communalizer.inject.kernel;

import com.communalizer.inject.kernel.Component;
import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.Registration;
import com.communalizer.inject.kernel.dependencies.ParameterDependency;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class RegistrationFixture {
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void Registration_ConstructedWithNullComponent_Throws() {
    // Act
    new Registration<Object, String>(null);
  }

  @Test
  public void Registration_CanBeCreatedWith_Component() {
    // Arrange
    Component<Object, String> component = new Component<Object, String>() {};

    // Act
    Registration<Object, String> registration = new Registration<Object, String>(component);

    // Assert
    assertThat(registration.getComponent()).isNotNull();
    assertThat(registration.getComponent()).isSameAs(component);
  }

  @Test
  public void Registration_CanSet_Factory() {
    // Arrange
    Component<Object, String> component = new Component<Object, String>() {};
    Factory<Object> fac = new Factory<Object>() {
      @Override
      public Object create() {
        return "";
      }
    };

    Registration<Object, String> registration = new Registration<Object, String>(component);

    // Act
    registration.setFactory(fac);

    // Assert
    assertThat(registration.getComponent()).isNotNull();
    assertThat(registration.getComponent()).isSameAs(component);

    assertThat(registration.getFactory()).isNotNull();
    assertThat(registration.getFactory()).isSameAs(fac);
  }

  @Test
  public void Registration_CanSet_Instance() {
    // Arrange
    String instance = "s";
    Component<Object, String> component = new Component<Object, String>() {};

    Registration<Object, String> registration = new Registration<Object, String>(component);

    // Act
    registration.setInstance(instance);

    // Assert
    assertThat(registration.getComponent()).isNotNull();
    assertThat(registration.getComponent()).isSameAs(component);

    assertThat(registration.getInstance()).isNotNull();
    assertThat(registration.getInstance()).isSameAs(instance);
  }

  @Test
  public void Registration_CanSet_Name() {
    // Arrange
    String name = "ANY_NAME";
    Component<Object, String> component = new Component<Object, String>() {};

    Registration<Object, String> registration = new Registration<Object, String>(component);

    // Act
    registration.setName(name);

    // Assert
    assertThat(registration.getComponent()).isNotNull();
    assertThat(registration.getComponent()).isSameAs(component);

    assertThat(registration.getName()).isNotNull();
    assertThat(registration.getName()).isSameAs(name);
  }

  @Test
  public void Registration_GenerateKeyWithoutName_ReturnsTypeKey() {
    // Arrange
    String expectedKey = "java.lang.Object->java.lang.String";
    Component<Object, String> component = new Component<Object, String>() {};

    Registration<Object, String> registration = new Registration<Object, String>(component);

    // Act
    String actual = registration.getKey();

    // Assert
    assertThat(actual).isEqualTo(expectedKey);
  }

  @Test
  public void Registration_GenerateKeyWithEmptyName_ReturnsTypeKey() {
    // Arrange
    String expectedKey = "java.lang.Object->java.lang.String";
    Component<Object, String> component = new Component<Object, String>() {};

    Registration<Object, String> registration = new Registration<Object, String>(component);
    registration.setName("");

    // Act
    String actual = registration.getKey();

    // Assert
    assertThat(actual).isEqualTo(expectedKey);
  }

  @Test
  public void Registration_GenerateKeyWithName_ReturnsCompoundTypeNameKey() {
    // Arrange
    String expectedKey = "java.util.List<java.lang.Object>->java.util.ArrayList<java.lang.String>-Foo";
    Component<List<Object>, ArrayList<String>> component = new Component<List<Object>, ArrayList<String>>() {};

    Registration<List<Object>, ArrayList<String>> registration = new Registration<List<Object>, ArrayList<String>>(component);
    registration.setName("Foo");

    // Act
    String actual = registration.getKey();

    // Assert
    assertThat(actual).isEqualTo(expectedKey);
  }

  @Test
  public void Registration_CanAddParameterDependencyToInternalCache_AndRetrieveItByName() {
    // Arrange
    Component<List<Object>, ArrayList<String>> component = new Component<List<Object>, ArrayList<String>>() {};
    Registration<List<Object>, ArrayList<String>> registration = new Registration<List<Object>, ArrayList<String>>(component);

    final String anyExplicitValue = "AnyExplicitValue";
    final String anyPropertyName = "AnyPropertyName";

    final ParameterDependency<String> parameterDependency = new ParameterDependency<String>(anyPropertyName, anyExplicitValue);

    // Act
    registration.addDependency(parameterDependency);

    // Assert
    assertThat(registration.getDependency(anyPropertyName)).isNotNull();
    assertThat(registration.getDependency(anyPropertyName)).isSameAs(parameterDependency);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void Registration_WithIncompatibleComponentFactoryRawTypes_Throws() {
    // Arrange
    Component<Object, String> component = new Component<Object, String>() {};
    Factory<Object> fac = new Factory<Object>() {
      @Override
      public Integer create() {
        return 1;
      }
    };

    // Act
    Registration<Object, String> registration = new Registration<Object, String>(component);
    registration.setFactory(fac);
  }
}
