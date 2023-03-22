package com.example.aranoz.services.init;

import com.example.aranoz.services.orderPaymentMethod.OrderPaymentMethodService;
import com.example.aranoz.services.orderStatus.OrderStatusService;
import com.example.aranoz.services.productCategory.ProductCategoryService;
import com.example.aranoz.services.user.UserService;
import com.example.aranoz.services.userRole.UserRoleService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataBaseInitServiceImpl implements DataBaseInitService{
    private final UserRoleService userRoleService;
    private final ProductCategoryService productCategoryService;
    private final UserService userService;
    private final OrderStatusService orderStatusService;
    private final OrderPaymentMethodService orderPaymentMethodService;

    @Autowired
    public DataBaseInitServiceImpl(UserRoleService roleService, ProductCategoryService productCategoryService, UserService userService, OrderStatusService orderStatusService, OrderPaymentMethodService orderPaymentMethodService) {
        this.userRoleService = roleService;
        this.productCategoryService = productCategoryService;
        this.userService = userService;
        this.orderStatusService = orderStatusService;
        this.orderPaymentMethodService = orderPaymentMethodService;
    }

    @PostConstruct
    public void postConstruct(){
        dbInit();
    }

    @Override
    public void dbInit() {
        if (!isDbInit()) {
            this.productCategoryService.dbInit();
            this.userRoleService.dbInit();
            this.userService.dbInit();
            this.orderStatusService.dbInit();
            this.orderPaymentMethodService.dbInit();
        }
    }

    @Override
    public boolean isDbInit() {
        return  this.productCategoryService.isDbInit() &&
                this.userRoleService.isDbInit() &&
                this.userService.isDbInit() &&
                this.orderStatusService.isDbInit() &&
                this.orderPaymentMethodService.isDbInit();
    }
}