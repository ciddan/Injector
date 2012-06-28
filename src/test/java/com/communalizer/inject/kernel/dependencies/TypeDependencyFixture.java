package com.communalizer.inject.kernel.dependencies;

import com.communalizer.inject.kernel.Factory;
import com.communalizer.inject.kernel.ResolutionToken;
import com.communalizer.inject.kernel.TypeToken;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class TypeDependencyFixture {
    @Test(expected = IllegalArgumentException.class)
    public void TypeDependency_WithNullTypeToken_Throws() {
        new TypeDependency<Class>(null, String.class);
    }

    @Test
    public void TypeDependency_CanBeCratedWithTypeTokenAndInstance() {
        // Arrange
        String instance = "S";
        TypeToken<String> tt = new TypeToken<String>() {};

        // Act
        TypeDependency<String> typeDependency = new TypeDependency<String>(tt, instance);

        // Assert
        assertThat(typeDependency).isNotNull();
        assertThat(typeDependency.getInstance()).isSameAs(instance);
    }

    @Test
    public void TypeDependency_CanBeCratedWithTypeTokenAndFactory() {
        // Arrange
        final String anyString = "S";

        Factory<String> factory = new Factory<String>() {
            @Override
            public String create() {
                return anyString;
            }
        };
        TypeToken<String> tt = new TypeToken<String>() {};

        // Act
        TypeDependency<String> typeDependency = new TypeDependency<String>(tt, factory);

        // Assert
        assertThat(typeDependency).isNotNull();
        assertThat(typeDependency.getFactoryArtifact()).isSameAs(anyString);
    }

    @Test
    public void TypeDependency_CanBeCreatedWithResolutionToken() {
        // Arrange
        ResolutionToken<String> rt = new ResolutionToken<String>("foo") {};
        TypeToken<String> tt = new TypeToken<String>() {};

        // Act
        TypeDependency<String> dep = new TypeDependency<String>(tt, rt);

        // Assert
        assertThat(dep).isNotNull();
        assertThat(dep.getResolutionToken()).isSameAs(rt);
    }

    @Test
    public void TypeDependency_GetIdentifier_ReturnsFullTypeAsString() {
        // Arrange
        List<String> instance = new ArrayList<String>();
        String expected = "java.util.List<java.lang.String>";
        TypeToken<List<String>> tt = new TypeToken<List<String>>() {};

        TypeDependency<List<String>> dep = new TypeDependency<List<String>>(tt, instance);

        // Act
        String actual = dep.getIdentifier();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
