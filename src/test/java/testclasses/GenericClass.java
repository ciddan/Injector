package testclasses;

public class GenericClass<T> implements GenericInterface<T> {
  private final Class<T> clazz;

  public GenericClass(Class<T> clazz) {
    this.clazz = clazz;
  }
}
