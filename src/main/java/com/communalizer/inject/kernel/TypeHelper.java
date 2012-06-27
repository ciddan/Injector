package com.communalizer.inject.kernel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeHelper {
    public static void checkGenericTypeCompatibility(ParameterizedType x, ParameterizedType y) {

        // Check both raw types (i.e. type-erased List, ArrayList) for compatibility.
        checkClassCompatibility(
            (Class) x.getRawType(),
            (Class) y.getRawType()
        );

        Type[] xTypes = x.getActualTypeArguments();
        Type[] yTypes = y.getActualTypeArguments();

        // If x-type contains more or fewer type-arguments we can conclude the types are incompatible.
        // I.e: X<String, Integer> can never be compatible with Y<Object>.
        if (xTypes.length != yTypes.length) {
            throw new RuntimeException(
                String.format(
                    "Type: %s does not have the same amount of type parameters as: %s.",
                    x.toString(),
                    y.toString()
                )
            );
        }

        for(int i = 0; i < xTypes.length; i++) {
            Type xt = xTypes[i];
            Type yt = yTypes[i];

            if ((xt instanceof ParameterizedType && yt instanceof ParameterizedType)) {
                // Check the generic type arguments (i.e. X<Object>, X<String>) for compatibility.
                checkGenericTypeCompatibility((ParameterizedType) xt,(ParameterizedType) yt);
            } else if (!(xt instanceof ParameterizedType) && !(yt instanceof ParameterizedType)) {
                checkClassCompatibility((Class) xt, (Class) yt);
            } else {
                throw new RuntimeException(
                    String.format(
                        "Generic type argument mismatch between: %s & %s.",
                        xt.toString(),
                        yt.toString()
                    )
                );
            }
        }
    }

    public static void checkClassCompatibility(Class x, Class y) {
        if (!(x).isAssignableFrom(y)) {
            throw new IllegalArgumentException(
                    String.format(
                            "Type parameters: %s is not assignable from %s.",
                            x.getName(),
                            y.getName()
                    )
            );
        }
    }
}
