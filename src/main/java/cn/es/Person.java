package cn.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther jxy
 * @Date 2020-04-30
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Integer id;
    private String name;
    private Integer age;
    private Integer sex;
    private String address;
    private String phone;
    private String email;
    private String say;
}
