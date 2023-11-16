package models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Order {
    String _id;
    List<Ingredient> ingredients;
    String status;
    User owner;
}
