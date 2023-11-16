package client;

import io.restassured.response.Response;
import models.CreateUserRequest;
import models.LoginUserRequest;

import static config.ConfigApp.*;
import static io.restassured.RestAssured.given;

public class UserApiClient extends BaseApiClient {
    public Response createUser(CreateUserRequest createUserRequest) {
        return getPostSpec()
                .body(createUserRequest)
                .when()
                .post(BASE_URL + REGISTER_URL);
    }

    public Response deleteUser (String bearerToken){
        return getPostSpec()
                .headers(
                        "Authorization",
                        bearerToken)
                .delete(BASE_URL + USER_URL);
    }

    public Response loginUser (LoginUserRequest loginUserRequest){
        return getPostSpec()
                .body(loginUserRequest)
                .when()
                .post(BASE_URL + LOGIN_USER_URL);
    }
    public Response updateUser (String bearerToken, CreateUserRequest userRequest){
        return getPostSpec()
            .headers(
                    "Authorization",
                    bearerToken)
                .body(userRequest)
                .patch(BASE_URL + USER_URL);
    }
    public Response getUser (String bearerToken){
        return getPostSpec()
                .headers(
                        "Authorization",
                        bearerToken)
                .get(BASE_URL + USER_URL);
    }
}
