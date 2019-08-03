package com.hongkun.finance.user.dto;

import java.io.Serializable;

/**
 * @Description :
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.dto.PrivilgeDTO
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class PrivilgeDTO implements Serializable {

    private java.lang.Integer id;
    private java.lang.String privName;
    private java.lang.String privDesc;
    private java.lang.String privUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrivName() {
        return privName;
    }

    public void setPrivName(String privName) {
        this.privName = privName;
    }

    public String getPrivDesc() {
        return privDesc;
    }

    public void setPrivDesc(String privDesc) {
        this.privDesc = privDesc;
    }

    public String getPrivUrl() {
        return privUrl;
    }

    public void setPrivUrl(String privUrl) {
        this.privUrl = privUrl;
    }
}
