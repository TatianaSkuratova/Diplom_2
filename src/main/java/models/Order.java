package models;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Order {
    private String _id;
    private List<Ingredient> ingredients;
    private String status;
    private User owner;
}
