package com.communalizer.inject;

import com.communalizer.inject.kernel.Registration;
import com.communalizer.inject.kernel.RegistrationBuilder;
import com.communalizer.inject.kernel.TypeProvider;
import com.communalizer.inject.kernel.TypeToken;

import java.util.Map;

public interface Container {
    <T> void register(Registration<T, ?> registration);
    <T> void register(Registration<T, ?>... registrations);

    void register(RegistrationBuilder builder);
    void register(RegistrationBuilder... builders);

    <T> T resolve(TypeToken<T> token);
    <T> T resolve(TypeToken<T> token, String name);

    Map<String, TypeProvider<?>> getRegistry();

}
