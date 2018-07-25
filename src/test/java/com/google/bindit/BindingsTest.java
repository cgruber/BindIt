package com.google.bindit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Test;

public class BindingsTest {

  @Test
  public void testBinding() {
    E e = Binder.inject(E.class, new Module());
    assertNotNull(e);
    assertTrue(e.a instanceof A);
    assertTrue(e.b instanceof BImpl);
    assertTrue(e.c instanceof CImpl);
    assertTrue(e.d instanceof DImpl);
    assertSame(e.c, ((DImpl)e.d).c);
  }
 
  public static class A {}
  public interface B {}
  public static class BImpl implements B {}
  public interface C {}
  public static class CImpl implements C {}
  public interface D {}
  public static class DImpl implements D {
    public final C c;
    @Inject public DImpl(C c) {
      this.c = c;
    }
  }
  public static class E {
    public final A a;
    public final B b;
    public final C c;
    public final D d;
    @Inject public E(A a, B b, C c, D d) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.d = d;
    }
  }
  
  @Bindings({
    @Bind(type=A.class),
    @Bind(type=B.class, impl=BImpl.class),
    @Bind(type=C.class, impl=CImpl.class, scope=Singleton.class),
  })
  public static class Module {
    @Provides public D provideD(C c) {
      return new DImpl(c);
    }
  }
  
}
