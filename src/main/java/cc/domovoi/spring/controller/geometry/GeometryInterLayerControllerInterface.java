package cc.domovoi.spring.controller.geometry;

import cc.domovoi.spring.controller.OriginalControllerInterface;
import cc.domovoi.spring.geometry.joining.GeometryExporterJoiningInterface;
import cc.domovoi.spring.geometry.joining.GeometryLoaderJoiningInterface;
import cc.domovoi.spring.geometry.model.GeoInterLayerContextLike;
import cc.domovoi.spring.service.geometry.GeometryServiceInterface;
import cc.domovoi.spring.geometry.joining.GeometryServiceJoiningInterface;
import cc.domovoi.spring.utils.BeanMapUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;

public interface GeometryInterLayerControllerInterface<INNER extends GeoInterLayerContextLike<K, OUTER>, OUTER, K> extends OriginalControllerInterface, GeometryServiceInterface<INNER>, GeometryServiceJoiningInterface<INNER>, GeometryExporterJoiningInterface<INNER, OUTER>, GeometryLoaderJoiningInterface<INNER, OUTER> {

    @Override
    default INNER tempInner() {
        return geometryService().tempInner();
    }

    @ApiOperation(value = "find Geometry", notes = "")
    @RequestMapping(
            value = "find",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    @Override
    default INNER findGeometry(@RequestBody INNER geometry) {
        INNER inner = geometryService().findGeometry(geometry);
        if (Objects.nonNull(inner) && Objects.isNull(inner.getOuter())) {
            OUTER outer = exporter().exportGeometry(inner);
            inner.setOuter(outer);
        }
        return inner;
    }

    @ApiOperation(value = "add Geometry", notes = "")
    @RequestMapping(
            value = "add",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    @Override
    default Integer addGeometry(@RequestBody INNER geometry) {
        if (Objects.nonNull(geometry) && Objects.nonNull(geometry.getOuter())) {
            INNER inner = loader().loadGeometry(geometry.getOuter());
            return geometryService().addGeometry(BeanMapUtils.copyPropertyIgnoreNull(geometry, inner));
        }
        else {
            return -1;
        }
    }

    @ApiOperation(value = "delete Geometry", notes = "")
    @RequestMapping(
            value = "delete",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    @Override
    default Integer deleteGeometry(@RequestBody INNER geometry) {
        return geometryService().deleteGeometry(geometry);
    }

    @ApiOperation(value = "update Geometry", notes = "")
    @RequestMapping(
            value = "update",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    @Deprecated
    @Override
    default Integer updateGeometry(INNER geometry) {
        deleteGeometry(geometry);
        return addGeometry(geometry);
    }

    @ApiOperation(value = "update Geometry List", notes = "")
    @RequestMapping(
            value = "findList",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    @Override
    default List<INNER> findGeometryList(@RequestBody INNER geometry) {
        List<INNER> innerList = geometryService().findGeometryList(geometry);
        innerList.forEach(inner -> {
            if (Objects.isNull(inner.getOuter())) {
                OUTER outer = exporter().exportGeometry(inner);
                inner.setOuter(outer);
            }
        });
        return innerList;
    }

    @ApiOperation(value = "check whether geometry exists", notes = "")
    @RequestMapping(
            value = "check",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    @Override
    default Boolean checkGeometryExists(INNER geometry) {
        return geometryService().checkGeometryExists(geometry);
    }
}
