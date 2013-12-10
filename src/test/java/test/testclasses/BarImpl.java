package test.testclasses;

public class BarImpl implements Bar {
  private final Foo foo;

  public BarImpl(Foo foo) {
    this.foo = foo;
  }

  @Override
  public Foo getFoo() {
    return foo;
  }
}
