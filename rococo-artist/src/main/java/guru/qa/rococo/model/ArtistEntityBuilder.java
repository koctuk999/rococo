package guru.qa.rococo.model;

import guru.qa.rococo.data.ArtistEntity;

import java.util.UUID;

public class ArtistEntityBuilder {
    private UUID id;
    private String name;
    private String biography;
    private byte[] photo;

    public ArtistEntityBuilder setId(UUID id) {
        this.id = id;
        return this;
    }

    public ArtistEntityBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ArtistEntityBuilder setBiography(String biography) {
        this.biography = biography;
        return this;
    }

    public ArtistEntityBuilder setPhoto(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public ArtistEntity build() {
        ArtistEntity artist = new ArtistEntity();
        artist.setId(this.id);
        artist.setName(this.name);
        artist.setBiography(this.biography);
        artist.setPhoto(this.photo);
        return artist;
    }
}
