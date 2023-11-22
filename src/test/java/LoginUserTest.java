import client.UserApiClient;
import helper.CreateUserRequestGenerator;
import io.restassured.response.Response;
import models.CreateUserRequest;
import models.LoginUserRequest;
import models.LoginUserResponse;
import models.Error;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LoginUserTest {
    CreateUserRequest createUserRequest;
    LoginUserRequest loginUserRequest;
    UserApiClient userApiClient;
    @Before
    public void setup(){
        userApiClient = new UserApiClient();
    }

    @Test
    @DisplayName("Проверка авторизации пользователя")
    public void loginUserIsAvailable(){
        createUserRequest = CreateUserRequestGenerator.getRandomUser();
        userApiClient.createUser(createUserRequest);
        Response responseLoginUser = userApiClient.loginUser(LoginUserRequest.fromCreateUserRequest(createUserRequest));
        LoginUserResponse loginUserResponse = responseLoginUser.as(LoginUserResponse.class);
        assertAll(() -> assertTrue(loginUserResponse.getSuccess()),
            () ->  assertEquals(createUserRequest.getEmail(),loginUserResponse.getUser().getEmail()),
            () ->  assertEquals(createUserRequest.getName(),loginUserResponse.getUser().getName()),
            () ->  assertEquals(SC_OK,responseLoginUser.statusCode())
        );
    }
    @Test
    @DisplayName("Проверка недоступности авторизации пользователя с неверным логином")
    public void loginUserWithWrongPasswordIsNotAvailable(){
        createUserRequest = CreateUserRequestGenerator.getRandomUser();
        userApiClient.createUser(createUserRequest);
        LoginUserRequest loginUserRequest = new LoginUserRequest.Builder()
                .withEmail(createUserRequest.getEmail())
                .withPassword(createUserRequest.getPassword()+ RandomStringUtils.randomAlphabetic(3)).build();
        Response errorResponse = userApiClient.loginUser(loginUserRequest);
        Error userError = errorResponse.as(Error.class);
        assertAll(
                () -> assertFalse(userError.getSuccess()),
                () -> assertEquals(userError.getMessage(), Error.MESSAGE_WRONG_CREDENTIALS),
                () -> assertEquals(SC_UNAUTHORIZED,errorResponse.statusCode())

        );
    }
    @Test
    @DisplayName("Проверка недоступности авторизации пользователя с неверным емейлом")
    public void loginUserWithWrongEmailIsNotAvailable(){
        createUserRequest = CreateUserRequestGenerator.getRandomUser();
        userApiClient.createUser(createUserRequest);
        LoginUserRequest loginUserRequest = new LoginUserRequest.Builder()
                .withEmail(createUserRequest.getEmail()+ RandomStringUtils.randomAlphabetic(3))
                .withPassword(createUserRequest.getPassword()).build();
        Response errorResponse = userApiClient.loginUser(loginUserRequest);
        Error userError = errorResponse.as(Error.class);
        assertAll(
                () ->  assertFalse(userError.getSuccess()),
                 () -> assertEquals(userError.getMessage(), Error.MESSAGE_WRONG_CREDENTIALS),
        () -> assertEquals(SC_UNAUTHORIZED,errorResponse.statusCode())
        );
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
}
