package com.communalizer.inject;

import com.communalizer.inject.kernel.Component;
import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.TypeToken;
import org.junit.Test;
import testclasses.*;

import java.util.ArrayList;
import java.util.List;

import static com.communalizer.inject.kernel.RegistrationBuilder.registration;
import static org.fest.assertions.Assertions.assertThat;

public class InjectContainerResolutionFixture {
    @Test(expected = IllegalArgumentException.class)
    public void Resolve_WithNullResolutionToken_Throws() {
        // Arrange
        Container container = getNewInjectContainer();

        // Act
        container.resolve(null);
    }

    @Test(expected = RuntimeException.class)
    public void Resolve_TypeThatIsNotRegistered_Throws() {
        // Arrange
        Container container = getNewInjectContainer();

        // Act
        container.resolve(new TypeToken<List<String>>() {});
    }

    @Test
    public void Resolve_TypeThatHasBeenRegisteredWithInstance_ReturnsTheRegisteredInstance() {
        // Arrange
        List<String> instance = new ArrayList<String>();
        Component<List<String>, ArrayList<String>> component = new Component<List<String>, ArrayList<String>>() {};

        Container container = getNewInjectContainer();
        container.register(
            registration()
                .component(component)
                .instance(instance)
                .build()
        );

        // Act
        List<String> actual = container.resolve(new TypeToken<List<String>>() {});

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

        Container container = getNewInjectContainer();
        container.register(
            registration()
                .component(component)
                .factory(fac)
                .build()
        );

        // Act
        List<String> actual1 = container.resolve(new TypeToken<List<String>>() {});
        List<String> actual2 = container.resolve(new TypeToken<List<String>>() {});

        // Assert
        assertThat(actual1).isNotNull();
        assertThat(actual2).isNotNull();

        assertThat(actual1).isNotSameAs(actual2);
    }

