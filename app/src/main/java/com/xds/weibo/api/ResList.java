package com.xds.weibo.api;

import java.util.Collections;
import java.util.List;

/**
 * 返回数据为list
 *
 * @author
 * @email
 * @date
 */
public class ResList<T> extends BaseResponse<List<T>> {
    public ListData<T> result;

    @Override
    public List<T> getResponse() {
        if (null == result || null == result.list) {
            return Collections.emptyList();
        }
        return result.list;
    }

}
