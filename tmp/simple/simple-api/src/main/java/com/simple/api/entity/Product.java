package com.simple.api.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * 使用 String 类型的 UUID 作为主键，避免多个 HashMap 之间的 ID 冲突
 */
@Data
public class Product {

    /** 商品唯一标识，使用 UUID 生成 */
    private String id;

    /** 商品名称 */
    private String name;

    /** 商品描述 */
    private String description;

    /** 商品价格，使用 BigDecimal 避免浮点精度问题 */
    private BigDecimal price;

    /** 库存数量 */
    private Integer stock;

    /** 创建时间，由服务端自动填充 */
    private LocalDateTime createTime;

    /** 最后修改时间，每次更新时刷新 */
    private LocalDateTime updateTime;
}
