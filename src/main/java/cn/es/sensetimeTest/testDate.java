package cn.es.sensetimeTest;

import org.junit.Test;

import java.util.Date;

/**
 * @Auther jxy
 * @Date 2020-05-09
 */
public class testDate {
    @Test
    public void test(){
        Date date = new Date();
        long time = date.getTime();
        long tim1=time/1000/24/3600;
        System.out.println(tim1);
    }
}
