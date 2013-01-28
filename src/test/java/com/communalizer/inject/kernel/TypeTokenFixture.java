package com.communalizer.inject.kernel;

import org.junit.Test;
import testclasses.BazImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class TypeTokenFixture {
    @Test(expected = IllegalArgumentException.class)
    public void TypeToken_ConstructedAsRawType_Throws() {
        // Act
        new TypeToken() {};
    }

    @Test
    public void TypeToken_GetKey_GeneratesTypeKey() {
        // Arrange
        String expectedKey = "java.lang.String";
        TypeToken<String> tt = new TypeToken<String>() {};

        // Act
        String actual = tt.getKey();

        // Assert
        assertThat(actual).isEqualTo(expectedKey);
    }

    @Test
    public void TypeToken_CanGetToken_FromClass() {
        // Arrange
        String expectedKey = "java.lang.String";

        // Act
        TypeToken<String> token = TypeToken.getToken(String.class);

        // Assert
        assertThat(token.getKey()).isEqualTo(expectedKey);
    }

    @Test
    public void TypeToken_CanGetTokenWithPreservedGenericInformation_FromParametrizedType() {
        // Arrange
        String expectedKey = "java.util.List<java.lang.String>";

        Constructor constructor = BazImpl.class.getConstructors()[0];
        Type t = constructor.getGenericParameterTypes()[0];

        // Act
        TypeToken<List<String>> token = TypeToken.getToken(t);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token.getKey()).isEqualTo(expectedKey);
    }
}
