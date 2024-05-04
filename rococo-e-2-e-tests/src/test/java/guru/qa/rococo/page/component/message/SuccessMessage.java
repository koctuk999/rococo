package guru.qa.rococo.page.component.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SuccessMessage implements Message {
    PROFILE_UPDATED("Профиль обновлен"),
    MUSEUM_ADDED("Добавлен музей: %s"),
    MUSEUM_UPDATED("Обновлен музей: %s"),
    ARTIST_ADDED("Добавлен художник: %s"),
    ARTIST_UPDATED("Обновлен художник: %s"),
    PAINTING_ADDED("Добавлена картина: %s"),
    PAINTING_UPDATED("Обновлена картина: %s");

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
