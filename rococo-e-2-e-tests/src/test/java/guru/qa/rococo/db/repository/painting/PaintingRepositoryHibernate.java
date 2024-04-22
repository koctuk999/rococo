package guru.qa.rococo.db.repository.painting;

import guru.qa.rococo.db.hibernate.JpaService;
import guru.qa.rococo.db.hibernate.ThreadLocalEntityManager;
import guru.qa.rococo.db.model.PaintingEntity;
import io.qameta.allure.Step;
import jakarta.persistence.TypedQuery;
import lombok.SneakyThrows;

import static guru.qa.rococo.db.Database.PAINTING;
import static guru.qa.rococo.db.hibernate.EmfProvider.INSTANCE;
import static guru.qa.rococo.utils.Helper.retryAction;
import static java.util.Map.of;

public class PaintingRepositoryHibernate extends JpaService implements PaintingRepository {
    public PaintingRepositoryHibernate() {
        super(of(PAINTING, new ThreadLocalEntityManager(INSTANCE.emf(PAINTING))));
    }

    @Override
    @SneakyThrows
    @Step("Find painting by title {0}")
    public PaintingEntity findPaintingByTitle(String title) {
        TypedQuery<PaintingEntity> query = entityManager(PAINTING)
                .createQuery(
                        "SELECT p FROM PaintingEntity p WHERE p.title = :title",
                        PaintingEntity.class
                )
                .setParameter("title", title);
        return retryAction(
                10,
                1000,
                () -> query.getSingleResult()
        );
    }
}
