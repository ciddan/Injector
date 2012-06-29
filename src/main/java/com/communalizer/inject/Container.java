package com.communalizer.inject;

import com.communalizer.inject.kernel.Registration;
import com.communalizer.inject.kernel.RegistrationBuilder;
import com.communalizer.inject.kernel.ResolutionToken;

import java.util.Map;

public interface Container {
    <T> void register(Registration<T, ?> registration);
    <T> void register(Registration<T, ?>... registrations);

    void register(RegistrationBuilder builder);
    void register(RegistrationBuilder... builders);

    <T> T resolve(ResolutionToken<T> token);

    Map<String, Registration> getRegistry();

}
