package testclasses;

import java.util.List;

public class Baz {
    private final List<String> strings;

    public List<String> getStrings() {
        return strings;
    }


    public Baz(List<String> sl) {
        this.strings = sl;
    }
}
