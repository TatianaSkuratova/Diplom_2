import client.IngredientsApiClients;
import client.OrdersApiClient;
import client.UserApiClient;
import helper.CreateIngredientsGenerator;
import helper.CreateUserRequestGenerator;
import io.restassured.response.Response;
import models.*;
import models.Error;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;


import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertFalse;

public class CreateOrderTest {

    IngredientsResponse ingredientsResponse;
    OrdersApiClient ordersApiClient;
    @Before
    public void setup(){
        IngredientsApiClients ingredientsApiClients = new IngredientsApiClients();
        Response response = ingredientsApiClients.getIngredients();
        ingredientsResponse = response.as(IngredientsResponse.class);
        ordersApiClient = new OrdersApiClient();
    }
    @Test
    @DisplayName("Создание заказа без авторизаци недоступно")
    public void createOrderWithoutAuthIsAvailable(){
        CreateOrderRequest createOrderRequest = CreateIngredientsGenerator.randomIngredients(ingredientsResponse.getData(), 6);
        Response response = ordersApiClient.createOrder(createOrderRequest, "");
        assertEquals(SC_OK, response.statusCode());
        assertTrue(response.body().jsonPath().get("success"));
        assertTrue(response.body().jsonPath().get("order.number") instanceof Integer);
    }
    @Test
    @DisplayName("Создание заказа с авторизацией доступно")
    public void createOrderWithAuthIsAvailable(){
        //создаем пользователя
        CreateUserRequest userRequest = CreateUserRequestGenerator.getRandomUser();
        UserApiClient userApiClient = new UserApiClient();
        Response createUserResponse= userApiClient.createUser(userRequest);
        CreateUserResponse createUser = createUserResponse.as(CreateUserResponse.class);
        //получаем из ответа токен авторизации
        String token = createUser.getAccessToken();
        //создаем произвольный набор ингредиентов
        CreateOrderRequest createOrderRequest = CreateIngredientsGenerator.randomIngredients(ingredientsResponse.getData(), 6);
        Response createResponse = ordersApiClient.createOrder(createOrderRequest, token);
        CreateOrderResponse createOrderResponse = createResponse.as(CreateOrderResponse.class);
        //создаем список id ингредиентов из ответа на запрос
        ArrayList<String> ingredients = new ArrayList<>();
        for (Ingredient ingredient: createOrderResponse.getOrder().getIngredients()
             ) {
            ingredients.add(ingredient.get_id());
        }
        userApiClient.deleteUser(token);
        //проверяем имя пользователя
        assertEquals(userRequest.getName(), createOrderResponse.getOrder().getOwner().getName());
        //статус-код и успешность
        assertEquals(SC_OK, createResponse.statusCode());
        assertTrue(createOrderResponse.getSuccess());
        assertEquals(createOrderResponse.getOrder().getStatus(), "done");
        //проверяем, что список ингредиентов из ответа и из запроса совпадают
        assertEquals(ingredients, createOrderRequest.getIngredients());
        //удаляем временного пользователя

    }
    @Test
    @DisplayName("Создание заказа без ингредиентов недоступно")
    public void createOrderWithoutIngredientsIsNotAvailable(){
        CreateOrderRequest createOrderRequest = CreateIngredientsGenerator.emptyIngredients();
        Response response = ordersApiClient.createOrder(createOrderRequest, "");
        Error error = response.as(Error.class);
        assertEquals(SC_BAD_REQUEST, response.statusCode());
        assertFalse(error.getSuccess());
        assertEquals(error.getMessage(), Error.MESSAGE_EMPTY_INGREDIENTS);
    }

    @Test
    @DisplayName("Создание заказа с несуществующими ингредиенами недоступно")
    public void createOrderWithWrongIngredientsIsNotAvailable(){
        CreateOrderRequest createOrderRequest = CreateIngredientsGenerator.fakeIngredients();
        Response response = ordersApiClient.createOrder(createOrderRequest, "");
        Error error = response.as(Error.class);
        assertEquals(SC_BAD_REQUEST, response.statusCode());
        assertFalse(error.getSuccess());
        assertEquals(error.getMessage(), Error.MESSAGE_INCORRECT_INGREDIENTS);
    }

}
