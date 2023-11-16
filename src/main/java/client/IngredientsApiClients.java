package client;

import io.restassured.response.Response;

import static config.ConfigApp.BASE_URL;
import static config.ConfigApp.INGREDIENTS_URL;

public class IngredientsApiClients extends BaseApiClient{

    public Response getIngredients(){
        return getPostSpec()
                .get(BASE_URL + INGREDIENTS_URL);
    }

}
