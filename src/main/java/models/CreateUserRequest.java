package models;
import lombok.Data;
@Data
public class CreateUserRequest {
    private String email;
    private String password;
    private String name;


    public static class Builder{
        private CreateUserRequest newUserRequest;
        public Builder(){
            newUserRequest = new CreateUserRequest();
        }
        public Builder email(String email){
            newUserRequest.email = email;
            return this;
        }
        public Builder password(String password){
            newUserRequest.password = password;
            return this;
        }
        public Builder name(String name){
            newUserRequest.name = name;
            return this;
        }
        public CreateUserRequest build(){
            return newUserRequest;
        }
    }
}
