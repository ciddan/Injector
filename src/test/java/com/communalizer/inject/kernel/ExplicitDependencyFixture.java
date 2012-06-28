package com.communalizer.inject.kernel;

import com.communalizer.inject.kernel.dependencies.DependencyProviderType;
import com.communalizer.inject.kernel.dependencies.ParameterDependency;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ExplicitDependencyFixture {
    @Test(expected = IllegalArgumentException.class)
    public void ExplicitDependency_WithNullPropertyName_Throws() {
        new ParameterDependency<Class>(null, String.class);
    }

    @Test
    public void ExplicitDependency_WithClass_ReturnsClassWhenRequestingInstance() {
        // Arrange
        ParameterDependency<Class> dependency = new ParameterDependency<Class>("foo", String.class);

        // Act
        Class actual = dependency.getInstance();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(String.class);
    }


    @Test
    public void ExplicitDependency_WithFactory_ReturnsInstanceCreatedByFactoryWhenRequestingInstance() {
        // Arrange
        Factory<String> factory = new Factory<String>() {
            @Override
            public String create() {
                return "ANY_STRING";
            }
        };
        ParameterDependency<String> dependency = new ParameterDependency<String>("foo", factory);

        // Act
        String actual = dependency.getFactoryArtifact();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("ANY_STRING");
    }

    @Test
    public void ExplicitDependency_WithInstanceSet_ReportsINSTANCE() {
        // Arrange
        ParameterDependency<String> ed = new ParameterDependency<String>("s", "s");

        // Act
        DependencyProviderType edt = ed.getProviderType();

        // Assert
        assertThat(edt).isEqualTo(DependencyProviderType.INSTANCE);
    }

    @Test
    public void ExplicitDependency_WithFactorySet_ReportsFACTORY() {
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
    public void ExplicitDependency_WithResolutionTokenSet_ReportsRESOLUTION_TOKEN() {
        // Arrange
        ResolutionToken<String> rt = new ResolutionToken<String>() {};
        ParameterDependency<String> ed = new ParameterDependency<String>("s", rt);

        // Act
        DependencyProviderType edt = ed.getProviderType();

        // Assert
        assertThat(edt).isEqualTo(DependencyProviderType.RESOLUTION_TOKEN);
    }
}
