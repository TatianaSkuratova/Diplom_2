package models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UpdateUserResponse {
       private Boolean success;
       private User user;
}
