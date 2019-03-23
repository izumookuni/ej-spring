package cc.domovoi.spring.utils.generalquery;

import cc.domovoi.spring.entity.BasePagingEntityInterface;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeneralQueryModel implements BasePagingEntityInterface {

    private static Converter<String, String> converter = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);

    private String baseTable;

    private Map<String, String> baseColumn;

    private Map<String, String> baseInnerJoin;

    private Map<String, String> baseLeftJoin;

    private Map<String, String> baseRightJoin;

    private Map<String, String> baseFullJoin;

    private Map<String, String> baseWhereEqual;

    private Map<String, String> baseWhereNotEqual;

    private Map<String, String> baseWhereLike;

    private Map<String, List<String>> baseWhereIn;

    private List<String> baseWhereNull;

    private List<String> baseWhereNotNull;

    private Map<String, String> baseWhereLessThan;

    private Map<String, String> baseWhereGreaterThan;

    private Map<String, String> baseWhereEqualLessThan;

    private Map<String, String> baseWhereEqualGreaterThan;

    private Integer pageNum;

    private Integer pageSize;

    private List<String> sortBy;

    private String sortOrder;

    @Override
    public Function<String, Boolean> checkSortKey() {
        return (key) -> true;
    }

    public GeneralQueryModel() {
    }

    public void putBaseColumn(String column, String as) {
        if (baseColumn == null) {
            baseColumn = new HashMap<>();
        }
        baseColumn.put(column, as);
    }

    public void putBaseColumn(String column) {
        if (baseColumn == null) {
            baseColumn = new HashMap<>();
        }
        baseColumn.put(column, column);
    }

    public void putBaseColumnUnderscore(String column) {
        if (baseColumn == null) {
            baseColumn = new HashMap<>();
        }
        baseColumn.put(column, converter.convert(column.toLowerCase()));
    }

    public void putBaseInnerJoin(String joiningTable, String on) {
        if (baseInnerJoin == null) {
            baseInnerJoin = new HashMap<>();
        }
        baseInnerJoin.put(joiningTable, on);
    }

    public void putBaseLeftJoin(String joiningTable, String on) {
        if (baseLeftJoin == null) {
            baseLeftJoin = new HashMap<>();
        }
        baseLeftJoin.put(joiningTable, on);
    }

    public void putBaseRightJoin(String joiningTable, String on) {
        if (baseRightJoin == null) {
            baseRightJoin = new HashMap<>();
        }
        baseRightJoin.put(joiningTable, on);
    }

    public void putBaseFullJoin(String joiningTable, String on) {
        if (baseFullJoin == null) {
            baseFullJoin = new HashMap<>();
        }
        baseFullJoin.put(joiningTable, on);
    }

    public void putBaseWhereEqual(String column, String value) {
        if (baseWhereEqual == null) {
            baseWhereEqual = new HashMap<>();
        }
        baseWhereEqual.put(column, value);
    }

    public void putBaseWhereNotEqual(String column, String value) {
        if (baseWhereNotEqual == null) {
            baseWhereNotEqual = new HashMap<>();
        }
        baseWhereNotEqual.put(column, value);
    }

    public void putBaseWhereLike(String column, String value) {
        if (baseWhereLike == null) {
            baseWhereLike = new HashMap<>();
        }
        baseWhereLike.put(column, value);
    }


    public void putBaseWhereIn(String column, List<String> value) {
        if (this.baseWhereIn == null) {
            this.baseWhereIn = new HashMap<>();
        }
        this.baseWhereIn.put(column, value);
    }

    public void putBaseWhereNull(String value) {
        if (baseWhereNull == null) {
            baseWhereNull = new ArrayList<>();
        }
        baseWhereNull.add(value);
    }

    public void putBaseWhereNotNull(String value) {
        if (baseWhereNotNull == null) {
            baseWhereNotNull = new ArrayList<>();
        }
        baseWhereNotNull.add(value);
    }

    public void putBaseWhereLessThan(String column, String value) {
        if (baseWhereLessThan == null) {
            baseWhereLessThan = new HashMap<>();
        }
        baseWhereLessThan.put(column, value);
    }

    public void putBaseWhereGreaterThan(String column, String value) {
        if (baseWhereGreaterThan == null) {
            baseWhereGreaterThan = new HashMap<>();
        }
        baseWhereGreaterThan.put(column, value);
    }

    public void putBaseWhereEqualLessThan(String column, String value) {
        if (baseWhereEqualLessThan == null) {
            baseWhereEqualLessThan = new HashMap<>();
        }
        baseWhereEqualLessThan.put(column, value);
    }

    public void putBaseWhereEqualGreaterThan(String column, String value) {
        if (baseWhereEqualGreaterThan == null) {
            baseWhereEqualGreaterThan = new HashMap<>();
        }
        baseWhereEqualGreaterThan.put(column, value);
    }

    public String getBaseTable() {
        if (baseTable != null) {
            return baseTable;
        }
        else {
            throw new RuntimeException("baseTable must not be null");
        }
    }

    public void setBaseTable(String baseTable) {
        this.baseTable = baseTable;
    }

    public Map<String, String> getBaseColumn() {
        return baseColumn != null ? baseColumn : Collections.singletonMap("*", "");
    }

    public void setBaseColumn(Map<String, String> baseColumn) {
        this.baseColumn = baseColumn;
    }

    public Map<String, String> getBaseInnerJoin() {
        return baseInnerJoin != null ? baseInnerJoin : Collections.emptyMap();
    }

    public void setBaseInnerJoin(Map<String, String> baseInnerJoin) {
        this.baseInnerJoin = baseInnerJoin;
    }

    public Map<String, String> getBaseLeftJoin() {
        return baseLeftJoin != null ? baseLeftJoin : Collections.emptyMap();
    }

    public void setBaseLeftJoin(Map<String, String> baseLeftJoin) {
        this.baseLeftJoin = baseLeftJoin;
    }

    public Map<String, String> getBaseRightJoin() {
        return baseRightJoin != null ? baseRightJoin : Collections.emptyMap();
    }

    public void setBaseRightJoin(Map<String, String> baseRightJoin) {
        this.baseRightJoin = baseRightJoin;
    }

    public Map<String, String> getBaseFullJoin() {
        return baseFullJoin != null ? baseFullJoin : Collections.emptyMap();
    }

    public void setBaseFullJoin(Map<String, String> baseFullJoin) {
        this.baseFullJoin = baseFullJoin;
    }

    public Map<String, String> getBaseWhereEqual() {
        return baseWhereEqual != null ? baseWhereEqual : Collections.emptyMap();
    }

    public void setBaseWhereEqual(Map<String, String> baseWhereEqual) {
        this.baseWhereEqual = baseWhereEqual;
    }

    public Map<String, String> getBaseWhereNotEqual() {
        return baseWhereNotEqual != null ? baseWhereNotEqual : Collections.emptyMap();
    }

    public void setBaseWhereNotEqual(Map<String, String> baseWhereNotEqual) {
        this.baseWhereNotEqual = baseWhereNotEqual;
    }

    public Map<String, String> getBaseWhereLike() {
        return baseWhereLike != null ? baseWhereLike : Collections.emptyMap();
    }

    public void setBaseWhereLike(Map<String, String> baseWhereLike) {
        this.baseWhereLike = baseWhereLike;
    }

    public Map<String, List<String>> getBaseWhereIn() {
        return baseWhereIn != null ? baseWhereIn : Collections.emptyMap();
    }

    public void setBaseWhereIn(Map<String, List<String>> baseWhereIn) {
        this.baseWhereIn = baseWhereIn;
    }

    public List<String> getBaseWhereNull() {
        return baseWhereNull != null ? baseWhereNull : Collections.emptyList();
    }

    public void setBaseWhereNull(List<String> baseWhereNull) {
        this.baseWhereNull = baseWhereNull;
    }

    public List<String> getBaseWhereNotNull() {
        return baseWhereNotNull != null ? baseWhereNotNull : Collections.emptyList();
    }

    public void setBaseWhereNotNull(List<String> baseWhereNotNull) {
        this.baseWhereNotNull = baseWhereNotNull;
    }

    public Map<String, String> getBaseWhereLessThan() {
        return baseWhereLessThan != null ? baseWhereLessThan : Collections.emptyMap();
    }

    public void setBaseWhereLessThan(Map<String, String> baseWhereLessThan) {
        this.baseWhereLessThan = baseWhereLessThan;
    }

    public Map<String, String> getBaseWhereGreaterThan() {
        return baseWhereGreaterThan != null ? baseWhereGreaterThan : Collections.emptyMap();
    }

    public void setBaseWhereGreaterThan(Map<String, String> baseWhereGreaterThan) {
        this.baseWhereGreaterThan = baseWhereGreaterThan;
    }

    public Map<String, String> getBaseWhereEqualLessThan() {
        return baseWhereEqualLessThan != null ? baseWhereEqualLessThan : Collections.emptyMap();
    }

    public void setBaseWhereEqualLessThan(Map<String, String> baseWhereEqualLessThan) {
        this.baseWhereEqualLessThan = baseWhereEqualLessThan;
    }

    public Map<String, String> getBaseWhereEqualGreaterThan() {
        return baseWhereEqualGreaterThan != null ? baseWhereEqualGreaterThan : Collections.emptyMap();
    }

    public void setBaseWhereEqualGreaterThan(Map<String, String> baseWhereEqualGreaterThan) {
        this.baseWhereEqualGreaterThan = baseWhereEqualGreaterThan;
    }

    @Override
    public Integer getPageNum() {
        return pageNum;
    }

    @Override
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public List<String> getSortBy() {
        return sortBy != null ? sortBy.stream().map(sb -> converter.reverse().convert(sb)).collect(Collectors.toList()) : null;
    }

    public void setSortBy(List<String> sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
