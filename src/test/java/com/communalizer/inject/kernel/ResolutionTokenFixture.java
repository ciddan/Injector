package com.communalizer.inject.kernel;

import org.junit.Test;

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
}
