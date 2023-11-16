package models;

import lombok.Data;

@Data
public class Error {
    public final static String MESSAGE_DUPLICATE_USER = "User already exists";
    public final static String MESSAGE_REQUIRED_FIELDS = "Email, password and name are required fields";
    public final static String MESSAGE_WRONG_CREDENTIALS = "email or password are incorrect";
    public final static String MESSAGE_AUTHORISED = "You should be authorised";
    public final static String MESSAGE_EMPTY_INGREDIENTS = "Ingredient ids must be provided";
    public final static String MESSAGE_INCORRECT_INGREDIENTS = "One or more ids provided are incorrect";

    Boolean success;
    String message;
}
