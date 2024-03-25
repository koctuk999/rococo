package guru.qa.rococo.model;

import guru.qa.rococo.data.MuseumEntity;

import java.util.UUID;

public class MuseumEntityBuilder {
    private UUID id;
    private String title;
    private String description;
    private byte[] photo;
    private String city;
    private UUID countryId;

    public MuseumEntityBuilder setId(UUID id) {
        this.id = id;
        return this;
    }

    public MuseumEntityBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public MuseumEntityBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public MuseumEntityBuilder setPhoto(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public MuseumEntityBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    public MuseumEntityBuilder setCountryId(UUID countryId) {
        this.countryId = countryId;
        return this;
    }

    public MuseumEntity build() {
        MuseumEntity museumEntity = new MuseumEntity();
        museumEntity.setId(this.id);
        museumEntity.setTitle(this.title);
        museumEntity.setDescription(this.description);
        museumEntity.setPhoto(this.photo);
        museumEntity.setCity(this.city);
        museumEntity.setCountryId(this.countryId);
        return museumEntity;
    }
}
