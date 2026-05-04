package com.simple.api.controller;

import com.simple.api.common.Result;
import com.simple.api.entity.Product;
import com.simple.api.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品控制器
 * 提供商品增删改查的 RESTful API
 *
 * 所有接口均受 AuthInterceptor 保护，需要先登录获取有效 Session Cookie
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 查询所有商品列表
     * GET /api/product/list
     */
    @GetMapping("/list")
    public Result<List<Product>> list() {
        return Result.success(productService.list());
    }

    /**
     * 根据 ID 查询商品详情
     * GET /api/product/{id}
     */
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable String id) {
        return Result.success(productService.getById(id));
    }

    /**
     * 创建新商品
     * POST /api/product
     *
     * 请求体：{ "name": "新商品", "description": "描述", "price": 99.99, "stock": 10 }
     */
    @PostMapping
    public Result<Product> create(@RequestBody Product product) {
        return Result.success(productService.create(product));
    }

    /**
     * 更新商品信息
     * PUT /api/product/{id}
     *
     * 请求体：{ "name": "新名称", "price": 199.99, "stock": 50 }
     * 只更新提供的字段，未提供的字段保持不变
     */
    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable String id, @RequestBody Product product) {
        return Result.success(productService.update(id, product));
    }

    /**
     * 删除商品
     * DELETE /api/product/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        productService.delete(id);
        return Result.success();
    }
}
