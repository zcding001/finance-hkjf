package com.hongkun.finance.user.dto;

import java.util.List;

/**
 * @Description : 菜单和权限关系的dto
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.dto.MenuPriDTO
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class MenuPriDTO {

    private List<Integer> menuIds;

    private List<Integer>  priIds;


    public MenuPriDTO() {
    }



    public MenuPriDTO(List<Integer> menuIds, List<Integer> priIds) {
        this.menuIds = menuIds;
        this.priIds = priIds;
    }

    public List<Integer> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Integer> menuIds) {
        this.menuIds = menuIds;
    }

    public List<Integer> getPriIds() {
        return priIds;
    }

    public void setPriIds(List<Integer> priIds) {
        this.priIds = priIds;
    }
}
