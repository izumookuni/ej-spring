package cc.domovoi.spring.utils.generalquery.dao;

import cc.domovoi.spring.utils.generalquery.GeneralQueryModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GeneralMapper {

    List<Map<String, Object>> findSimpleBaseList(GeneralQueryModel queryModel);

}
