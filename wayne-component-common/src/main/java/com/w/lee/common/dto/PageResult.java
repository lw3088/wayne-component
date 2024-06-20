package com.w.lee.common.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果集
 * 对mybatis-plus 中对Page进行了重新封装
 *
 */
@Data
public class PageResult<T> implements Serializable {

    @ApiModelProperty("列表集合")
    private List<T> records;

    @ApiModelProperty("总条数")
    private Long total;

    @ApiModelProperty("分页条数")
    private Integer size;

    @ApiModelProperty("当前页")
    private Integer current;

    @ApiModelProperty("总页数")
    private Long pages;

    public PageResult<T> setPageData(Page<?> page){
        this.total = page.getTotal();
        this.size = (int)page.getSize();
        this.current = (int)page.getCurrent();
        this.pages = page.getPages();
        return this;
    }
}
