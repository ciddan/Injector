package testclasses;

import java.util.List;

public class Baz {
    private final List<String> strings;

    public List<String> getStrings() {
        return strings;
    }


    public Baz(String s, List<String> sl, Integer i) {
        this.strings = sl;
    }
}
