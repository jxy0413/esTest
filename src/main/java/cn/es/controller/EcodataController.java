package cn.es.controller;

import cn.es.bean.Ecodata;
import cn.es.common.ResultModel;
import cn.es.service.EcodataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther jxy
 * @Date 2020-04-30
 */
@RestController
@RequestMapping("/ecodata")
public class EcodataController {
    @Autowired
    private EcodataService ecodataService;

//    @GetMapping("/get/{datanumId}")
//    public ResultModel get(@PathVariable("datanumId") Long datanumId) throws Exception{
//        Ecodata ecodata =ecodataService.get(datanumId);
//        return ResultModel.ok(ecodata);
//    }

    @ApiOperation("测试100")
    @GetMapping("/get")
    public void getji() throws Exception{
        ecodataService.get();
    }
}