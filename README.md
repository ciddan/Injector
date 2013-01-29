# Injector
*By [@Ciddish](https://twitter.com/ciddish)*

## About
Injector is a modern and easy to use Java Inversion of Control container. Furthermore, it's annotation and XML-file free!

## Usage
```Java
// Create a new Container
Container container = new InjectContainer();

// Register your types
container.register(
   registration()
     .component(new Component<Foo, FooImpl>() {})
     .build(),
   registration()
     .component(new Component<Bar, BarImpl>() {})
     .build()
);

// And resolve them later
Bar resolvedBar = container.resolve(new TypeToken<Bar>() {});
```

## Features

### Registration / Resolution strategies

>**Injector** contains a static helper for building component registrations. Be sure to `import static com.communalizer.inject.kernel.RegistrationBuilder.registration;`. That allows for the registration syntax used in the examples below.

**Injector** provides support for a number of different resolution strategies:

1. Reflection - Components are created using the "largest" possible Constructor (depending on which constructor dependencies have been registered).
2. Factory - You can provide a custom factory function that is called during resolution that instantiates a Component.
3. Instance - You can provide an object instance during registration. Every subsequent resolution returns that instance.


#### Reflection
Let's say BarImpl requires a Foo in its Constructor. Upon resolution, Injector will investigate BarImpl's Constructor and automatically inject a reflected new instance of a FooImpl since we provided that mapping.

```Java
container.register(
    registration()
        .component(new Component<Foo, FooImpl>() {})
        .build(),
    registration()
        .component(new Component<Bar, BarImpl>() {})
        .build()
);

Bar bar = container.resolve(new TypeToken<Bar>() {});
```

#### Factory
You can register a factory function along with your component registration. That function is then called every time you resolve a component of that type. NOTE: This will be updated to support Singleton Component behaviour with factory functions.

```Java
Factory<List<String>> factory = new Factory<List<String>>() {
    @Override
    public List<String> create() {
        return new ArrayList<String>();
    }
};

container.register(
    registration()
        .component(new Component<List<String>, ArrayList<String>>() {})
        .factory(factory)
        .build()
);

List<String> actual1 = container.resolve(new TypeToken<List<String>>() {});
List<String> actual2 = container.resolve(new TypeToken<List<String>>() {});

// Assert
assertThat(actual1).isNotNull();
assertThat(actual2).isNotNull();

assertThat(actual1).isNotSameAs(actual2);
```

#### Instance
A straight-forward Singleton. Register an object instance and have it returned every time.

```Java
List<String> instance = new ArrayList<String>();

container.register(
    registration()
        .component(new Component<List<String>, ArrayList<String>>() {})
        .instance(instance)
        .build()
);

List<String> actual = container.resolve(new TypeToken<List<String>>() {});

assertThat(actual).isNotNull();
assertThat(actual).isSameAs(instance);
```

---------------------------------------------

### Type checking

**Injector** uses anonymous generic class implementations to represent Component registrations. Because of that, it has access to generic type information at runtime. Normally that information is erased by the java compiler in a process known as [Type Erasure](http://docs.oracle.com/javase/tutorial/java/generics/erasure.html).

However, due to the fact that anonymous classes retain that information, **Injector** can perform type checking and type compatibility checks during runtime. Cue unit test copy & paste!

```Java
@Test(expected = IllegalArgumentException.class)
public void Component_WithIncompatibleTypes_Throws() {
    // Act
    new Component<String, Integer>() {};
}

@Test(expected = IllegalArgumentException.class)
public void Component_WithCompatibleBaseTypesAndIncompatibleWrappedTypes_Throws() {
    // Act
    new Component<List<String>, ArrayList<Integer>>() {};
}
```

This also applies to Factory components:

```Java
@Test(expected = IllegalArgumentException.class)
public void Component_CreatedWithFactoryNotCreatingCompatibleTypes_Throws() {
    // Arrange
    Factory<Object> factory = new Factory<Object>() {
        @Override
        public Integer create() {
            return 1;
        }
    };

    // Act
    new Component<Object, String>(factory){};
}
```

---------------------------------------------

### Explicit dependencies
There are times when you need to be explicit about which specific type (or which exact instance) of a dependency is passed in to a Component at resolve-time. Injector allows for that with explicit dependencies.

You can define an explicit dependency in three ways:

1. A specific object instance.
2. A Factory function.
3. A specific named Component registration.

#### Instance dependency
Given that `QuuxImpl`'s constructor has a parameter of type `Foo` named `foo1`.
`public QuuxImpl(Foo foo1) {`

```Java
Foo instance = new FooImpl();

container.register(
    registration()
        .component(new Component<Quux, QuuxImpl>() {})
        .dependsOn("foo1", instance)
);
```

#### Factory dependency
Uses the provided Factory function to instantiate the named constructor parameter.

```Java
Factory<Foo> fooFactory = new Factory<Foo>() {
    @Override
    public Foo create() {
        return instance;
    }
};

container.register(
    registration()
        .component(new Component<Quux, QuuxImpl>() {})
        .dependsOn("foo1", fooFactory)
);
```

#### Named Component registration
Since you can register multiple implementations for any given base type it's useful to be able to point to a specific registration if you need a specific implementation passed into a Component's constructor.

```Java
final Foo instance = new FooImpl();

container.register(
    registration()
        .component(new Component<Foo, FooImpl>() {}),
    registration()
        .component(new Component<Foo, FooImpl>() {})
        .instance(instance)
        .named("superSpecialFoo"),
    registration()
        .component(new Component<Quux, QuuxImpl>() {})
        .dependsOn("foo1", new TypeToken<Foo>() {}, "superSpecialFoo")
);

Quux actual = container.resolve(new TypeToken<Quux>() {});

// Assert
assertThat(actual).isNotNull();
assertThat(actual.getFoo1()).isSameAs(instance);
assertThat(actual.getFoo2()).isNotSameAs(instance);
```

## Limitations
Since **Injector** uses a [BytecodeReadingParanamer](http://paranamer.codehaus.org/javadoc/com/thoughtworks/paranamer/BytecodeReadingParanamer.html) to extract information about the types registered in the container, it relies on debug information compiled with the "-g" javac option. It's not ideal, to be sure, but it was a necessary trade-off in order to get rid of annotations and XML-configuration.

## ToDo
Setting component lifestyle (Pooled, Transient, Singleton, ThreadScope etc).
Explicit de-registration/release of registered components.

## Issues or ideas?
Feel free to open an [issue](https://github.com/ciddan/Injector/issues) or fork it and submit a pull request!