package com.example.aranoz.web;

import com.example.aranoz.domain.dtoS.model.Product.CreateProductFormDto;
import com.example.aranoz.domain.dtoS.model.User.CreateUserFormDto;
import com.example.aranoz.domain.dtoS.model.Product.UpdateProductFormDto;
import com.example.aranoz.domain.dtoS.model.User.UpdateUserFormDto;
import com.example.aranoz.services.product.ProductService;
import com.example.aranoz.services.productCategory.ProductCategoryService;
import com.example.aranoz.services.user.UserService;
import com.example.aranoz.services.userRole.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/Admin")
public class AdminController extends BaseController{
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public AdminController(ProductService productService, ProductCategoryService productCategoryService, UserService userService, UserRoleService userRoleService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }


    //-------------DATA MANIPULATION----------------//


    @GetMapping("/panel")
    public ModelAndView getAdminPanel(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("users",this.userService.getAllUsers());
        modelAndView.addObject("products",this.productService.getAllProducts());
        return super.view("Admin/adminPanel", modelAndView);
    }


    //-------------PRODUCT ENTITY MANIPULATION----------------//


    @GetMapping("/createProduct")
    public ModelAndView getCreateProduct(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categories",this.productCategoryService.getAll());
        return super.view("Admin/createProduct",modelAndView);
    }

    @PostMapping("/createProduct")
    public ModelAndView postCreateProduct(@Valid @ModelAttribute(name = "createProductFormDto") CreateProductFormDto createProductFormDto,
                                          BindingResult bindingResult,
                                          ModelAndView modelAndView){
        if (bindingResult.hasErrors()){
            return super.view("Admin/createProduct",
                    modelAndView.addObject("categories",this.productCategoryService.getAll())
                            .addObject("createProductFormDto",createProductFormDto));
        }

        if (createProductFormDto.getImage().isEmpty()){
            return super.view("Admin/createProduct",
                    modelAndView.addObject("categories",this.productCategoryService.getAll())
                            .addObject("createProductFormDto",createProductFormDto)
                            .addObject("ImageIsEmpty", true));
        }

        try{
            productService.createProduct(createProductFormDto);
        }catch (Exception e){
            return super.view("Admin/createProduct",
                    modelAndView.addObject("categories",this.productCategoryService.getAll())
                            .addObject("updateProductFormDto",createProductFormDto)
                            .addObject("IsNameTaken",e.getMessage()));
        }

        return super.redirect("/Admin/panel");
    }

    @GetMapping("/updateProduct/{id}")
    public ModelAndView getUpdateProduct(@PathVariable("id") String id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("updateProductFormDto",this.productService.findById(id));
        modelAndView.addObject("categories",this.productCategoryService.getAll());
        return super.view("Admin/updateProduct",modelAndView);
    }

    @PostMapping("/updateProduct/{id}")
    public ModelAndView postUpdateProduct(@Valid @ModelAttribute(name = "updateProductFormDto") UpdateProductFormDto updateProductFormDto,
                                          BindingResult bindingResult,
                                          ModelAndView modelAndView){
        if (bindingResult.hasErrors()){
            return super.view("Admin/updateProduct",
                    modelAndView.addObject("categories",this.productCategoryService.getAll())
                            .addObject("updateProductFormDto",updateProductFormDto));
        }

        try{
            productService.updateProduct(updateProductFormDto);
        }catch (Exception e){
            return super.view("Admin/updateProduct",
                    modelAndView.addObject("categories",this.productCategoryService.getAll())
                            .addObject("updateProductFormDto",updateProductFormDto)
                            .addObject("IsNameTaken",e.getMessage()));
        }
        return super.redirect("/Admin/panel");
    }

    @GetMapping("/deleteProduct/{id}")
    public ModelAndView getDeleteProduct(@PathVariable("id") String id){
        this.productService.removeProductById(id);
        return super.redirect("/Admin/panel");
    }


    //-------------USER ENTITY MANIPULATION----------------//


    @GetMapping("/createUser")
    public ModelAndView getCreateUser(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roles",this.userRoleService.getAll());
        return super.view("Admin/createUser",modelAndView);
    }

    @PostMapping("/createUser")
    public ModelAndView postCreateUser(@Valid @ModelAttribute(name = "createUserFormDto") CreateUserFormDto createUserFormDto,
                                       BindingResult bindingResult,
                                       ModelAndView modelAndView){
        modelAndView.addObject("roles",this.userRoleService.getAll());
        modelAndView.addObject("createUserFormDto",createUserFormDto);

        if (bindingResult.hasErrors()){
            return super.view("Admin/createUser",modelAndView);
        }

        try{
            this.userService.createUser(createUserFormDto);
        }catch (Exception e){
            modelAndView.addObject("EmailIsTaken",e.getMessage());
            return super.view("Admin/createUser",modelAndView);
        }

        return super.redirect("/Admin/panel");
    }

    @GetMapping("/updateUser/{id}")
    public ModelAndView getUpdateUser(@PathVariable("id") String id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roles",this.userRoleService.getAll());
        modelAndView.addObject("updateUserFormDto",this.userService.findById(id));
        return super.view("Admin/updateUser",modelAndView);
    }

    @PostMapping("/updateUser/{id}")
    public ModelAndView postUpdateUser(@Valid @ModelAttribute(name = "updateUserFormDto") UpdateUserFormDto updateUserFormDto,
                                       BindingResult bindingResult,
                                       ModelAndView modelAndView){
        modelAndView.addObject("roles",this.userRoleService.getAll());
        modelAndView.addObject("updateUserFormDto",updateUserFormDto);

        if (bindingResult.hasErrors()){
            return super.view("Admin/updateUser",modelAndView);
        }

        try{
            this.userService.updateUser(updateUserFormDto);
        }catch (Exception e){
            modelAndView.addObject("EmailIsTaken",e.getMessage());
            return super.view("Admin/updateUser",modelAndView);
        }

        return super.redirect("/Admin/panel");
    }

    @GetMapping("/deleteUser/{id}")
    public ModelAndView getDeleteUser(@PathVariable("id") String id){
        this.userService.removeById(id);
        return super.redirect("/Admin/panel");
    }


    //-------------MODEL ATTRIBUTES----------------//


    @ModelAttribute(name = "createProductFormDto")
    public CreateProductFormDto initCreateProductFormDto(){
        return new CreateProductFormDto();
    }

    @ModelAttribute(name = "updateProductFormDto")
    public UpdateProductFormDto initUpdateProductFormDto(){
        return new UpdateProductFormDto();
    }

    @ModelAttribute(name = "createUserFormDto")
    public CreateUserFormDto initCreateUserFormDto(){
        return new CreateUserFormDto();
    }

    @ModelAttribute(name = "updateUserFormDto")
    public UpdateUserFormDto initUpdateUserFormDto(){
        return new UpdateUserFormDto();
    }
}
