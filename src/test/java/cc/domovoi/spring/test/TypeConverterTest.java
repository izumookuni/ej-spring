package cc.domovoi.spring.test;

import cc.domovoi.spring.utils.TypeConverterUtils;
import org.jooq.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeConverterTest {

    private static Logger logger = LoggerFactory.getLogger(TypeConverterTest.class);

    @Test
    public void testTypeConverter() throws Exception {
        String s1 = "666";
        Integer r1 = TypeConverterUtils.convert(s1, Integer.class);
        logger.debug("r1: {}, class: {}", r1, r1.getClass());
        Double r2 = TypeConverterUtils.convert(s1, Double.class);
        logger.debug("r2: {}, class: {}", r2, r2.getClass());

        JSON s3 = JSON.valueOf("{\"a\":\"haha\"}");
        String r3 = TypeConverterUtils.convert(s3, String.class);
        logger.debug("r3: {}, class: {}", r3, r3.getClass());
        String s4 = "{\"a\":\"haha\"}";
        JSON r4 = TypeConverterUtils.convert(s4, JSON.class);
        logger.debug("r4: {}, class: {}", r4, r4.getClass());
    }

}
