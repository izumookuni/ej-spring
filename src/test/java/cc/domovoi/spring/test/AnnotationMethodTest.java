package cc.domovoi.spring.test;

import cc.domovoi.spring.annotation.after.AfterFind;
import cc.domovoi.spring.annotation.method.ForcedThrow;
import cc.domovoi.spring.utils.GeneralUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationMethodTest {

    private static Logger logger = LoggerFactory.getLogger(AnnotationMethodTest.class);

    @AfterFind(order = 1)
    public void test1() {
        logger.debug("test1");
    }

    @AfterFind(order = 2)
    @ForcedThrow
    public void test2() {
//        logger.debug("test2");
        throw new RuntimeException("test2 failed");
    }

    @AfterFind(order = 3)
    public void test3() {
        logger.debug("test3");
    }

    @Test
    public void test() throws Exception {
        Map<String, Object> params = new HashMap<>();
        GeneralUtils.doAnnotationMethod(this, AfterFind.class, 0, "test", params);
    }
}
