package cn.es.controller;

import cn.es.EsDemoTest;
import cn.es.bean.Ecodata;
import cn.es.common.ResultModel;
import cn.es.mapper.EcodataMapper;
import cn.es.service.EcodataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther jxy
 * @Date 2020-04-30
 */
@RestController
@RequestMapping("/ecodata")
public class EcodataController {
    @Autowired
    private EcodataService ecodataService;
    @Autowired
    private EcodataMapper ecodataMapper;

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

    @ApiOperation("测试100")
    @GetMapping("/getOne")
    public void getOne(){
        long time1 = System.currentTimeMillis();
        Ecodata ecodata = ecodataMapper.get(11213939L);
        System.out.println(ecodata);
        long time2 = System.currentTimeMillis();
        Long time =  ((time2 - time1));
        System.out.println("执行了："+time+"毫秒！");
    }

    @ApiOperation("测试mysql")
    @GetMapping("/getList/{stationId}/{datatypeId}")
    public void getList(@PathVariable Integer stationId,@PathVariable Integer datatypeId){
        long time1 = System.currentTimeMillis();
        List<Ecodata> list =ecodataMapper.getList(stationId,datatypeId);
        System.out.println(list);
        long time2 = System.currentTimeMillis();
        Long time =  ((time2 - time1));
        System.out.println("执行了："+time+"毫秒！");
    }

}