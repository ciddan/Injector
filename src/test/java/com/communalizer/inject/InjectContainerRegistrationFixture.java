package com.communalizer.inject;

import com.communalizer.inject.kernel.Component;
import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.Registration;
import com.communalizer.inject.kernel.ResolutionToken;
import org.junit.Test;
import testclasses.*;

import java.util.ArrayList;
import java.util.List;

import static com.communalizer.inject.kernel.RegistrationBuilder.registration;
import static org.fest.assertions.Assertions.assertThat;

public class InjectContainerRegistrationFixture {
    @Test
    public void Register_Component_AddsItToRegistry() {
        // Arrange
        Container container = getNewInjectContainer();

        // Act
        container.register(
            registration()
                .component(new Component<Object, String>(){})
                .build()
        );

        // Assert
        assertThat(container.getRegistry()).isNotNull();
        assertThat(container.getRegistry()).isNotEmpty();
    }

    @Test
    public void Register_Component_AddsItToRegistryWithExpectedKey() {
        // Arrange
        String expectedKey = "java.util.List<java.lang.String>->java.util.ArrayList<java.lang.String>";
        Container container = getNewInjectContainer();

        // Act
        container.register(
            registration()
                .component(new Component<List<String>, ArrayList<String>>() {})
                .build()
        );

        // Assert
        final Registration actual = container.getRegistry().get(expectedKey);
        assertThat(actual).isNotNull();
    }

    @Test
    public void Register_NamedComponent_AddsItToRegistryWithExpectedKey() {
        // Arrange
        String expectedKey = "java.util.List<java.lang.String>->java.util.ArrayList<java.lang.String>-Foo";
        Container container = getNewInjectContainer();

        // Act
        container.register(
            registration()
                .component(new Component<List<String>, ArrayList<String>>() {
                })
                .named("Foo")
                .build()
        );

        // Assert
        assertThat(container.getRegistry().get(expectedKey)).isNotNull();
    }

    @Test
    public void Register_MultipleComponentsOfSameBaseTypeWithoutNames_AddsBothToRegistry() {
        // Arrange
        Container container = getNewInjectContainer();

        // Act
        container.register(
                registration()
                        .component(new Component<Object, String>() {
                        })
                        .build(),
                registration()
                        .component(new Component<Object, Integer>() {
                        })
                        .build()
        );

        // Assert
        assertThat(container.getRegistry().size()).isEqualTo(2);
    }

    @Test(expected = RuntimeException.class)
    public void Register_MultipleComponentsOfSameBaseAndReferencedTypeWithoutName_Throws() {
        // Arrange
        Container container = getNewInjectContainer();

        // Act
        container.register(
                registration()
                        .component(new Component<Object, String>() {
                        })
                        .build(),
                registration()
                        .component(new Component<Object, String>() {
                        })
                        .build()
        );
    }

    @Test
    public void Register_MultipleComponentsOfSameBaseAndReferencedTypeWithDifferingNames_RetainsBothInRegistry() {
        // Arrange
        Container container = getNewInjectContainer();

        // Act
        container.register(
            registration()
                .component(new Component<Object, String>() {})
                .named("foo")
                .build(),
            registration()
                .component(new Component<Object, String>() {})
                .build()
        );

        // Assert
        assertThat(container.getRegistry().size()).isEqualTo(2);
    }

    private static Container getNewInjectContainer() {
        return new InjectContainer();
    }
}
