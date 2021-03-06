package test.com.communalizer.inject.kernel;

import com.communalizer.inject.kernel.Component;
import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.Registration;
import com.communalizer.inject.kernel.TypeToken;
import com.communalizer.inject.kernel.dependencies.ExplicitDependency;
import com.communalizer.inject.kernel.dependencies.ParameterDependency;
import com.communalizer.inject.kernel.dependencies.TypeDependency;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.communalizer.inject.kernel.RegistrationBuilder.registration;
import static org.fest.assertions.Assertions.assertThat;

public class RegistrationBuilderFixture {
  @Test
  public void RegistrationBuilder_BuildWithComponent_CanConstructRegistration() {
    // Arrange
    Component<Object, String> component = new Component<Object, String>() {};

    // Act
    Registration reg =
      registration()
        .component(component)
        .build();

    // Assert
    assertThat(reg).isNotNull();
    assertThat(reg.getComponent()).isSameAs(component);
  }

  @Test
  public void RegistrationBuilder_RawTypeUsage_StillPreservesInnerComponentTypeInformation() {
    // Arrange
    final String expectedKey = "java.util.List<java.lang.String>->java.util.ArrayList<java.lang.String>";

    // Act
    Registration reg =
      registration()
        .component(new Component<List<String>, ArrayList<String>>() {})
        .build();

    // Assert
    assertThat(reg.getKey()).isEqualTo(expectedKey);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void RegistrationBuilder_BuildWithComponentAndFactory_DoesTypeCheckingBetweenFactoryAndComponent() {
    // Arrange
    Component<Object, String> component = new Component<Object, String>() {};
    Factory<Object> factory = new Factory<Object>() {
      @Override
      public Integer create() {
        return 1;
      }
    };

    // Act
    registration()
      .component(component)
      .factory(factory)
      .build();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void RegistrationBuilder_BuildWithComponentAndFactory_DoesTypeCheckingBetweenInstanceAndComponent() {
    // Arrange
    Component<Object, String> component = new Component<Object, String>() {};
    Integer instance = 1;

    // Act
    registration()
      .component(component)
      .instance(instance)
      .build();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void RegistrationBuilder_BuildWithComponentAndFactory_DoesGenericTypeCheckingBetweenFactoryAndComponent() {
    // Arrange
    Component<List<Object>, ArrayList<String>> component = new Component<List<Object>, ArrayList<String>>() {};
    Factory<List<Integer>> factory = new Factory<List<Integer>>() {
      @Override
      public List<Integer> create() {
        return new ArrayList<Integer>();
      }
    };

    // Act
    registration()
      .component(component)
      .factory(factory)
      .build();
  }

  @Test
  public void RegistrationBuilder_CanSetName() {
    // Arrange
    Component<Object, String> component = new Component<Object, String>() {};
    String anyName = "ANY_NAME";

    Registration reg =
      registration()
        .component(component)
        .named(anyName)
        .build();

    // Act
    String actual = reg.getName();

    // Assert
    assertThat(actual).isEqualTo(anyName);
  }

  @Test
  public void RegistrationBuilder_CanAddParameterDependencyWithInstance_AndThenRetrieveItFromTheBuiltRegistration() {
    // Arrange
    final String anyInstance = "value";

    Component<Object, String> component = new Component<Object, String>() {};

    // Act
    Registration reg =
      registration()
        .component(component)
        .dependsOn("parameterName", anyInstance)
        .build();

    // Assert
    ExplicitDependency dep = reg.getDependency("parameterName");

    assertThat(dep).isNotNull();

    assertThat(dep instanceof ParameterDependency);
    assertThat(dep.getInstance()).isSameAs(anyInstance);
  }

  @Test
  public void RegistrationBuilder_CanAddTypeDependencyWithInstance_AndThenRetrieveItFromTheBuiltRegistration() {
    // Arrange
    final String anyInstance = "value";

    TypeToken<String> tt = new TypeToken<String>() {};
    Component<Object, String> component = new Component<Object, String>() {};

    // Act
    Registration reg =
      registration()
        .component(component)
        .dependsOn(tt, anyInstance)
        .build();

    // Assert
    ExplicitDependency dep = reg.getDependency(tt.getKey());

    assertThat(dep).isNotNull();

    assertThat(dep instanceof TypeDependency);
    assertThat(dep.getInstance()).isSameAs(anyInstance);
  }


}
