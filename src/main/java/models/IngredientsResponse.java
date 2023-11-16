package models;

import lombok.Data;

import java.util.ArrayList;


@Data
public class IngredientsResponse {
    private  Boolean success;
    private ArrayList<Ingredient> data;
}
