package test.com.communalizer.inject.kernel.dependencies;

import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.dependencies.ParameterDependency;
import org.testng.annotations.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ParameterDependencyFixture {
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void ParameterDependency_WithNullPropertyName_Throws() {
    new ParameterDependency<Class>(null, String.class);
  }

  @Test
  public void ParameterDependency_WithInstance_ReturnsInstanceWhenRequestingInstance() {
    // Arrange
    ParameterDependency<Class> dependency = new ParameterDependency<Class>("foo", String.class);

    // Act
    Class actual = dependency.getInstance();

    // Assert
    assertThat(actual).isNotNull();
    assertThat(actual).isEqualTo(String.class);
  }


  @Test
  public void ParameterDependency_WithFactory_ReturnsInstanceCreatedByFactoryWhenRequestingInstance() {
    // Arrange
    final String anyString = "ANY_STRING";

    Factory<String> factory = new Factory<String>() {
      @Override
      public String create() {
        return anyString;
      }
    };
    ParameterDependency<String> dependency = new ParameterDependency<String>("foo", factory);

    // Act
    String actual = dependency.getFactoryArtifact();

    // Assert
    assertThat(actual).isNotNull();
    assertThat(actual).isSameAs(anyString);
  }

  @Test
  public void ParameterDependency_GetIdentifier_ReturnsParameterName() {
    // Arrange
    String expected = "foo";
    ParameterDependency<Class> dependency = new ParameterDependency<Class>("foo", String.class);

    // Act
    String actual = dependency.getIdentifier();

    // Assert
    assertThat(actual).isEqualTo(expected);
  }
}
