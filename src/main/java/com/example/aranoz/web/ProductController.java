package com.example.aranoz.web;

import com.example.aranoz.domain.dtoS.view.Product.AddToCartVM;
import com.example.aranoz.domain.dtoS.view.Product.ExtractProductVM;
import com.example.aranoz.domain.dtoS.view.Product.ProductSearchVM;
import com.example.aranoz.domain.entities.Product;
import com.example.aranoz.services.comment.CommentService;
import com.example.aranoz.services.product.ProductService;
import com.example.aranoz.util.ImageUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Product")
public class ProductController extends BaseController {
    private final ProductService productService;
    private final CommentService commentService;

    @Autowired
    public ProductController(ProductService productService, CommentService commentService) {
        this.productService = productService;
        this.commentService = commentService;
    }


    //-------------DATA DISPLAY----------------//


    @GetMapping("/search")
    public ModelAndView getSearch(String search){
        return super.redirect("/Product/" + search);
    }

    @GetMapping("/{category}")
    public ModelAndView getProducts(@PathVariable("category") String category){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("current", category);
        modelAndView.addObject("imgUtil", new ImageUtil());
        modelAndView.addObject("products",this.productService.findAllByCategory(category));
        modelAndView.addObject("productsCount", this.productService.findAllByCategory(category).size());
        modelAndView.addObject("categoryItemsCounts",this.productService.getAllCategoryCounts());
        return super.view("/Product/products", modelAndView);
    }

    @GetMapping(value = "/fetch", produces = "application/json")
    @ResponseBody
    public Object fetchData(@RequestParam("category") String category){
        List<Product> products = this.productService.findAllByCategory(category);

        return products.stream().map(x -> ExtractProductVM.builder()
                .name(x.getName())
                .depth(x.getDepth())
                .image(x.convertImageToString())
                .peaces(x.getPeaces())
                .price(x.getPrice())
                .qualityChecking(x.getQualityChecking())
                .weight(x.getWeight())
                .width(x.getWidth())
                .category(x.getCategory())
                .description(x.getDescription())
                .height(x.getHeight())
                .build()).toList();
    }

    @PostMapping("/{category}")
    public ModelAndView postProducts(@PathVariable("category") String category,
                                     ProductSearchVM model,
                                     ModelAndView modelAndView){
        modelAndView.addObject("current", category);
        modelAndView.addObject("products",this.productService.findAllByCategoryAndPriceBetween(category,model.getMin(),model.getMax()));
        modelAndView.addObject("imgUtil", new ImageUtil());
        modelAndView.addObject("productsCount", this.productService.findAllByCategoryAndPriceBetween(category,model.getMin(),model.getMax()).size());
        modelAndView.addObject("categoryItemsCounts",this.productService.getAllCategoryCounts());
        return super.view("/Product/products", modelAndView);
    }

    @GetMapping("/details/{id}")
    public ModelAndView getDetails(@PathVariable("id") String id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("product",this.productService.findById(id));
        modelAndView.addObject("imgUtil", new ImageUtil());
        modelAndView.addObject("comments", this.commentService.getAllCommentsForProduct(id));
        return super.view("Product/details",modelAndView);
    }


    //-------------DATA MANIPULATION----------------//


    @PostMapping("/addToCart")
    public ModelAndView postAddToCart(HttpServletResponse response,
                                      AddToCartVM model){
        this.productService.addToCart(response,model);
        return super.redirect("/Product/all");
    }
}
