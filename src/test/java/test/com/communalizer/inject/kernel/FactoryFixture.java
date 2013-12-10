package test.com.communalizer.inject.kernel;

import com.communalizer.inject.kernel.Factory;
import org.testng.annotations.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FactoryFixture {
  @Test
  public void Factory_CanBeImplemented_ToCreateAFactoryMethod() {
    // Arrange
    final String anyString = "";

    // Act
    Factory<String> fac = new Factory<String>() {
      @Override
      public String create() {
        return anyString;
      }
    };

    // Assert
    assertThat(fac.create()).isEqualTo(anyString);
  }
}
