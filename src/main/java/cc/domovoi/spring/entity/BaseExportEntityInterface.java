package cc.domovoi.spring.entity;

import cc.domovoi.ej.collection.tuple.Tuple2;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BaseExportEntityInterface<C> {

    Supplier<? extends String> exportTitle();

    List<Tuple2<String, Function<? super C, ? extends Object>>> exportColumnMeta();

    Supplier<? extends List<C>> exportContent();

    Supplier<? extends String> exportSheetName();
}
