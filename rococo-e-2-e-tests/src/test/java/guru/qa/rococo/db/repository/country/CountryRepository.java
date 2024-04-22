package guru.qa.rococo.db.repository.country;

import guru.qa.rococo.db.model.CountryEntity;

import java.util.List;
import java.util.UUID;

public interface CountryRepository {

   CountryEntity findCountryById(UUID id);

   List<CountryEntity> findAllCountries();
}
