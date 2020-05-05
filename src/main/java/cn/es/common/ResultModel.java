package cn.es.common;

import lombok.Data;

/**
 * @Auther jxy
 * @Date 2020-04-19
 */
@Data
public class ResultModel<E> {
    private String code;
    private String msg;
    private E data;

    public ResultModel<E> setData(E data) {
        this.data = data;
        return this;
    }

    public static <E> ResultModel<E> ok(E data) {
        ResultModel<E> result = new ResultModel<E>();
        result.code = "200";
        result.msg = "success";
        result.data = data;
        return result;
    }

    public static <E> ResultModel<E> error(String code, String error) {
        ResultModel<E> result = new ResultModel<E>();
        result.code = code;
        result.msg = error;
        return result;
    }

    public static <E> ResultModel<E> error(String error) {
        return error("470", error);
    }


}
