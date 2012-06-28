package com.communalizer.inject.kernel;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ExplicitDependencyFixture {
    @Test(expected = IllegalArgumentException.class)
    public void ExplicitDependency_WithNullPropertyName_Throws() {
        new ExplicitDependency<Class>(null, String.class);
    }

    @Test
    public void ExplicitDependency_WithClass_ReturnsClassWhenRequestingInstance() {
        // Arrange
        ExplicitDependency<Class> dependency = new ExplicitDependency<Class>("foo", String.class);

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
        ExplicitDependency<String> dependency = new ExplicitDependency<String>("foo", factory);

        // Act
        String actual = dependency.getFactoryArtefact();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("ANY_STRING");
    }

    @Test
    public void ExplicitDependency_WithInstanceSet_ReportsINSTANCE() {
        // Arrange
        ExplicitDependency<String> ed = new ExplicitDependency<String>("s", "s");

        // Act
        ExplicitDependencyType edt = ed.getProviderType();

        // Assert
        assertThat(edt).isEqualTo(ExplicitDependencyType.INSTANCE);
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
        ExplicitDependency<String> ed = new ExplicitDependency<String>("s", factory);

        // Act
        ExplicitDependencyType edt = ed.getProviderType();

        // Assert
        assertThat(edt).isEqualTo(ExplicitDependencyType.FACTORY);
    }

    @Test
    public void ExplicitDependency_WithResolutionTokenSet_ReportsRESOLUTION_TOKEN() {
        // Arrange
        ResolutionToken<String> rt = new ResolutionToken<String>() {};
        ExplicitDependency<String> ed = new ExplicitDependency<String>("s", rt);

        // Act
        ExplicitDependencyType edt = ed.getProviderType();

        // Assert
        assertThat(edt).isEqualTo(ExplicitDependencyType.RESOLUTION_TOKEN);
    }
}
