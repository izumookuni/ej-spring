package cc.domovoi.spring.utils;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import org.jooq.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServiceUtils {

    /**
     * 分组查询
     * @param mapper mapper实例
     * @param query 原始查询请求
     * @param qkF 将查询请求变成key列表（当列表长度超过500时，将分组查询）
     * @param qkq2F 根据原始查询请求和key列表生成最终查询请求
     * @param mq2rF 根据mapper实例和最终查询请求获得最终结果
     * @param <M> mapper实例类型
     * @param <K> key列表类型
     * @param <Q> 原始查询请求类型
     * @param <Q2> 最终查询请求类型
     * @param <R> 最终结果类型
     * @return 最终结果列表
     */
    public static <M, K, Q, Q2, R> List<R> findListGrouped(M mapper,
                                                           Q query,
                                                           Function<? super Q, ? extends List<K>> qkF,
                                                           BiFunction<? super Q, ? super List<K>, ? extends Q2> qkq2F,
                                                           BiFunction<? super M, ? super Q2, ? extends List<R>> mq2rF) {
        List<K> keyList = qkF.apply(query);
        if (keyList == null) {
            return Collections.emptyList();
        }
        int listSize = keyList.size();
        if (listSize <= 500) {
            Q2 q2 = qkq2F.apply(query, keyList);
            return mq2rF.apply(mapper, q2);
        }
        else {
            List<R> resultList = new ArrayList<>();
            for(int i = 0; i < listSize / 500; ++i) {
                List<K> innerIdList = keyList.subList(i * 500, (i + 1) * 500);
                Q2 q2 = qkq2F.apply(query, innerIdList);
                List<R> innerEntityList = mq2rF.apply(mapper, q2);
                resultList.addAll(innerEntityList);
            }
            return resultList;
        }
    }

    public static <E> Try<Tuple2<Integer, List<String>>> collectAddResult(List<Try<Tuple2<Integer, String>>> addResultList) {
        if (addResultList.stream().allMatch(Try::isSuccess)) {
            return new Success<>(new Tuple2<>(addResultList.size(), addResultList.stream().map(Try::get).map(Tuple2::v2).collect(Collectors.toList())));
        }
        else {
            return new Failure<>(new RuntimeException(addResultList.stream().filter(Try::isFailure).findFirst().map(t -> t.failed().get().getMessage()).get()));
        }
    }
}
