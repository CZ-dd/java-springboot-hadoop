package com.czdd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean {
    /*
     * @description:分页查询返回结果封装类
     */
    private Long total; // 总数
    private List rows;  // 该页内容
}
