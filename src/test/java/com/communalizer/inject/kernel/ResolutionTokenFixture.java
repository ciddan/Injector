package com.communalizer.inject.kernel;

import org.junit.Test;
import testclasses.BazImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import static org.fest.assertions.Assertions.assertThat;

public class ResolutionTokenFixture {
    @Test(expected = IllegalArgumentException.class)
    public void ResolutionToken_ConstructedAsRawType_Throws() {
        // Act
        new ResolutionToken() {};
    }

    @Test
    public void ResolutionToken_GetKeyWithoutName_GeneratesTypeKey() {
        // Arrange
        String expectedKey = "java.lang.String";
        ResolutionToken<String> rt = new ResolutionToken<String>() {};

        // Act
        String actual = rt.getKey();

        // Assert
        assertThat(actual).isEqualTo(expectedKey);
    }

    @Test
    public void ResolutionToken_GetKeyWithEmptyName_GeneratesTypeKey() {
        // Arrange
        String expectedKey = "java.lang.String";
        ResolutionToken<String> rt = new ResolutionToken<String>("") {};

        // Act
        String actual = rt.getKey();

        // Assert
        assertThat(actual).isEqualTo(expectedKey);
    }

    @Test
    public void ResolutionToken_GetKeyWithName_GeneratesCompoundTypeNameKey() {
        // Arrange
        String expectedKey = "java.lang.String-Foo";
        ResolutionToken<String> rt = new ResolutionToken<String>("Foo") {};

        // Act
        String actual = rt.getKey();

        // Assert
        assertThat(actual).isEqualTo(expectedKey);
    }

    @Test
    public void ResolutionToken_CanGetToken_FromClass() {
        // Arrange
        String expectedKey = "java.lang.String";

        // Act
        ResolutionToken<String> token = ResolutionToken.getToken(String.class);

        // Assert
        assertThat(token.getKey()).isEqualTo(expectedKey);
    }

    @Test
    public void ResolutionToken_CanGetTokenWithPreservedGenericInformation_FromParametrizedType() {
        // Arrange
        String expectedKey = "java.util.List<java.lang.String>";

        Constructor constructor = BazImpl.class.getConstructors()[0];
        Type t = constructor.getGenericParameterTypes()[0];

        // Act
        ResolutionToken token = ResolutionToken.getToken(t);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token.getKey()).isEqualTo(expectedKey);
    }
}
