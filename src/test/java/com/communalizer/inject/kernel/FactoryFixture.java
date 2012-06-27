package com.communalizer.inject.kernel;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FactoryFixture {
    @Test
    public void Factory_CanBeImplemented_ToCreateAFactoryMethod() {
        // Arrange
        final String anyString = "";

        // Act
        Factory<String> fac = new Factory<String>() {
            @Override
            public String create() {
                return anyString;
            }
        };

        // Assert
        assertThat(fac.create()).isEqualTo(anyString);
    }
}
