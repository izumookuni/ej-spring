package cc.domovoi.spring.test;

import cc.domovoi.spring.annotation.after.AfterFind;
import cc.domovoi.spring.annotation.method.ForcedBreak;
import cc.domovoi.spring.annotation.method.ForcedThrow;
import cc.domovoi.spring.annotation.method.Param;
import cc.domovoi.spring.utils.GeneralUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationMethodTest {

    private static Logger logger = LoggerFactory.getLogger(AnnotationMethodTest.class);

    @AfterFind(order = 1)
    public void test1(@Param("a") Integer a) {
        logger.debug("test1 a: {}", a);
    }

    @AfterFind(order = 2)
    @ForcedThrow
//    @ForcedBreak
    public void test2(@Param(value = "b", defaultValue = "3")Integer b) {
        logger.debug("test2 b: {}", b);
//        throw new RuntimeException("test2 failed");
    }

    @AfterFind(order = 3)
    public void test3(@Param(value = "c", checkNull = true, alias = {"d", "e"}) Integer c, @Param("b") Integer b) {
        logger.debug("test3 b: {}, c: {}", b, c);
    }

    @Test
    public void test() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("a", 2);
//        params.put("b", 3);
        params.put("c", null);
        params.put("d", 7);
        params.put("e", 11);
        GeneralUtils.doAnnotationMethod(this, AfterFind.class, 0, "test", params);
    }
}
