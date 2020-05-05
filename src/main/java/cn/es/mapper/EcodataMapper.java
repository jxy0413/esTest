package cn.es.mapper;

import cn.es.bean.Ecodata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Auther jxy
 * @Date 2020-04-30
 */
@Mapper
public interface EcodataMapper {
    @Select("select * from ecodata where datanum_id=#{datanumId}")
    public Ecodata get(Long datanumId);
}