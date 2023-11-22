package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static config.ConfigApp.BASE_URL;
import static config.ConfigApp.INGREDIENTS_URL;
public class IngredientsApiClients extends BaseApiClient{

    @Step("Получить список ингредиентов")
    public Response getIngredients(){
        return getPostSpec()
                .get(INGREDIENTS_URL);
    }

}
