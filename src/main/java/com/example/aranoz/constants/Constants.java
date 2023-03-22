package com.example.aranoz.constants;

public enum Constants {
    ;


    //-------------EMAIL CONSTANTS---------------//


    public static final String EMAIL_ADDRESS = "aranoz@outlook.com";
    public static final String EMAIL_ADDRESS_PASSWORD = "Zra159noz";
    public static final String EMAIL_SUBJECT_FORMAT = "Тема: %s. имейл: %s";
    public static final String EMAIL_MESSAGE_FORMAT = "%s има въпрос: %s";
    public static final String FORGOTTEN_EMAIL_SUBJECT = "Линк за промяна на паролата.";
    public static final String FORGOTTEN_EMAIL_MESSAGE_FORMAT = "Изпращаме ти линк с който можете да смените вашата забравена потребителска парола: %s";
    public static final String EMAIL_ACCEPTED_ORDER = "Успешна поръчка!";
    public static final String EMAIL_ACCEPTED_ORDER_FORMAT = "Вие успешно направихте поръчка за %d продукта с обща сума %.2f. Линк към поръчката: %s";
    public static final String EMAIL_CONTENT_TYPE = "text/html; charset=utf-8";


    //-------------PATH CONSTANTS---------------//


    public static final String CHANGE_PASSWORD_LINK = "http://localhost:8000/User/changePassword/";
    public static final String ORDER_INFO_FORMAT = "http://localhost:8000/Order/%s";
    public static final String DEFAULT_COOKIE_PATH = "/";
    public static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";


    //-------------CATEGORY CONSTANTS---------------//


    public static final String PRODUCT_CATEGORY_ALL = "all";
    public static final String PRODUCT_CATEGORY_BED = "bed";
    public static final String PRODUCT_CATEGORY_SOFA = "sofa";
    public static final String PRODUCT_CATEGORY_CHAIR = "chair";
    public static final String PRODUCT_CATEGORY_TABLE = "table";
    public static final String PRODUCT_CATEGORY_CABINET = "cabinet";


    //-------------PAYMENT METHOD CONSTANTS---------------//


    public static final String CASH_PAYMENT = "CASH";
    public static final String CARD_PAYMENT = "CARD";


    //-------------PAYMENT METHOD CONSTANTS---------------//


    public static final String DEFAULT_USER_FIRST_NAME = "Samuil";
    public static final String DEFAULT_USER_LAST_NAME = "Dobrinski";
    public static final String DEFAULT_USER_EMAIL_ADDRESS = "msamuil@abv.bg";
    public static final String DEFAULT_USER_PASSWORD = "Admin123";
}
