package test;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import org.testng.annotations.Test;
import test.testclasses.*;

import java.lang.reflect.Constructor;

public class Playground {
  @Test
  public void foo() {
    Paranamer paranamer = new BytecodeReadingParanamer();
    Constructor ctor = BarImpl.class.getConstructors()[0];

    String[] params = paranamer.lookupParameterNames(ctor);

    for (String s : params) {
      System.out.println(s);
    }
  }
}
