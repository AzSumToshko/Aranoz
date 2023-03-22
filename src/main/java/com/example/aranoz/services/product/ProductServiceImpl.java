package com.example.aranoz.services.product;

import com.example.aranoz.domain.dtoS.view.Product.AddToCartVM;
import com.example.aranoz.domain.dtoS.model.Product.CreateProductFormDto;
import com.example.aranoz.domain.dtoS.model.Product.UpdateProductFormDto;
import com.example.aranoz.domain.dtoS.view.User.CartProductVM;
import com.example.aranoz.domain.entities.CartItem;
import com.example.aranoz.domain.entities.Product;
import com.example.aranoz.domain.enums.Category;
import com.example.aranoz.repository.ProductRepository;
import com.example.aranoz.services.cartItem.CartItemService;
import com.example.aranoz.services.productCategory.ProductCategoryService;
import com.example.aranoz.services.user.UserService;
import com.example.aranoz.util.ImageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.aranoz.constants.Constants.*;
import static com.example.aranoz.constants.Errors.PRODUCT_NAME_TAKEN_FORMAT;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final CartItemService cartItemService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductCategoryService productCategoryService, ObjectMapper objectMapper, UserService userService, CartItemService cartItemService) {
        this.productRepository = productRepository;
        this.productCategoryService = productCategoryService;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.cartItemService = cartItemService;
    }

    public void createProduct(CreateProductFormDto model) throws Exception {

        if (findByName(model.getName()) != null){
            throw new Exception(String.format(PRODUCT_NAME_TAKEN_FORMAT,model.getName()));
        }

        this.productRepository.saveAndFlush(Product.builder()
                .name(model.getName())
                .depth(model.getDepth())
                .price(model.getPrice())
                .peaces(model.getPeaces())
                .description(model.getDescription())
                .height(model.getHeight())
                .width(model.getWidth())
                .weight(model.getWeight())
                .category(this.productCategoryService.findByCategory(model.getCategory()))
                .qualityChecking(model.getQualityChecking())
                .image(ImageUtil.compressImage(model.getImage().getBytes()))
                .build());
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Transactional
    public byte[] getImage(String name) {
        return ImageUtil.decompressImage(
                this.productRepository.findFirstByName(name).getImage());
    }

    @Override
    public void removeProductById(String id) {
        this.productRepository.deleteById(id);
    }

    @Override
    public Product findById(String id) {
        return this.productRepository.findById(id).get();
    }

    @Override
    public void updateProduct(UpdateProductFormDto model) throws Exception {
        Product product = this.productRepository.findById(model.getId()).get();
        if (product == null)
            return;

        if (findByName(model.getName()) != null && !Objects.equals(findByName(model.getName()).getId(), product.getId())){
            throw new Exception(String.format(PRODUCT_NAME_TAKEN_FORMAT,model.getName()));
        }
        product.setName(model.getName());
        product.setCategory(this.productCategoryService.findByCategory(model.getCategory()));
        product.setDepth(model.getDepth());
        product.setHeight(model.getHeight());
        product.setPeaces(model.getPeaces());
        product.setPrice(model.getPrice());
        product.setDescription(model.getDescription());
        product.setQualityChecking(model.getQualityChecking());
        product.setWeight(model.getWeight());
        product.setWidth(model.getWidth());

        if(model.getImage().getSize() > 0){
            product.setImage(ImageUtil.compressImage(model.getImage().getBytes()));
        }

        this.productRepository.saveAndFlush(product);
    }

    private Product findByName(String name) {
        return this.productRepository.findFirstByName(name);
    }

    @Override
    public Long getProductsCount() {
        return this.productRepository.count();
    }

    @Override
    public Long[] getAllCategoryCounts() {
        return new Long[]
                {
                    this.productRepository.count(),
                    (long) this.productRepository.findAllByCategory(this.productCategoryService.findByCategory(Category.BED)).size(),
                    (long) this.productRepository.findAllByCategory(this.productCategoryService.findByCategory(Category.SOFA)).size(),
                    (long) this.productRepository.findAllByCategory(this.productCategoryService.findByCategory(Category.CHAIR)).size(),
                    (long) this.productRepository.findAllByCategory(this.productCategoryService.findByCategory(Category.TABLE)).size(),
                    (long) this.productRepository.findAllByCategory(this.productCategoryService.findByCategory(Category.CABINET)).size(),
                };
    }

    @Override
    public List<Product> findAllByCategory(String category) {
        switch (category){
            case PRODUCT_CATEGORY_ALL: return this.productRepository.findAll();
            case PRODUCT_CATEGORY_BED: return this.productRepository.findAllByCategory(this.productCategoryService.findByCategory(Category.BED));
            case PRODUCT_CATEGORY_SOFA: return this.productRepository.findAllByCategory(this.productCategoryService.findByCategory(Category.SOFA));
            case PRODUCT_CATEGORY_CHAIR: return this.productRepository.findAllByCategory(this.productCategoryService.findByCategory(Category.CHAIR));
            case PRODUCT_CATEGORY_TABLE: return this.productRepository.findAllByCategory(this.productCategoryService.findByCategory(Category.TABLE));
            case PRODUCT_CATEGORY_CABINET: return this.productRepository.findAllByCategory(this.productCategoryService.findByCategory(Category.CABINET));
            default: return this.productRepository.findAllByNameContains(category);
        }
    }

    @Override
    public List<Product> findAllByCategoryAndPriceBetween(String category, Double min, Double max) {
        switch (category){
            case PRODUCT_CATEGORY_ALL: return this.productRepository.findAllByPriceBetween(min,max);
            case PRODUCT_CATEGORY_BED: return this.productRepository.findAllByCategoryAndPriceBetween(this.productCategoryService.findByCategory(Category.BED), min,max);
            case PRODUCT_CATEGORY_SOFA: return this.productRepository.findAllByCategoryAndPriceBetween(this.productCategoryService.findByCategory(Category.SOFA), min,max);
            case PRODUCT_CATEGORY_CHAIR: return this.productRepository.findAllByCategoryAndPriceBetween(this.productCategoryService.findByCategory(Category.CHAIR), min,max);
            case PRODUCT_CATEGORY_TABLE: return this.productRepository.findAllByCategoryAndPriceBetween(this.productCategoryService.findByCategory(Category.TABLE), min,max);
            case PRODUCT_CATEGORY_CABINET: return this.productRepository.findAllByCategoryAndPriceBetween(this.productCategoryService.findByCategory(Category.CABINET), min,max);
            default: return this.productRepository.findAllByNameContainsAndPriceBetween(category, min,max);
        }
    }

    @Override
    public void addToCart(HttpServletResponse response, AddToCartVM model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && !authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ANONYMOUS")){
            cartItemService.addToCart(CartItem.builder()
                    .productId(model.getId())
                    .quantity(model.getQuantity())
                    .userId(this.userService.getUserByEmail(authentication.getName()).getId())
                    .build());
        }else{
            final Cookie cookie = new Cookie(model.getId(),model.getQuantity().toString());

            cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
            cookie.setPath(DEFAULT_COOKIE_PATH);
            response.addCookie(cookie);
        }

    }

    @Override
    public List<CartProductVM> getCookieProducts(Cookie[] cookies) {
        List<CartProductVM> cart = new ArrayList<>();
        for (Cookie cookie : cookies) {
            try{
                Integer.parseInt(cookie.getValue());
                cart.add(CartProductVM.builder()
                        .product(findById(cookie.getName()))
                        .quantity(Long.valueOf(cookie.getValue()))
                        .total(findById(cookie.getName()).getPrice() * Long.parseLong(cookie.getValue()))
                        .build());
            }catch (Exception ignored){

            }
        }
        return cart;
    }

    @Override
    public Double getTotalPriceOfCartProducts(List<CartProductVM> cookieProducts) {
        Double sum = 0.0;
        for (CartProductVM cookieProduct : cookieProducts) {
            sum += cookieProduct.getTotal();
        }
        return sum;
    }

    @Override
    public String convertCartProductListToString(List<CartProductVM> cookieProducts) throws JsonProcessingException {
        return objectMapper.writeValueAsString(cookieProducts);
    }

    @Override
    public List<CartProductVM> convertStringToCartProductList(String stringCookies) throws JsonProcessingException {
        return objectMapper.readValue(stringCookies, new TypeReference<List<CartProductVM>>(){});
    }

}