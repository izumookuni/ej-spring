package cc.domovoi.spring.test;

import cc.domovoi.spring.utils.ClassUtils;
import org.jooq.lambda.tuple.Tuple1;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ClassUtilsTest {

    private abstract class TestAbsClass<T> {}

    private class TestClass extends TestAbsClass<Tuple2<Integer, Tuple3<Boolean, Tuple2<String, List<Integer>>, Double>>> {}

    @Test
    public void testGenericType() {

//        Tuple2<Integer, Tuple3<Boolean, Tuple2<String, List<Integer>>, Double>> tuple = new Tuple2<>(42, new Tuple3<>(false, new Tuple2<>("Hello", Arrays.asList(1, 2, 3)), 3.14));

        Assert.assertEquals(Tuple2.class, ClassUtils.genericType(TestClass.class, 0));

        Assert.assertEquals(Integer.class, ClassUtils.genericType(TestClass.class, 0, 0));
        Assert.assertEquals(Tuple3.class, ClassUtils.genericType(TestClass.class, 0, 1));

        Assert.assertEquals(Boolean.class, ClassUtils.genericType(TestClass.class, 0, 1, 0));
        Assert.assertEquals(Tuple2.class, ClassUtils.genericType(TestClass.class, 0, 1, 1));
        Assert.assertEquals(Integer.class, ClassUtils.genericType(TestClass.class, 0, 1, 1, 1, 0));

    }
}
