package com.communalizer.inject.kernel.dependencies;

import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.TypeToken;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class AbstractDependencyFixture {
  @Test
  public void AbstractDependency_WithInstanceSet_Reports_INSTANCE() {
    // Arrange
    ParameterDependency<String> ed = new ParameterDependency<String>("s", "s");

    // Act
    DependencyProviderType edt = ed.getProviderType();

    // Assert
    assertThat(edt).isEqualTo(DependencyProviderType.INSTANCE);
  }

  @Test
  public void AbstractDependency_WithFactorySet_Reports_FACTORY() {
    // Arrange
    Factory<String> factory = new Factory<String>() {
      @Override
      public String create() {
        return "s";
      }
    };
    ParameterDependency<String> ed = new ParameterDependency<String>("s", factory);

    // Act
    DependencyProviderType edt = ed.getProviderType();

    // Assert
    assertThat(edt).isEqualTo(DependencyProviderType.FACTORY);
  }

  @Test
  public void AbstractDependency_WithTypeTokenSet_Reports_TYPE_TOKEN() {
    // Arrange
    TypeToken<String> tt = new TypeToken<String>() {};
    ParameterDependency<String> ed = new ParameterDependency<String>("s", tt);

    // Act
    DependencyProviderType edt = ed.getProviderType();

    // Assert
    assertThat(edt).isEqualTo(DependencyProviderType.TYPE_TOKEN);
  }
}
