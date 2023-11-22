package models;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@AllArgsConstructor
@Data
public class CreateOrderRequest {
    private ArrayList<String> ingredients;

}
