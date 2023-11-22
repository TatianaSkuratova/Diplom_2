package models;
public class LoginUserRequest {
    private String email;
    private String password;

    public static LoginUserRequest fromCreateUserRequest(CreateUserRequest userRequest){
        return new Builder()
                .withEmail(userRequest.getEmail())
                .withPassword(userRequest.getPassword())
                .build();
    }

    public static class Builder{
        private LoginUserRequest newCredential;
        public Builder(){
            newCredential = new LoginUserRequest();
        }
        public Builder withEmail(String email){
            newCredential.email = email;
            return this;
        }
        public Builder withPassword(String password){
            newCredential.password = password;
            return this;
        }
        public LoginUserRequest build(){
            return newCredential;
        }
    }
}
