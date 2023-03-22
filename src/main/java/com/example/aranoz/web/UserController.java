package com.example.aranoz.web;

import com.example.aranoz.domain.dtoS.model.User.UserChangePasswordFormDto;
import com.example.aranoz.domain.dtoS.model.User.UserLoginFormDto;
import com.example.aranoz.domain.dtoS.model.User.UserRegisterFormDto;
import com.example.aranoz.domain.dtoS.model.User.UserProfileEditDto;
import com.example.aranoz.services.cartItem.CartItemService;
import com.example.aranoz.services.order.OrderService;
import com.example.aranoz.services.product.ProductService;
import com.example.aranoz.services.user.UserService;
import com.example.aranoz.util.ImageUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/User")
public class UserController extends BaseController {
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CartItemService cartItemService;

    @Autowired
    public UserController(UserService userService, ProductService productService, OrderService orderService, CartItemService cartItemService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.cartItemService = cartItemService;
    }


    //-------------DATA MANIPULATION----------------//


    @GetMapping("/login")
    public ModelAndView getLogin(){
        return super.view("User/login");
    }

    @PostMapping("/login")
    public ModelAndView postLogin(@Valid @ModelAttribute(name = "userLoginFormDto") UserLoginFormDto userLoginFormDto,
                                  BindingResult bindingResult,
                                  ModelAndView modelAndView){
        if (bindingResult.hasErrors()){
            return super.view("User/login",
                    modelAndView.addObject("userLoginFormDto", userLoginFormDto));
        }

        try{
            this.userService.authenticateUser(userLoginFormDto);
        }catch (Exception e){
            return super.view("User/login",
                    modelAndView.addObject("NoSuchUser",e.getMessage())
                            .addObject("userLoginFormDto", userLoginFormDto));
        }
        return super.redirect("/");
    }



    @GetMapping("/register")
    public ModelAndView getRegister(){
        return super.view("User/register");
    }


    @PostMapping ("/register")
    public ModelAndView postRegister(@Valid @ModelAttribute(name = "userRegisterFormDto") UserRegisterFormDto userRegisterFormDto,
                                     BindingResult bindingResult,
                                     ModelAndView modelAndView){
        if (bindingResult.hasErrors()){
            return super.view("User/register",
                    modelAndView.addObject("userRegisterFormDto",userRegisterFormDto));
        }

        try{
            userService.registerUser(userRegisterFormDto);
        }catch (Exception e){
            return super.view("User/register",
                    modelAndView.addObject("EmailIsTaken",e.getMessage()));
        }

        return super.redirect("/User/login");
    }

    @GetMapping("/editProfile/{id}")
    public ModelAndView getEditProfile(@PathVariable("id") String id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userProfileEditDto", this.userService.findById(id));
        return super.view("User/editProfile", modelAndView);
    }

    @PostMapping("/editProfile/{id}")
    public ModelAndView postEditProfile(@Valid @ModelAttribute("userProfileEditDto") UserProfileEditDto userProfileEditDto,
                                        BindingResult bindingResult,
                                        ModelAndView modelAndView){
        modelAndView.addObject("userProfileEditDto",userProfileEditDto);

        if (bindingResult.hasErrors()){
            return super.view("User/editProfile",modelAndView);
        }

        try{
            this.userService.editUserProfile(userProfileEditDto);
        }catch (Exception e){
            modelAndView.addObject("EmailIsTaken",e.getMessage());//да променя името на еррор контейнер примерно
            return super.view("User/editProfile",modelAndView);
        }

        return super.redirect("/User/profile");
    }

    @GetMapping("/changePassword/{id}")
    public ModelAndView getChangePassword(@PathVariable("id") String id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("id",id);
        return super.view("User/changePassword");
    }

    @PostMapping("/changePassword/{id}")
    public ModelAndView postChangePassword(@Valid @ModelAttribute(name = "userChangePasswordFormDto") UserChangePasswordFormDto userChangePasswordFormDto,
                                           BindingResult bindingResult,
                                           ModelAndView modelAndView){
        if (bindingResult.hasErrors()){
            return super.view("User/changePassword",
                    modelAndView.addObject("id",userChangePasswordFormDto.getId())
                            .addObject("userChangePasswordFormDto", userChangePasswordFormDto));
        }

        try{
            this.userService.changePassword(userChangePasswordFormDto);
        } catch (Exception e){
            return super.view("User/changePassword",
                                modelAndView.addObject("id",userChangePasswordFormDto.getId())
                                            .addObject("userChangePasswordFormDto", userChangePasswordFormDto))
                                            .addObject("error",e.getMessage());
        }

        return super.redirect("/");
    }

    @GetMapping("/removeProduct/{id}")
    public ModelAndView getRemoveProductFromCart(@PathVariable("id") String id,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated() || authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ANONYMOUS")){
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(id)){
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }else{
            this.cartItemService.removeCartItemByProductId(id,this.userService.getUserByEmail(authentication.getName()).getId());
        }


        return super.redirect("/User/cart");
    }


    //-------------DATA DISPLAY----------------//


    @GetMapping("/profile")
    public ModelAndView getProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user",this.userService.getUserByEmail(authentication.getName()));
        modelAndView.addObject("items",this.orderService.getUserOrders(this.userService.getUserByEmail(authentication.getName()).getId()));
        return super.view("User/profile", modelAndView);
    }

    @GetMapping("/cart")
    public ModelAndView cart(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        ModelAndView modelAndView = new ModelAndView();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated() && !authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ANONYMOUS")){//nagajdam z lognat user - da pogledna ot dolniq if ko trqq
            modelAndView.addObject("items",this.cartItemService.getCartItemsForLoggedUser(this.userService.getUserByEmail(authentication.getName()).getId()));
            modelAndView.addObject("imgUtil", new ImageUtil());
            modelAndView.addObject("isAccepted", this.cartItemService.getCartItemsForLoggedUser(this.userService.getUserByEmail(authentication.getName()).getId()).size() <= 0);
            modelAndView.addObject("total", this.productService.getTotalPriceOfCartProducts(this.cartItemService.getCartItemsForLoggedUser(this.userService.getUserByEmail(authentication.getName()).getId())));
            return super.view("User/cart", modelAndView);
        }

        if (cookies != null){
            modelAndView.addObject("items",this.productService.getCookieProducts(cookies));
            modelAndView.addObject("isAccepted", this.productService.getCookieProducts(cookies).size() <= 0);
            modelAndView.addObject("imgUtil", new ImageUtil());
            modelAndView.addObject("total", this.productService.getTotalPriceOfCartProducts(this.productService.getCookieProducts(cookies)));
        }

        return super.view("User/cart", modelAndView);
    }


    //-------------MODEL ATTRIBUTES----------------//


    @ModelAttribute(name = "userRegisterFormDto")
    public UserRegisterFormDto initUserRegisterVM(){
        return new UserRegisterFormDto();
    }

    @ModelAttribute(name = "userLoginFormDto")
    public UserLoginFormDto initUserLoginFormDto(){
        return new UserLoginFormDto();
    }

    @ModelAttribute(name = "userChangePasswordFormDto")
    public UserChangePasswordFormDto initUserChangePasswordFormDto(){
        return new UserChangePasswordFormDto();
    }

    @ModelAttribute(name = "userProfileEditDto")
    public UserProfileEditDto initUserProfileEditDto(){
        return new UserProfileEditDto();
    }
}
