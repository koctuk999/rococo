package guru.qa.rococo.db.repository.country;

import guru.qa.rococo.db.hibernate.JpaService;
import guru.qa.rococo.db.hibernate.ThreadLocalEntityManager;
import guru.qa.rococo.db.model.CountryEntity;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static guru.qa.rococo.db.Database.COUNTRY;
import static guru.qa.rococo.db.hibernate.EmfProvider.INSTANCE;

public class CountryRepositoryHibernate extends JpaService implements CountryRepository {
    public CountryRepositoryHibernate() {
        super(Map.of(COUNTRY, new ThreadLocalEntityManager(INSTANCE.emf(COUNTRY))));
    }

    @Override
    public CountryEntity findCountryById(UUID id) {
        return entityManager(COUNTRY).find(CountryEntity.class, id);
    }

    @Override
    public List<CountryEntity> findAllCountries() {
        TypedQuery<CountryEntity> query = entityManager(COUNTRY).createQuery("SELECT c FROM CountryEntity c", CountryEntity.class);
        return query.getResultList();

    }
}
