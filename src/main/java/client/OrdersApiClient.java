package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.CreateOrderRequest;

import static config.ConfigApp.*;
public class OrdersApiClient extends BaseApiClient{
    @Step("Создать заказ")
    public Response createOrder (CreateOrderRequest request, String bearerToken){
        return getPostSpec()
                .headers(
                        "Authorization",
                        bearerToken)
                .body(request)
                .when()
                .post(ORDERS_URL);
    }
    @Step("Получить заказ по токену авторизации")
    public Response getOrder(String bearerToken){
        return getPostSpec()
                .headers(
                "Authorization",
                bearerToken)
                .when()
                .get(GET_ORDERS_URL);

    }
}
