package com.zinum.service.Impl;

import com.zinum.dto.request.CreateProductRequest;
import com.zinum.exception.ProductException;
import com.zinum.model.Category;
import com.zinum.model.Product;
import com.zinum.model.Seller;
import com.zinum.repository.CategoryRepository;
import com.zinum.repository.ProductRepository;
import com.zinum.service.ProductService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public Product createProduct(CreateProductRequest request, Seller seller) {
        Category category1 = categoryRepository.findByCategoryId(request.getCategory());
        if(category1 == null){
            Category category = new Category();
            category.setName(request.getCategory());
            category.setCategoryId(request.getCategory());
            category.setLevel(1);
            category1 = categoryRepository.save(category);
        }

        Category category2 = categoryRepository.findByCategoryId(request.getCategory2());
        if(category2 == null){
            Category category = new Category();
            category.setName(request.getCategory2());
            category.setCategoryId(request.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);
            category2 = categoryRepository.save(category);
        }

        Category category3 = categoryRepository.findByCategoryId(request.getCategory3());
        if(category3 == null){
            Category category = new Category();
            category.setName(request.getCategory3());
            category.setCategoryId(request.getCategory3());
            category.setLevel(3);
            category.setParentCategory(category2);
            category3 = categoryRepository.save(category);
        }
        int discountPercent =  calculateDiscountPercent(request.getMrpPrice(), request.getSellingPrice());

        Product product = new Product();
        product.setCategory(category3);
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setMrpPrice(request.getMrpPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setColor(request.getColor());
        product.setImages(request.getImages());
        product.setSizes(request.getSizes());
        product.setSeller(seller);
        product.setDiscountPercent(discountPercent);

        return productRepository.save(product);
    }

    private int calculateDiscountPercent(int mrpPrice, int sellingPrice) {
        if(mrpPrice < 0){
            throw new IllegalArgumentException("MRP price cannot be negative");
        }

        if(sellingPrice < 0){
            throw new IllegalArgumentException("Selling price cannot be negative");
        }

        if(sellingPrice > mrpPrice){
            throw new IllegalArgumentException("Selling price cannot be greater than MRP price");
        }

        return (int) ((mrpPrice - sellingPrice) * 100 / mrpPrice);
    }

    @Override
    public void deleteProduct(Long id) throws ProductException {
        productRepository.deleteById(id);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found"));

        if (product.getTitle() != null) {
            existingProduct.setTitle(product.getTitle());
        }
        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        if (product.getMrpPrice() >= 0) {
            existingProduct.setMrpPrice(product.getMrpPrice());
        }
        if (product.getSellingPrice() >= 0) {
            existingProduct.setSellingPrice(product.getSellingPrice());
        }
        if (product.getColor() != null) {
            existingProduct.setColor(product.getColor());
        }
        if (product.getImages() != null) {
            existingProduct.setImages(product.getImages());
        }
        if (product.getSizes() != null) {
            existingProduct.setSizes(product.getSizes());
        }

        return productRepository.save(existingProduct);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        return productRepository.findById(id).orElseThrow(() -> new ProductException("Product not with id " + id));
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if (colors != null && !colors.isEmpty()) {
                CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("color"));
                for (String color : colors.split(",")) {
                    inClause.value(color.trim());
                }
                predicates.add(inClause);
            }
            if (sizes != null && !sizes.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("sizes"), sizes));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if (minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }
            if (stock != null) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "price_low":
                    pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").ascending());
                    break;
                case "price_high":
                    pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").descending());
                    break;
                default:
                    pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10);
            }
        } else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10);
        }

        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        return productRepository.findAllBySellerId(sellerId);
    }
}
