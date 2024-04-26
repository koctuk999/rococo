package guru.qa.rococo.db.repository.artist;

import guru.qa.rococo.db.hibernate.JpaService;
import guru.qa.rococo.db.hibernate.ThreadLocalEntityManager;
import guru.qa.rococo.db.model.ArtistEntity;
import io.qameta.allure.Step;
import jakarta.persistence.TypedQuery;
import lombok.SneakyThrows;

import java.util.UUID;

import static guru.qa.rococo.db.Database.ARTIST;
import static guru.qa.rococo.db.hibernate.EmfProvider.INSTANCE;
import static guru.qa.rococo.utils.Helper.attempt;
import static java.util.Map.of;

public class ArtistRepositoryHibernate extends JpaService implements ArtistRepository {
    public ArtistRepositoryHibernate() {
        super(of(ARTIST, new ThreadLocalEntityManager(INSTANCE.emf(ARTIST))));
    }

    @Override
    @SneakyThrows
    @Step("Find artist by title {0}")
    public ArtistEntity findArtistByName(String name) {
        TypedQuery<ArtistEntity> query = entityManager(ARTIST)
                .createQuery(
                        "SELECT a FROM ArtistEntity a WHERE a.name = :name",
                        ArtistEntity.class
                )
                .setParameter("name", name);
        return attempt(
                10,
                1000,
                () -> query.getSingleResult()
        );
    }

    @Override
    @Step("Find artist by id {0}")
    public ArtistEntity findArtistById(UUID id) {
        return entityManager(ARTIST).find(ArtistEntity.class, id);
    }
}
