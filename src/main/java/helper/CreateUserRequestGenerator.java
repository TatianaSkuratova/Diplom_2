package helper;
import models.CreateUserRequest;
import net.datafaker.Faker;

public class CreateUserRequestGenerator {
    public static CreateUserRequest getRandomUser(){
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().firstName();
        return new CreateUserRequest.Builder().email(email).password(password).name(name).build();
    }

    public static CreateUserRequest getRandomUserWithoutEmail(){
        Faker faker = new Faker();
        String password = faker.internet().password();
        String name = faker.name().firstName();
        return new CreateUserRequest.Builder().password(password).name(name).build();
    }
    public static CreateUserRequest getRandomUserWithoutPassword(){
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String name = faker.name().firstName();
        return new CreateUserRequest.Builder().email(email).name(name).build();
    }
    public static CreateUserRequest getRandomUserWithoutName(){
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        return new CreateUserRequest.Builder().email(email).password(password).build();
    }
}
