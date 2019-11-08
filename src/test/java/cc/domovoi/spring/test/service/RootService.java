package cc.domovoi.spring.test.service;

import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTree;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTreeLike;
import cc.domovoi.spring.service.BaseRetrieveJoiningServiceInterface;
import cc.domovoi.spring.test.entity.RootEntity;
import cc.domovoi.spring.test.mapper.RootMapper;

import java.util.HashMap;
import java.util.Map;

public class RootService implements BaseRetrieveJoiningServiceInterface<RootEntity, RootMapper> {

    private RootMapper mapper;

    private ServiceA serviceA;

    private ServiceB serviceB;

    private ServiceC serviceC;

    @Override
    public Integer depth() {
        return -1;
    }

    @Override
    public JoiningDepthTreeLike depthTree() {
        return new JoiningDepthTree().put("a").put("c", new JoiningDepthTree().put("d").put("e", new JoiningDepthTree().put("f")));
    }

    public RootService(RootMapper mapper, ServiceA serviceA, ServiceB serviceB, ServiceC serviceC) {
        this.mapper = mapper;
        this.serviceA = serviceA;
        this.serviceB = serviceB;
        this.serviceC = serviceC;
    }

    @Override
    public Map<String, BaseRetrieveJoiningServiceInterface> joiningService() {
        Map<String, BaseRetrieveJoiningServiceInterface> joiningService = new HashMap<>();
        joiningService.put("a", serviceA);
        joiningService.put("b", serviceB);
        joiningService.put("c", serviceC);
        return joiningService;
    }

    @Override
    public RootMapper mvcMapper() {
        return mapper;
    }
}
