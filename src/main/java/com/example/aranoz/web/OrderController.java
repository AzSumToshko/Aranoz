package com.example.aranoz.web;

import com.example.aranoz.domain.dtoS.model.Order.OrderCheckoutFormDto;
import com.example.aranoz.domain.dtoS.view.Order.PaymentVM;
import com.example.aranoz.domain.dtoS.view.User.CartProductVM;
import com.example.aranoz.domain.entities.CartItem;
import com.example.aranoz.services.cartItem.CartItemService;
import com.example.aranoz.services.order.OrderService;
import com.example.aranoz.services.product.ProductService;
import com.example.aranoz.services.user.UserService;
import com.example.aranoz.util.ImageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import javax.mail.MessagingException;
import java.util.List;

@Controller
@RequestMapping("/Order")
public class OrderController extends BaseController {
    private final ProductService productService;
    private final UserService userService;
    private final CartItemService cartItemService;
    private final OrderService orderService;

    @Autowired
    public OrderController(ProductService productService, UserService userService, CartItemService cartItemService, OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.cartItemService = cartItemService;
        this.orderService = orderService;
    }


    //-------------DATA MANIPULATION----------------//


    @GetMapping("/checkout")
    public ModelAndView getCheckout(HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView();

        if (!authentication.isAuthenticated() || authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ANONYMOUS")){
            modelAndView.addObject("items", this.productService.getCookieProducts(cookies));
            modelAndView.addObject("isAccepted", this.productService.getCookieProducts(cookies).size() <= 0);
            modelAndView.addObject("subtotal", this.productService.getTotalPriceOfCartProducts(this.productService.getCookieProducts(cookies)));
            modelAndView.addObject("total", this.productService.getTotalPriceOfCartProducts(this.productService.getCookieProducts(cookies)) + 5);
            modelAndView.addObject("cookies", this.productService.convertCartProductListToString(this.productService.getCookieProducts(cookies)));
        }else{
            modelAndView.addObject("items",this.cartItemService.getCartItemsForLoggedUser(this.userService.getUserByEmail(authentication.getName()).getId()));
            modelAndView.addObject("isAccepted", this.cartItemService.getCartItemsForLoggedUser(this.userService.getUserByEmail(authentication.getName()).getId()).size() <= 0);
            modelAndView.addObject("subtotal", this.productService.getTotalPriceOfCartProducts(this.cartItemService.getCartItemsForLoggedUser(this.userService.getUserByEmail(authentication.getName()).getId())));
            modelAndView.addObject("total", this.productService.getTotalPriceOfCartProducts(this.cartItemService.getCartItemsForLoggedUser(this.userService.getUserByEmail(authentication.getName()).getId())) +5);
            modelAndView.addObject("cookies", this.productService.convertCartProductListToString(this.cartItemService.getCartItemsForLoggedUser(this.userService.getUserByEmail(authentication.getName()).getId())));
        }
        return super.view("Order/checkout", modelAndView);
    }

    @PostMapping("/checkout")
    public ModelAndView postCheckout(@Valid @ModelAttribute(name = "orderCheckoutFormDto") OrderCheckoutFormDto orderCheckoutFormDto,
                                     BindingResult bindingResult,
                                     ModelAndView modelAndView) throws JsonProcessingException {
        List<CartProductVM> items = this.productService.convertStringToCartProductList(orderCheckoutFormDto.getStringCookies());

        modelAndView.addObject("items", items);
        modelAndView.addObject("subtotal", this.productService.getTotalPriceOfCartProducts(items));
        modelAndView.addObject("total", this.productService.getTotalPriceOfCartProducts(items) + 5);

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("orderCheckoutFormDto",orderCheckoutFormDto);
            modelAndView.addObject("cookies", orderCheckoutFormDto.getStringCookies());
            return super.view("Order/checkout", modelAndView);
        }

        modelAndView.addObject("createdOrder", this.orderService.createOrder(orderCheckoutFormDto,items));
        //order object contains all the data except date and payment method and status

        return super.view("Order/payment", modelAndView);
    }

    @PostMapping("/payment")
    public ModelAndView postPayment(HttpServletRequest request,
                                    HttpServletResponse response,
                                    PaymentVM model,
                                    ModelAndView modelAndView) throws MessagingException, JsonProcessingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated() || authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ANONYMOUS")){
            this.orderService.saveOrder(model,"-1");
            for (Cookie cookie : this.orderService.updateCookiesForDelete(request.getCookies())) {
                cookie.setPath("/");
                response.addCookie(cookie);
            }

        }else{
            String id = this.userService.getUserByEmail(authentication.getName()).getId();
            this.orderService.saveOrder(model,id);
            List<CartItem> cartItems = this.cartItemService.getCartItems(id);
            for (CartItem cartItem : cartItems) {
                this.cartItemService.removeCartItemByProductId(cartItem.getProductId(),cartItem.getUserId());
            }

        }

        modelAndView.addObject("order", this.orderService.findByEmailAndDate(model.getOrder().getEmail(), model.getOrder().getDateOfCreation()));
        modelAndView.addObject("items", this.orderService.getProductsAndQuantities(this.orderService.findByEmailAndDate(model.getOrder().getEmail(), model.getOrder().getDateOfCreation()).getProductAndQuantities()));
        return super.view("Order/confirmation", modelAndView);
    }

    @GetMapping("/{id}")
    public ModelAndView getUserOrder(@PathVariable("id") String id) throws JsonProcessingException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("order", this.orderService.findById(id));
        modelAndView.addObject("items", this.orderService.getProductsAndQuantities(this.orderService.findById(id).getProductAndQuantities()));
        return super.view("Order/confirmation", modelAndView);
    }


    //-------------MODEL ATTRIBUTES----------------//


    @ModelAttribute(name = "orderCheckoutFormDto")
    public OrderCheckoutFormDto initOrderCheckoutFormDto() {
        return new OrderCheckoutFormDto();
    }
}
