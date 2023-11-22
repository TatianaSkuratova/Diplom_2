package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.CreateUserRequest;
import models.LoginUserRequest;

import static config.ConfigApp.*;

public class UserApiClient extends BaseApiClient {
    @Step("Создать пользователя")
    public Response createUser(CreateUserRequest createUserRequest) {
        return getPostSpec()
                .body(createUserRequest)
                .when()
                .post(REGISTER_URL);
    }

    @Step("Удалить пользователя")
    public Response deleteUser (String bearerToken){
        return getPostSpec()
                .headers(
                        "Authorization",
                        bearerToken)
                .delete(USER_URL);
    }

    @Step("Авторизация")
    public Response loginUser (LoginUserRequest loginUserRequest){
        return getPostSpec()
                .body(loginUserRequest)
                .when()
                .post(LOGIN_USER_URL);
    }
    @Step("Обновить данные пользователя")
    public Response updateUser (String bearerToken, CreateUserRequest userRequest){
        return getPostSpec()
            .headers(
                    "Authorization",
                    bearerToken)
                .body(userRequest)
                .patch(USER_URL);
    }
    @Step("Получить информацию о пользователе")
    public Response getUser (String bearerToken){
        return getPostSpec()
                .headers(
                        "Authorization",
                        bearerToken)
                .get(USER_URL);
    }
}
