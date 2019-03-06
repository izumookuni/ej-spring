package cc.domovoi.spring.test;

import cc.domovoi.spring.test.mapper.*;
import cc.domovoi.spring.test.service.*;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTree;
import cc.domovoi.spring.test.entity.RootEntity;
import org.junit.Test;

import java.util.List;

public class TreeTest {

    @Test
    public void testTree() {
        JoiningDepthTree tree = new JoiningDepthTree();
        tree.put("a").put("b").put("c", new JoiningDepthTree().put("d").put("e", new JoiningDepthTree().put("f").put("g")));
        System.out.println(tree.treeString());
    }

    private RootMapper rootMapper = new RootMapper();
    private MapperA mapperA = new MapperA();
    private MapperB mapperB = new MapperB();
    private MapperC mapperC = new MapperC();
    private MapperD mapperD = new MapperD();
    private MapperE mapperE = new MapperE();
    private MapperF mapperF = new MapperF();

    private ServiceF serviceF = new ServiceF(mapperF);
    private ServiceE serviceE = new ServiceE(mapperE, serviceF);
    private ServiceD serviceD = new ServiceD(mapperD);
    private ServiceC serviceC = new ServiceC(mapperC, serviceD, serviceE);
    private ServiceB serviceB = new ServiceB(mapperB);
    private ServiceA serviceA = new ServiceA(mapperA);
    private RootService rootService = new RootService(rootMapper, serviceA, serviceB, serviceC);

    @Test
    public void testJoiningEntityByDepth() {
        List<RootEntity> entityList = rootService.findList(new RootEntity());
        entityList.forEach(e -> System.out.println(e.toString()));
    }

    @Test
    public void testJoinEntityListByTree() {
        List<RootEntity> entityList = rootService.findList(new RootEntity());
        entityList.forEach(e -> System.out.println(e.toString()));
    }



}
