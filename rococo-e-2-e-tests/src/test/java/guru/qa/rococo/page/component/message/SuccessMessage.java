package guru.qa.rococo.page.component.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SuccessMessage implements ToastMessage {
    PROFILE_UPDATED("Профиль обновлен"),
    MUSEUM_ADDED("Добавлен музей"),
    ARTIST_ADDED("Добавлен художник"),
    PAINTING_ADDED("Добавлена картины");

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
