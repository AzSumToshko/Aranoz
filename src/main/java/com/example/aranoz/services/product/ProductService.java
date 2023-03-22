package com.example.aranoz.services.product;

import com.example.aranoz.domain.dtoS.view.Product.AddToCartVM;
import com.example.aranoz.domain.dtoS.model.Product.CreateProductFormDto;
import com.example.aranoz.domain.dtoS.model.Product.UpdateProductFormDto;
import com.example.aranoz.domain.dtoS.view.User.CartProductVM;
import com.example.aranoz.domain.entities.Product;
import com.example.aranoz.services.init.DataBaseInitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ProductService{
    void createProduct(CreateProductFormDto model) throws Exception;
    List<Product> getAllProducts();
    byte[] getImage(String name);
    void removeProductById(String id);
    Product findById(String id);
    void updateProduct(UpdateProductFormDto model) throws Exception;
    Long getProductsCount();
    Long[] getAllCategoryCounts();
    List<Product> findAllByCategory(String category);
    List<Product> findAllByCategoryAndPriceBetween(String category, Double min, Double max);
    void addToCart(HttpServletResponse response,AddToCartVM model);
    List<CartProductVM> getCookieProducts(Cookie[] cookies);
    Double getTotalPriceOfCartProducts(List<CartProductVM> cookieProducts);
    List<CartProductVM> convertStringToCartProductList(String stringCookies) throws JsonProcessingException;
    String convertCartProductListToString(List<CartProductVM> cookieProducts) throws JsonProcessingException;
}
