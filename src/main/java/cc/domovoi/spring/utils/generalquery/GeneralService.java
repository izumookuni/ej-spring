package cc.domovoi.spring.utils.generalquery;

import cc.domovoi.spring.utils.generalquery.dao.GeneralMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GeneralService {

    private GeneralMapper generalMapper;

    public GeneralService(GeneralMapper generalMapper) {
        this.generalMapper = generalMapper;
    }

    public List<Map<String, Object>> findSimpleBaseList(GeneralQueryModel queryModel) {
        return generalMapper.findSimpleBaseList(queryModel);
    }
}
