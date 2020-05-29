package com.eminem.weibo.api;

/**
 * 返回数据为object
 *
 * @author
 * @email
 * @date
 */
public class ResData<T> extends BaseResponse<T> {

    public T result;


    @Override
    public T getResponse() {
        return result;
    }

}