    @Test
    public void Resolve_UsingNamedToken_ReturnsExplicitRegisteredInstance() {
        // Arrange
        Component<Object, String> c1 = new Component<Object, String>() {};
        Component<Object, Integer> c2 = new Component<Object, Integer>() {};

        Container container = getNewInjectContainer();
        container.register(
            registration()
                .component(c1)
                .named("foo"),
            registration()
                .component(c2)
        );

        // Act
        Object actual = container.resolve(new TypeToken<Object>() {}, "foo");

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual instanceof String);
    }

    @Test
    public void Resolve_PlainTypeWithNoDependenciesThroughReflection_ReturnsNewInstance() {
        // Arrange
        Container container = getNewInjectContainer();
        container.register(
            registration()
                .component(new Component<Object, String>() {})
                .build()
        );

        // Act
        Object actual = container.resolve(new TypeToken<Object>() {});

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual instanceof String);
    }

    @Test
    public void Resolve_GenericTypeWithNoDependenciesThroughReflection_ReturnsNewInstance() {
        // Arrange
        Container container = getNewInjectContainer();
        container.register(
            registration()
                .component(new Component<List<String>, ArrayList<String>>() {})
                .build()
        );

        // Act
        List<String> actual = container.resolve(new TypeToken<List<String>>() {});

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual instanceof ArrayList);
    }

    @Test
    public void Resolve_PlainTypeWithRegisteredDependency_CreatesANewInstanceAndInjectsTheDependency() {
        // Arrange
        Container container = getNewInjectContainer();
        container.register(
            registration()
                .component(new Component<Bar, BarImpl>() {})
                .build(),
            registration()
                .component(new Component<Foo, FooImpl>() {})
                .build()
        );

        // Act
        Bar actual = container.resolve(new TypeToken<Bar>() {});

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual instanceof BarImpl);

        assertThat(actual.getFoo()).isNotNull();
        assertThat(actual.getFoo() instanceof FooImpl);
    }

    @Test
    public void Resolve_WithMoreThanOneRegisteredComponentOfTheSameRawType_TheCorrectOneIsResolved() {
        // Arrange
        List<String> l1 = new ArrayList<String>();
        List<Integer> l2 = new ArrayList<Integer>();

        Component<List<String>, ArrayList<String>> c1 = new Component<List<String>, ArrayList<String>>(l1) {};
        Component<List<Integer>, ArrayList<Integer>> c2 = new Component<List<Integer>, ArrayList<Integer>>(l2) {};

        Container container = getNewInjectContainer();
        container.register(
            registration()
                .component(c1)
                .build(),
            registration()
                .component(c2)
                .build()
        );

        // Act
        List actual = container.resolve(new TypeToken<List<Integer>>() {});

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isSameAs(l2);
    }

    @Test
    public void Resolve_ComponentWithGenericConstructorParameterWhenMoreThanOneOfTheSameRawTypeIsRegistered_SelectsTheFullyMatchingDependency() {
        // Arrange
        List<String> l1 = new ArrayList<String>();
        List<Integer> l2 = new ArrayList<Integer>();

        Component<List<String>, ArrayList<String>> c1 = new Component<List<String>, ArrayList<String>>(l1) {};
        Component<List<Integer>, ArrayList<Integer>> c2 = new Component<List<Integer>, ArrayList<Integer>>(l2) {};
        Component<Baz, BazImpl> c3 = new Component<Baz, BazImpl>() {};

        Container container = getNewInjectContainer();
        container.register(
            registration()
                .component(c1)
                .build(),
            registration()
                .component(c2)
                .build(),
            registration()
                .component(c3)
                .build()
        );

        // Act
        Baz baz = container.resolve(new TypeToken<Baz>() {});

        // Assert
        assertThat(baz.getStrings()).isSameAs(l1);
    }

    @Test
    public void Resolve_ComponentWithUnregisteredDependencies_ThrowsRuntimeException() {
        // Arrange
        Container container = getNewInjectContainer();
        Component<Quux, QuuxImpl> qc = new Component<Quux, QuuxImpl>() {};

        container.register(
            registration()
                .component(qc)
        );

        // Act
        try {
            container.resolve(new TypeToken<Quux>() {});
        } catch (Exception e) {
            // Assert
            assertThat(e.getMessage()).isEqualTo(
                String.format(
                    "java.lang.RuntimeException: Component '%s' has unregistered dependencies ['%s', '%s']. Cannot resolve.",
                    "testclasses.Quux->testclasses.QuuxImpl", "testclasses.Foo", "testclasses.Foo"
                )
            );

            return;
        }

        assertThat(false);
    }

    @Test
    public void Resolve_ComponentWithExplicitDependencyInstance_GetsThatExplicitInstanceInjectedForTheCorrectParameter() {
        // Arrange
        Container container = getNewInjectContainer();
        Foo instance = new FooImpl();
        Component<Foo, FooImpl> fc = new Component<Foo, FooImpl>() {};
        Component<Quux, QuuxImpl> qc = new Component<Quux, QuuxImpl>() {};

        container.register(
            registration()
                .component(fc),
            registration()
                .component(qc)
                .dependsOn("foo1", instance)
        );

        // Act
        Quux actual = container.resolve(new TypeToken<Quux>() {});

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getFoo1()).isSameAs(instance);
        assertThat(actual.getFoo2()).isNotSameAs(instance);
    }

    @Test
    public void Resolve_ComponentWithExplicitDependencyFactory_GetsThatExplicitInstanceInjectedForTheCorrectParameter() {
        // Arrange
        Container container = getNewInjectContainer();
        final Foo instance = new FooImpl();

        Component<Foo, FooImpl> fc = new Component<Foo, FooImpl>() {};
        Component<Quux, QuuxImpl> qc = new Component<Quux, QuuxImpl>() {};

        Factory<Foo> fooFactory = new Factory<Foo>() {
            @Override
            public Foo create() {
                return instance;
            }
        };

        container.register(
            registration()
                .component(fc),
            registration()
                .component(qc)
                .dependsOn("foo1", fooFactory)
        );

        // Act
        Quux actual = container.resolve(new TypeToken<Quux>() {});

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getFoo1()).isSameAs(instance);
        assertThat(actual.getFoo2()).isNotSameAs(instance);
    }

    @Test
    public void Resolve_ComponentWithExplicitDependencyResolutionToken_GetsThatExplicitInstanceInjectedForTheCorrectParameter() {
        // Arrange
        Container container = getNewInjectContainer();
        final Foo instance = new FooImpl();

        Component<Foo, FooImpl> fc = new Component<Foo, FooImpl>() {};
        Component<Foo, FooImpl> fc2 = new Component<Foo, FooImpl>() {};
        Component<Quux, QuuxImpl> qc = new Component<Quux, QuuxImpl>() {};

        container.register(
            registration()
                .component(fc),
            registration()
                .component(fc2)
                .instance(instance)
                .named("foo"),
            registration()
                .component(qc)
                .dependsOn("foo1", new TypeToken<Foo>() {}, "foo")
        );

        // Act
        Quux actual = container.resolve(new TypeToken<Quux>() {});

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getFoo1()).isSameAs(instance);
        assertThat(actual.getFoo2()).isNotSameAs(instance);
    }

    private static Container getNewInjectContainer() {
        return new InjectContainer();
    }
}
