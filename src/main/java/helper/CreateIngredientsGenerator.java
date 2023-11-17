package helper;
import models.CreateOrderRequest;
import models.Ingredient;

import java.util.ArrayList;
import java.util.Random;

public class CreateIngredientsGenerator {

    public static CreateOrderRequest randomIngredients(ArrayList<Ingredient> listIngredients, int countOfIngredients){
        ArrayList<String> idIngredients = new ArrayList<>();
        ArrayList<String> buns = new ArrayList<>();
        ArrayList<String> fillings = new ArrayList<>();
        for (Ingredient ingredients: listIngredients
             ) {
                if (ingredients.getName().contains("булка")){
                    buns.add(ingredients.get_id());
                }else {
                    fillings.add(ingredients.get_id());
                };
        }
        //добавляем булку
        idIngredients.add(buns.get(new Random().nextInt(buns.size())));
        //добавляем ингредиенты
        for (int i=0; i< countOfIngredients; i ++) {
            idIngredients.add(fillings.get(new Random().nextInt(fillings.size())));
        }
        return new CreateOrderRequest(idIngredients);
    };
    public static CreateOrderRequest emptyIngredients(){
        ArrayList<String> idIngredients = new ArrayList<>();
        return new CreateOrderRequest(idIngredients);
    };
    public static CreateOrderRequest fakeIngredients(){
        ArrayList<String> idIngredients = new ArrayList<>();
        idIngredients.add("61c0c5a71d1f82001bdaaa00");
        idIngredients.add("61c0c5a71d1f82001bdaaa01");
        return new CreateOrderRequest(idIngredients);
    };

}
