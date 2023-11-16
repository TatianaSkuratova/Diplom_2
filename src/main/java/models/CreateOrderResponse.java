package models;

import lombok.Data;

@Data
public class CreateOrderResponse {
    private Boolean success;
    private String name;
    private Order order;

}
