package com.simple.api.service;

import com.simple.api.entity.Product;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商品服务
 * 使用 ConcurrentHashMap 模拟数据库存储，实现商品增删改查
 * 服务启动时预置演示数据
 */
@Service
public class ProductService {

    /**
     * 商品数据存储容器
     * Key: 商品ID（UUID）
     * Value: 商品实体
     */
    private final Map<String, Product> productStore = new ConcurrentHashMap<>();

    /**
     * 服务初始化时预置演示数据
     * 包含三个示例商品
     */
    @PostConstruct
    public void init() {
        // 预置示例商品 1
        Product p1 = new Product();
        p1.setId(UUID.randomUUID().toString());
        p1.setName("iPhone 15 Pro");
        p1.setDescription("Apple 最新旗舰手机，A17 Pro 芯片");
        p1.setPrice(new BigDecimal("7999.00"));
        p1.setStock(100);
        p1.setCreateTime(LocalDateTime.now());
        p1.setUpdateTime(LocalDateTime.now());
        productStore.put(p1.getId(), p1);

        // 预置示例商品 2
        Product p2 = new Product();
        p2.setId(UUID.randomUUID().toString());
        p2.setName("MacBook Air M3");
        p2.setDescription("轻薄笔记本电脑，M3 芯片，15 英寸");
        p2.setPrice(new BigDecimal("10499.00"));
        p2.setStock(50);
        p2.setCreateTime(LocalDateTime.now());
        p2.setUpdateTime(LocalDateTime.now());
        productStore.put(p2.getId(), p2);

        // 预置示例商品 3
        Product p3 = new Product();
        p3.setId(UUID.randomUUID().toString());
        p3.setName("AirPods Pro 2");
        p3.setDescription("主动降噪无线耳机，USB-C 充电盒");
        p3.setPrice(new BigDecimal("1799.00"));
        p3.setStock(200);
        p3.setCreateTime(LocalDateTime.now());
        p3.setUpdateTime(LocalDateTime.now());
        productStore.put(p3.getId(), p3);
    }

    /**
     * 分页查询商品列表，支持按商品名模糊搜索
     *
     * @param name    商品名（可选，模糊匹配）
     * @param pageNum 页码（从 1 开始）
     * @param pageSize 每页条数
     * @return 分页结果
     */
    public com.simple.api.entity.common.PageResult<Product> page(String name, int pageNum, int pageSize) {
        List<Product> all = new ArrayList<>(productStore.values());
        // 按商品名模糊过滤
        if (name != null && !name.isBlank()) {
            String keyword = name.toLowerCase();
            all = all.stream()
                    .filter(p -> p.getName().toLowerCase().contains(keyword))
                    .toList();
        }
        long total = all.size();
        // 分页截取
        int fromIndex = Math.min((pageNum - 1) * pageSize, all.size());
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        List<Product> records = all.subList(fromIndex, toIndex);
        return new com.simple.api.entity.common.PageResult<>(total, records);
    }

    /**
     * 查询所有商品列表
     */
    public List<Product> list() {
        return new ArrayList<>(productStore.values());
    }

    /**
     * 根据 ID 查询商品
     *
     * @param id 商品ID
     * @return 商品实体，不存在时抛出异常
     */
    public Product getById(String id) {
        Product product = productStore.get(id);
        if (product == null) {
            throw new IllegalArgumentException("商品不存在：" + id);
        }
        return product;
    }

    /**
     * 创建新商品
     * 自动生成 UUID 作为 ID，并填充创建时间
     *
     * @param product 商品信息（不含 ID 和时间字段）
     * @return 创建完成后的商品实体
     */
    public Product create(Product product) {
        String id = UUID.randomUUID().toString();
        product.setId(id);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        productStore.put(id, product);
        return product;
    }

    /**
     * 更新商品信息
     * 可更新 name、description、price、stock 字段
     *
     * @param id      商品ID
     * @param product 新的商品信息
     * @return 更新后的商品实体
     */
    public Product update(String id, Product product) {
        Product existing = getById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setUpdateTime(LocalDateTime.now());
        return existing;
    }

    /**
     * 删除商品
     *
     * @param id 商品ID
     */
    public void delete(String id) {
        if (productStore.remove(id) == null) {
            throw new IllegalArgumentException("商品不存在：" + id);
        }
    }
}
