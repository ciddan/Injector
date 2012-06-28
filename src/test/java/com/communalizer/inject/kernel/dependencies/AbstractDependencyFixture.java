package com.communalizer.inject.kernel.dependencies;

import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.ResolutionToken;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class AbstractDependencyFixture {
    @Test
    public void AbstractDependency_WithInstanceSet_ReportsINSTANCE() {
        // Arrange
        ParameterDependency<String> ed = new ParameterDependency<String>("s", "s");

        // Act
        DependencyProviderType edt = ed.getProviderType();

        // Assert
        assertThat(edt).isEqualTo(DependencyProviderType.INSTANCE);
    }

    @Test
    public void AbstractDependency_WithFactorySet_ReportsFACTORY() {
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
    public void AbstractDependency_WithResolutionTokenSet_ReportsRESOLUTION_TOKEN() {
        // Arrange
        ResolutionToken<String> rt = new ResolutionToken<String>() {};
        ParameterDependency<String> ed = new ParameterDependency<String>("s", rt);

        // Act
        DependencyProviderType edt = ed.getProviderType();

        // Assert
        assertThat(edt).isEqualTo(DependencyProviderType.RESOLUTION_TOKEN);
    }
}
