package testclasses;

import java.util.List;

public class BazImpl implements Baz {
  private final List<String> strings;

  @Override
  public List<String> getStrings() {
    return strings;
  }


  public BazImpl(List<String> sl) {
    this.strings = sl;
  }
}
