package cc.domovoi.spring.utils;

import org.springframework.beans.SimpleTypeConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.Formatter;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.util.Map;

public class TypeConverterUtils {

    private static final SimpleTypeConverter typeConverter = new SimpleTypeConverter();

    private static final DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(true);

    static {
        addConverter(new ObjectToMapConverter());
        typeConverter.setConversionService(conversionService);
    }

    public static <T> void addFormatter(Formatter<T> formatter) {
        conversionService.addFormatter(formatter);
    }

    public static <S, T> void addConverter(Converter<S, T> converter) {
        conversionService.addConverter(converter);
    }

    public static <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
        conversionService.addConverter(sourceType, targetType, converter);
    }

    public static void addConverter(GenericConverter converter) {
        conversionService.addConverter(converter);
    }

    public static <T> T convert(Object value, Class<T> requiredType) {
        return typeConverter.convertIfNecessary(value, requiredType);
    }

    public static class ObjectToMapConverter implements Converter<Object, Map<String, Object>> {
        @Override
        public Map<String, Object> convert(Object source) {
            return BeanMapUtils.beanToMap(source);
        }
    }
}
