package client;

import io.restassured.response.Response;
import models.CreateOrderRequest;

import static config.ConfigApp.*;

public class OrdersApiClient extends BaseApiClient{
    public Response createOrder (CreateOrderRequest request, String bearerToken){
        return getPostSpec()
                .headers(
                        "Authorization",
                        bearerToken)
                .body(request)
                .when()
                .post(BASE_URL + ORDERS_URL);
    }
    public Response getOrder(String bearerToken){
        return getPostSpec()
                .headers(
                "Authorization",
                bearerToken)
                .when()
                .get(BASE_URL + GET_ORDERS_URL);

    }
}
