package com.communalizer.inject;

import com.communalizer.inject.kernel.Registration;
import com.communalizer.inject.kernel.ResolutionToken;

import java.util.Map;

public interface Container {
    void register(Registration registration);
    void register(Registration... registrations);

    <T> T resolve(ResolutionToken<T> token);

    Map<String, Registration> getRegistry();

}