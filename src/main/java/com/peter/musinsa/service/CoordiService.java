package com.peter.musinsa.service;

import com.peter.musinsa.domain.Brand;
import com.peter.musinsa.domain.Category;
import com.peter.musinsa.domain.Product;
import com.peter.musinsa.dto.response.BrandLowestTotalResponse;
import com.peter.musinsa.dto.response.CategoryLowestPricesResponse;
import com.peter.musinsa.dto.response.CategoryMinMaxPriceResponse;
import com.peter.musinsa.exception.BusinessException;
import com.peter.musinsa.exception.ErrorCode;
import com.peter.musinsa.repository.BrandRepository;
import com.peter.musinsa.repository.CategoryRepository;
import com.peter.musinsa.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoordiService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    public CategoryLowestPricesResponse findLowestPricesByCategory() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryLowestPricesResponse.CategoryPrice> categoryPrices = new ArrayList<>();

        long totalPrice = 0;

        for (Category category : categories) {
            Optional<Product> productOpt = productRepository.findMinPriceProductByCategory(category);

            Product product = productOpt.orElseThrow(() ->
                new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, String.format("'%s' 카테고리에 상품이 없습니다.", category.getName()))
            );

            categoryPrices.add(new CategoryLowestPricesResponse.CategoryPrice(
                category.getName(),
                product.getBrand().getName(),
                product.getPrice()
            ));

            totalPrice += product.getPrice();
        }

        return new CategoryLowestPricesResponse(categoryPrices, totalPrice);
    }


    public BrandLowestTotalResponse findLowestTotalPriceBrand() {
        List<Brand> brands = brandRepository.findAll();
        if (brands.isEmpty()) {
            throw new BusinessException(ErrorCode.BRAND_NOT_FOUND);
        }

        Brand lowestTotalBrand = null;
        long lowestTotal = Long.MAX_VALUE;
        List<Object[]> lowestCategoryPrices = null;

        for (Brand brand : brands) {
            List<Object[]> categoryPrices = productRepository.findCategoryMinPricesByBrand(brand);

            // TODO : size가 아닌, 카테고리 목록으로 비교한다
            if (categoryPrices.size() != categoryRepository.findAll().size()) {
                continue;
            }

            long totalPrice = categoryPrices.stream()
                .mapToLong(price -> (Long) price[1])
                .sum();

            if (totalPrice < lowestTotal) {
                lowestTotal = totalPrice;
                lowestTotalBrand = brand;
                lowestCategoryPrices = categoryPrices;
            }
        }

        if (lowestTotalBrand == null) {
//            lowestTotalBrand = null
            throw new BusinessException(ErrorCode.NO_BRAND_WITH_CATEGORIES);
        }

        List<BrandLowestTotalResponse.CategoryPrice> categoryPrices = lowestCategoryPrices.stream()
            .map(price -> new BrandLowestTotalResponse.CategoryPrice(
                (String) price[0],
                (Long) price[1]
            ))
            .collect(Collectors.toList());

        return new BrandLowestTotalResponse(
            lowestTotalBrand.getName(),
            categoryPrices,
            lowestTotal
        );
    }


    public CategoryMinMaxPriceResponse findMinMaxPricesByCategory(String categoryName) {

        Optional<Category> categoryOpt = categoryRepository.findByName(categoryName);
        Category category = categoryOpt.orElseThrow(() ->
            new BusinessException(ErrorCode.CATEGORY_NOT_FOUND)
        );

        // 카테고리별 최고, 최저가 상품 조회
        List<Product> minPriceProducts = productRepository.findAllMinPriceProductsByCategory(category);
        List<Product> maxPriceProducts = productRepository.findAllMaxPriceProductsByCategory(category);

        if (minPriceProducts.isEmpty() || maxPriceProducts.isEmpty()) {
            throw new BusinessException(
                ErrorCode.PRODUCT_NOT_FOUND,
                String.format("카테고리 '%s'에 해당하는 제품이 없습니다.", categoryName)
            );
        }

        List<CategoryMinMaxPriceResponse.BrandPrice> lowestPriceProducts = minPriceProducts.stream()
            .map(product -> new CategoryMinMaxPriceResponse.BrandPrice(
                product.getBrand().getName(),
                product.getPrice()
            ))
            .collect(Collectors.toList());

        List<CategoryMinMaxPriceResponse.BrandPrice> highestPriceProducts = maxPriceProducts.stream()
            .map(product -> new CategoryMinMaxPriceResponse.BrandPrice(
                product.getBrand().getName(),
                product.getPrice()
            ))
            .collect(Collectors.toList());

        return new CategoryMinMaxPriceResponse(categoryName, lowestPriceProducts, highestPriceProducts);
    }
}
