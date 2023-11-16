import client.IngredientsApiClients;
import client.OrdersApiClient;
import client.UserApiClient;
import helper.CreateIngredientsGenerator;
import helper.CreateUserRequestGenerator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.*;
import models.Error;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;

import static junit.framework.TestCase.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class GetOrderTest {


    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersOfClientWithAuthIsAvailable(){
        //создаем пользователя
        UserApiClient userApiClient = new UserApiClient();
        CreateUserRequest createUserRequest =  CreateUserRequestGenerator.getRandomUser();
        Response createUserResponse = userApiClient.createUser(createUserRequest);
        CreateUserResponse userResponse = createUserResponse.as(CreateUserResponse.class);
        //получаем токен пользователя
        String token = userResponse.getAccessToken();
        //получаем список ингредиентов для создания заказов
        IngredientsApiClients ingredientsApiClients = new IngredientsApiClients();
        Response response = ingredientsApiClients.getIngredients();
        IngredientsResponse ingredientsResponse = response.as(IngredientsResponse.class);
        //создаем заказ
        OrdersApiClient ordersApiClient = new OrdersApiClient();
        CreateOrderRequest createOrderRequest = CreateIngredientsGenerator.randomIngredients(ingredientsResponse.getData(), 3);
        Response createOrderResponse = ordersApiClient.createOrder(createOrderRequest, token);
        CreateOrderResponse orderResponse = createOrderResponse.as(CreateOrderResponse.class);
        String idCreatedResponse = orderResponse.getOrder().get_id();
        //получаем заказ
        Response getOrderResponse = ordersApiClient.getOrder(token);
        //получаем id вернувшегося заказа
        String idOrderResponse = getOrderResponse.body().jsonPath().get("orders[0]._id");
        //проверяем, что id вернувшегося заказа равно id созданного
        assertEquals(idCreatedResponse, idOrderResponse);
        assertEquals(SC_OK, getOrderResponse.statusCode());
        assertTrue(getOrderResponse.body().jsonPath().get("success"));
        userApiClient.deleteUser(token);
    }
    @Test
    @DisplayName("Проверка недоступности получения заказов неавторизованного пользователя")
    public void getOrdersOfClientWithoutAuthIsNotAvailable(){
        OrdersApiClient ordersApiClient = new OrdersApiClient();
        //получаем заказ
        Response getOrderResponse = ordersApiClient.getOrder("token");
        //получаем id вернувшегося заказа
        Error error = getOrderResponse.as(Error.class);
        //проверяем, что id вернувшегося заказа равно id созданного
        assertEquals(error.getMessage(), Error.MESSAGE_AUTHORISED);
        assertEquals(SC_UNAUTHORIZED, getOrderResponse.statusCode());
        assertFalse(error.getSuccess());
    }
}
