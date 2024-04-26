package guru.qa.rococo.page.component.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorMessage implements Message {
    USERNAME_ALREADY_EXISTS("Username `%s` already exists"),
    PASSWORD_NOT_EQUAL("Passwords should be equal"),
    LOGIN_ERROR("Неверные учетные данные пользователя"),
    PASSWORD_NOT_VALID("Allowed password length should be from 3 to 12 characters");


    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
