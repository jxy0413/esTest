package cn.es.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;


/**
 * @Auther jxy
 * @Date 2020-04-22
 */
@Configuration
public class MyCheConfig {

    @Bean("myGenerator")
    public KeyGenerator keyGenerator() {
      return new KeyGenerator(){
           @Override
           public Object generate(Object target, Method method, Object... params) {
               return method.getName()+"["+ Arrays.asList(params).toString()+"]";
           }
       };
    }
}
