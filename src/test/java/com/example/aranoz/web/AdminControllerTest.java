package com.example.aranoz.web;

import com.example.aranoz.domain.dtoS.model.Product.CreateProductFormDto;
import com.example.aranoz.domain.dtoS.model.Product.UpdateProductFormDto;
import com.example.aranoz.domain.dtoS.model.User.CreateUserFormDto;
import com.example.aranoz.domain.dtoS.model.User.UpdateUserFormDto;
import com.example.aranoz.domain.entities.Product;
import com.example.aranoz.domain.entities.ProductCategory;
import com.example.aranoz.domain.entities.User;
import com.example.aranoz.services.product.ProductService;
import com.example.aranoz.services.productCategory.ProductCategoryService;
import com.example.aranoz.services.user.UserService;
import com.example.aranoz.services.userRole.UserRoleService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductCategoryService productCategoryService;

    @Mock
    private UserService userService;

    @Mock
    private UserRoleService userRoleService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testGetAdminPanel() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User());
        List<Product> products = new ArrayList<>();
        products.add(new Product());

        when(userService.getAllUsers()).thenReturn(users);
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/Admin/panel"))
                .andExpect(status().isOk())
                .andExpect(view().name("Admin/adminPanel"))
                .andExpect(model().attribute("users", users))
                .andExpect(model().attribute("products", products));
    }

    @Test
    public void testGetCreateProduct() throws Exception {
        List<ProductCategory> categories = new ArrayList<>();
        categories.add(new ProductCategory());

        when(productCategoryService.getAll()).thenReturn(categories);

        mockMvc.perform(get("/Admin/createProduct"))
                .andExpect(status().isOk())
                .andExpect(view().name("Admin/createProduct"))
                .andExpect(model().attribute("categories", categories));
    }

    @Test
    public void testPostCreateProduct() throws Exception {
        CreateProductFormDto createProductFormDto = new CreateProductFormDto();
        createProductFormDto.setName("Test Product");
        createProductFormDto.setPrice(9.99);

        doNothing().when(productService).createProduct(createProductFormDto);

        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/Admin/createProduct")
                        .file(file)
                        .param("name", "Test Product")
                        .param("price", "9.99")
                        .param("categoryId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/Admin/panel"));
    }

    @Test
    public void testGetUpdateProduct() throws Exception {
        String id = "1234";
        Product product = new Product();
        product.setId(id);

        when(productService.findById(id)).thenReturn(product);
        when(productCategoryService.getAll()).thenReturn(new ArrayList<ProductCategory>());

        mockMvc.perform(get("/updateProduct/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("Admin/updateProduct"))
                .andExpect(model().attributeExists("updateProductFormDto", "categories"))
                .andExpect(model().attribute("updateProductFormDto", hasProperty("id", equalTo(id))));
    }

    @Test
    public void testPostUpdateProduct_withValidForm() throws Exception {
        UpdateProductFormDto updateProductFormDto = new UpdateProductFormDto();
        updateProductFormDto.setId("1234");
        BindingResult bindingResult = new BeanPropertyBindingResult(updateProductFormDto, "updateProductFormDto");

        doNothing().when(productService).updateProduct(updateProductFormDto);

        mockMvc.perform(post("/updateProduct/{id}", updateProductFormDto.getId())
                        .flashAttr("updateProductFormDto", updateProductFormDto)
                        .param("name", "Test Product")
                        .param("price", "9.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/Admin/panel"));
    }

    @Test
    public void testPostUpdateProduct_withInvalidForm() throws Exception {
        UpdateProductFormDto updateProductFormDto = new UpdateProductFormDto();
        updateProductFormDto.setId("1234");
        BindingResult bindingResult = new BeanPropertyBindingResult(updateProductFormDto, "updateProductFormDto");
        bindingResult.reject("name", "Name is required");
        bindingResult.reject("price", "Price must be a number");

        when(productCategoryService.getAll()).thenReturn(new ArrayList<ProductCategory>());

        mockMvc.perform(post("/updateProduct/{id}", updateProductFormDto.getId())
                        .flashAttr("updateProductFormDto", updateProductFormDto))
                .andExpect(status().isOk())
                .andExpect(view().name("Admin/updateProduct"))
                .andExpect(model().attributeExists("categories", "updateProductFormDto"))
                .andExpect(model().attributeHasFieldErrors("updateProductFormDto", "name", "price"));
    }

    @Test
    public void testPostUpdateProduct() throws Exception {
        String id = "123";
        UpdateProductFormDto updateProductFormDto = new UpdateProductFormDto();
        BindingResult bindingResult = mock(BindingResult.class);
        ModelAndView modelAndView = new ModelAndView();
        doNothing().when(productService).updateProduct(updateProductFormDto);

        // Test when there are no errors
        ModelAndView result = adminController.postUpdateProduct(updateProductFormDto, bindingResult, modelAndView);
        assertEquals(result.getViewName(), "redirect:/Admin/panel");

        // Test when there are errors
        bindingResult.addError(new FieldError("updateProductFormDto", "name", "Name is required"));
        result = adminController.postUpdateProduct(updateProductFormDto, bindingResult, modelAndView);
        assertEquals(result.getViewName(), "Admin/updateProduct");
    }

    @Test
    public void testGetDeleteProduct() throws Exception {
        String id = "123";
        ModelAndView result = adminController.getDeleteProduct(id);
        assertEquals(result.getViewName(), "redirect:/Admin/panel");
        verify(productService, Mockito.times(1)).removeProductById(id);
    }

    @Test
    public void testGetCreateUser() throws Exception {
        ModelAndView result = adminController.getCreateUser();
        assertEquals(result.getViewName(), "Admin/createUser");
        assertTrue(result.getModel().containsKey("roles"));
    }

    @Test
    public void testPostCreateUser() throws Exception {
        CreateUserFormDto createUserFormDto = new CreateUserFormDto();
        BindingResult bindingResult = mock(BindingResult.class);
        ModelAndView modelAndView = new ModelAndView();
        doNothing().when(userService).createUser(createUserFormDto);

        // Test when there are no errors
        ModelAndView result = adminController.postCreateUser(createUserFormDto, bindingResult, modelAndView);
        assertEquals(result.getViewName(), "redirect:/Admin/panel");

        // Test when there are errors
        bindingResult.addError(new FieldError("createUserFormDto", "email", "Email is required"));
        result = adminController.postCreateUser(createUserFormDto, bindingResult, modelAndView);
        assertEquals(result.getViewName(), "Admin/createUser");
    }

    @Test
    public void testPostCreateUser_EmailIsTaken() throws Exception {
        CreateUserFormDto createUserFormDto = new CreateUserFormDto();
        BindingResult bindingResult = mock(BindingResult.class);
        ModelAndView modelAndView = new ModelAndView();
        String errorMessage = "Email is already taken";
        doThrow(new Exception(errorMessage)).when(userService).createUser(createUserFormDto);

        // Test when email is already taken
        ModelAndView result = adminController.postCreateUser(createUserFormDto, bindingResult, modelAndView);
        assertEquals(result.getViewName(), "Admin/createUser");
        assertTrue(result.getModel().containsKey("EmailIsTaken"));
        assertEquals(result.getModel().get("EmailIsTaken"), errorMessage);
    }

    @Test
    public void testGetUpdateUser() throws Exception {
        String id = "123";
        ModelAndView result = adminController.getUpdateUser(id);
        assertEquals(result.getViewName(), "Admin/updateUser");
        assertTrue(result.getModel().containsKey("roles"));
        assertTrue(result.getModel().containsKey("updateUserFormDto"));
    }

    @Test
    public void postUpdateUser_WithValidData_ShouldUpdateUserAndRedirectToAdminPanel() throws Exception {
        // Arrange
        UpdateUserFormDto dto = new UpdateUserFormDto();
        BindingResult bindingResult = mock(BindingResult.class);
        ModelAndView modelAndView = new ModelAndView();
        doNothing().when(userService).updateUser(dto);

        // Act
        ModelAndView result = adminController.postUpdateUser(dto, bindingResult, modelAndView);

        // Assert
        verify(userRoleService).getAll();
        verify(userService).updateUser(dto);
        assertEquals("/Admin/panel", result.getViewName());
    }

    @Test
    public void postUpdateUser_WithInvalidData_ShouldReturnUpdateUserView() {
        // Arrange
        UpdateUserFormDto dto = new UpdateUserFormDto();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        ModelAndView modelAndView = new ModelAndView("Admin/updateUser");

        // Act
        ModelAndView result = adminController.postUpdateUser(dto, bindingResult, modelAndView);

        // Assert
        verify(userRoleService).getAll();
        assertEquals("Admin/updateUser", result.getViewName());
    }

    @Test
    public void postUpdateUser_WithException_ShouldReturnUpdateUserViewWithErrorMessage() throws Exception {
        // Arrange
        UpdateUserFormDto dto = new UpdateUserFormDto();
        BindingResult bindingResult = mock(BindingResult.class);
        ModelAndView modelAndView = new ModelAndView("Admin/updateUser");
        String errorMessage = "Email is already taken";
        doThrow(new Exception(errorMessage)).when(userService).updateUser(dto);

        // Act
        ModelAndView result = adminController.postUpdateUser(dto, bindingResult, modelAndView);

        // Assert
        verify(userRoleService).getAll();
        verify(userService).updateUser(dto);
        assertEquals("Admin/updateUser", result.getViewName());
        assertEquals(errorMessage, result.getModel().get("EmailIsTaken"));
    }

    @Test
    public void getDeleteUser_ShouldRemoveUserAndRedirectToAdminPanel() {
        // Arrange
        String userId = "123";

        // Act
        ModelAndView result = adminController.getDeleteUser(userId);

        // Assert
        verify(userService).removeById(userId);
        assertEquals("/Admin/panel", result.getViewName());
    }
}