package com.communalizer.inject;

import com.communalizer.inject.kernel.Component;
import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.ResolutionToken;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.communalizer.inject.kernel.RegistrationBuilder.registration;
import static org.fest.assertions.Assertions.assertThat;

public class InjectContainerFixture {
    @Test
    public void Register_Component_AddsItToRegistry() {
        // Arrange
        Container container = new InjectContainer();

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
        String expectedKey = "java.util.List<java.lang.String>";
        Container container = new InjectContainer();

        // Act
        container.register(
            registration()
                .component(new Component<List<String>, ArrayList<String>>() {})
                .build()
        );

        // Assert
        assertThat(container.getRegistry().get(expectedKey)).isNotNull();
    }

    @Test
    public void Register_NamedComponent_AddsItToRegistryWithExpectedKey() {
        // Arrange
        String expectedKey = "java.util.List<java.lang.String>-Foo";
        Container container = new InjectContainer();

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

    @Test(expected = RuntimeException.class)
    public void Register_MultipleComponentsOfSameBaseTypeWithoutNames_Throws() {
        // Arrange
        Container container = new InjectContainer();

        // Act
        container.register(
            registration()
                .component(new Component<Object, String>() {})
                .build(),
            registration()
                .component(new Component<Object, Integer>() {})
                .build()
        );
    }

    @Test
    public void Register_MultipleComponentsOfSameBaseTypeWithDifferingNames_RetainsBothInRegistry() {
        // Arrange
        Container container = new InjectContainer();

        // Act
        container.register(
            registration()
                .component(new Component<Object, String>() {})
                .named("foo")
                .build(),
            registration()
                .component(new Component<Object, Integer>() {})
                .named("bar")
                .build()
        );

        // Assert
        assertThat(container.getRegistry().size()).isEqualTo(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void Resolve_WithNullResolutionToken_Throws() {
        // Arrange
        Container container = new InjectContainer();

        // Act
        container.resolve(null);
    }

    @Test(expected = RuntimeException.class)
    public void Resolve_TypeThatIsNotRegistered_Throws() {
        // Arrange
        Container container = new InjectContainer();

        // Act
        container.resolve(new ResolutionToken<List<String>>() {});
    }

    @Test
    public void Resolve_TypeThatHasBeenRegisteredWithInstance_ReturnsTheRegisteredInstance() {
        // Arrange
        List<String> instance = new ArrayList<String>();
        Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>() {};

        Container container = new InjectContainer();
        container.register(
            registration()
                .component(component)
                .instance(instance)
                .build()
        );

        // Act
        List<String> actual = container.resolve(new ResolutionToken<List<String>>() {});

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isSameAs(instance);
    }

    @Test
    public void Resolve_TypeThatHasBeenRegisteredWithFactory_UsesFactoryToCreateInstance() {
        // Arrange
        Factory<List<String>> fac = new Factory<List<String>>() {
            @Override
            public List<String> create() {
                return new ArrayList<String>();
            }
        };

        Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>() {};

        Container container = new InjectContainer();
        container.register(
            registration()
                .component(component)
                .factory(fac)
                .build()
        );

        // Act
        List<String> actual1 = container.resolve(new ResolutionToken<List<String>>() {});
        List<String> actual2 = container.resolve(new ResolutionToken<List<String>>() {});

        // Assert
        assertThat(actual1).isNotNull();
        assertThat(actual2).isNotNull();

        assertThat(actual1).isNotSameAs(actual2);
    }
    
    @Test
    public void Resolve_TypeWithNoDependenciesThroughReflection_ReturnsNewInstance() {
        // Arrange
        Container container = new InjectContainer();
        container.register(
            registration()
                .component(new Component<List<String>, ArrayList<String>>() {})
                .build()
        );
        
        // Act
        List<String> actual = container.resolve(new ResolutionToken<List<String>>() {});
        
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual instanceof ArrayList);
    }
}
