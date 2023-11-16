import client.UserApiClient;
import helper.CreateUserRequestGenerator;
import io.restassured.response.Response;
import models.*;
import models.Error;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

import static junit.framework.TestCase.*;

public class UpdateUserTest {

    UserApiClient userApiClient;
    CreateUserRequest createUserRequest;
    CreateUserRequest updateUserRequest;
    Boolean success;

    @Before
    public void setup(){
        userApiClient = new UserApiClient();
    }

    @Test
    @DisplayName("Проверка изменения данных пользоваля с токеном авторизации")
    public void updateUserWithAuthIsAvailable(){
        //создаем рандомного пользователя
        createUserRequest = CreateUserRequestGenerator.getRandomUser();
        Response createUserResponse= userApiClient.createUser(createUserRequest);
        CreateUserResponse createUser = createUserResponse.as(CreateUserResponse.class);
        //получаем из ответа токен авторизации
        String token = createUser.getAccessToken();
        //создаем данные для обновления пользователя по существующему токену
        updateUserRequest = CreateUserRequestGenerator.getRandomUser();
        Response updateUserResponse= userApiClient.updateUser(token, updateUserRequest);
        UpdateUserResponse updateUser = updateUserResponse.as(UpdateUserResponse.class);
        //получаем флаг успешности обновления для удаления пользователя после тестового прогона
        success = updateUser.getSuccess();
        //получаем данные обновленного пользователя
        Response getUserResponse = userApiClient.getUser(token);
        UpdateUserResponse getUser = getUserResponse.as(UpdateUserResponse.class);
        //проверяем, что данные совпадают
        assertEquals(updateUser.getUser(),getUser.getUser());
        //проверяем успешность обновления
        assertTrue(success);
        assertEquals(SC_OK, getUserResponse.statusCode());
    }
    @Test
    @DisplayName("Проверка недоступности изменения данных пользоваля без токена авторизации")
    public void updateUserWithoutAuthIsNotAvailable(){
        //создаем рандомного пользователя
        createUserRequest = CreateUserRequestGenerator.getRandomUser();
        Response createUserResponse= userApiClient.createUser(createUserRequest);
        CreateUserResponse createUser = createUserResponse.as(CreateUserResponse.class);
        //создаем данные для обновления пользователя по существующему токену
        updateUserRequest = CreateUserRequestGenerator.getRandomUser();
        Response updateUserResponse= userApiClient.updateUser("token", updateUserRequest);
        Error error = updateUserResponse.as(Error.class);
        //получаем флаг успешности обновления для удаления пользователя после тестового прогона
        success = error.getSuccess();
        assertFalse(success);
        assertEquals(SC_UNAUTHORIZED, updateUserResponse.statusCode());
        assertEquals(Error.MESSAGE_AUTHORISED, error.getMessage());
    }
    @After
    public void cleanUp(){
        Response loginResponse;
        if (success){
            loginResponse = userApiClient.loginUser(LoginUserRequest.fromCreateUserRequest(updateUserRequest));
        } else {
            loginResponse = userApiClient.loginUser(LoginUserRequest.fromCreateUserRequest(createUserRequest));
        }
        if (loginResponse.body().jsonPath().getBoolean("success")){
            LoginUserResponse loginUserResponse = loginResponse.as(LoginUserResponse.class);
            String token = loginUserResponse.getAccessToken();
            userApiClient.deleteUser(token);
        }
    }
}
