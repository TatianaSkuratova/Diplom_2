import client.UserApiClient;
import io.restassured.response.Response;
import models.*;
import models.Error;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static helper.CreateUserRequestGenerator.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;


public class CreateUserTest {
    CreateUserRequest createUserRequest;
    UserApiClient userApiClient;

    @Before
    public void setup() {
        userApiClient = new UserApiClient();
    }
    @After
    public void cleanUp(){
        Response loginResponse = userApiClient.loginUser(LoginUserRequest.fromCreateUserRequest(createUserRequest));
        if (loginResponse.body().jsonPath().getBoolean("success")){
            LoginUserResponse loginUserResponse = loginResponse.as(LoginUserResponse.class);
            String token = loginUserResponse.getAccessToken();
            userApiClient.deleteUser(token);
        }
    }
    @Test
    @DisplayName("Создание пользователя доступно")
    public void createUserIsAvailable() {
        createUserRequest = getRandomUser();
        Response createResponse =  userApiClient.createUser(createUserRequest);
        CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
        assertTrue(createUserResponse.getSuccess());
        assertEquals(createUserRequest.getEmail(),createUserResponse.getUser().getEmail());
        assertEquals(createUserRequest.getName(),createUserResponse.getUser().getName());
        assertEquals(SC_OK,createResponse.statusCode());
    }
    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован, недоступно")
    public void createDuplicateUserIsNotAvailable(){
        createUserRequest = getRandomUser();
        userApiClient.createUser(createUserRequest);
        Response createDuplicateResponse = userApiClient.createUser(createUserRequest);
        Error errorMessageResponse = createDuplicateResponse.as(Error.class);
        assertEquals(SC_FORBIDDEN,createDuplicateResponse.statusCode());
        assertEquals(Error.MESSAGE_DUPLICATE_USER, errorMessageResponse.getMessage());
        assertFalse(errorMessageResponse.getSuccess());
    }

    @Test
    @DisplayName("Создание пользователя без email недоступно")
    public void createUserWithoutEmailIsNotAvailable() {
        createUserRequest = getRandomUserWithoutEmail();
        Response createResponse =  userApiClient.createUser(createUserRequest);
        Error errorMessageResponse = createResponse.as(Error.class);
        assertEquals(SC_FORBIDDEN,createResponse.statusCode());
        assertEquals(Error.MESSAGE_REQUIRED_FIELDS, errorMessageResponse.getMessage());
        assertFalse(errorMessageResponse.getSuccess());
    }
    @Test
    @DisplayName("Создание пользователя без пароля недоступно")
    public void createUserWithoutPasswordIsNotAvailable() {
        createUserRequest = getRandomUserWithoutPassword();
        Response createResponse =  userApiClient.createUser(createUserRequest);
        Error errorMessageResponse = createResponse.as(Error.class);
        assertEquals(SC_FORBIDDEN,createResponse.statusCode());
        assertEquals(Error.MESSAGE_REQUIRED_FIELDS, errorMessageResponse.getMessage());
        assertFalse(errorMessageResponse.getSuccess());
    }
    @Test
    @DisplayName("Создание пользователя без имени доступно")
    public void createUserWithoutNameIsAvailable() {
        createUserRequest = getRandomUserWithoutName();
        createUserRequest = getRandomUserWithoutPassword();
        Response createResponse =  userApiClient.createUser(createUserRequest);
        Error errorMessageResponse = createResponse.as(Error.class);
        assertEquals(SC_FORBIDDEN,createResponse.statusCode());
        assertEquals(Error.MESSAGE_REQUIRED_FIELDS, errorMessageResponse.getMessage());
        assertFalse(errorMessageResponse.getSuccess());
    }

}