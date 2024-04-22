package guru.qa.rococo.db.repository.museum;

import guru.qa.rococo.db.hibernate.JpaService;
import guru.qa.rococo.db.hibernate.ThreadLocalEntityManager;
import guru.qa.rococo.db.model.MuseumEntity;
import guru.qa.rococo.utils.Helper;
import io.qameta.allure.Step;
import jakarta.persistence.TypedQuery;
import lombok.SneakyThrows;

import static guru.qa.rococo.db.Database.MUSEUM;
import static guru.qa.rococo.db.hibernate.EmfProvider.INSTANCE;
import static guru.qa.rococo.utils.Helper.retryAction;
import static java.util.Map.of;

public class MuseumRepositoryHibernate extends JpaService implements MuseumRepository {
    public MuseumRepositoryHibernate() {
        super(of(MUSEUM, new ThreadLocalEntityManager(INSTANCE.emf(MUSEUM))));
    }

    @Override
    @SneakyThrows
    @Step("Find museum by title {0}")
    public MuseumEntity findMuseumByTitle(String title) {
        TypedQuery<MuseumEntity> query = entityManager(MUSEUM)
                .createQuery(
                        "SELECT m FROM MuseumEntity m WHERE m.title = :title",
                        MuseumEntity.class
                )
                .setParameter("title", title);
        return retryAction(
                10,
                1000,
                () -> query.getSingleResult()
        );
    }
}
