package guru.qa.rococo.core.extensions;

import guru.qa.rococo.core.annotations.CreatedUser;
import guru.qa.rococo.core.annotations.LoggedIn;
import guru.qa.rococo.db.model.*;
import guru.qa.rococo.db.repository.user.UserRepository;
import guru.qa.rococo.db.repository.user.UserRepositoryHibernate;
import guru.qa.rococo.utils.RandomUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.Optional;

public class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    private final UserRepository userRepository = new UserRepositoryHibernate();
    public final static Namespace CREATE_USER_NAMESPACE = Namespace.create(CreateUserExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        CreatedUser annotationData = null;
        Optional<LoggedIn> loggedInAnnotation = AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), LoggedIn.class);
        Optional<CreatedUser> createdUserAnnotation = AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), CreatedUser.class);

        if (loggedInAnnotation.isPresent()) {
            annotationData = loggedInAnnotation.get().user();
        } else if (createdUserAnnotation.isPresent()) {
            annotationData = createdUserAnnotation.get();
        }

        if (annotationData != null) {
            String username = annotationData.username().isEmpty()
                    ? RandomUtils.genRandomUsername()
                    : annotationData.username();

            String password = annotationData.password().isEmpty()
                    ? "12345"
                    : annotationData.password();

            UserAuthEntity userAuthEntity = new UserAuthEntity();
            userAuthEntity.setUsername(username);
            userAuthEntity.setPassword(password);
            userAuthEntity.setAccountNonLocked(true);
            userAuthEntity.setAccountNonExpired(true);
            userAuthEntity.setEnabled(true);
            userAuthEntity.setCredentialsNonExpired(true);

            AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
                    a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(a);
                        return ae;
                    }
            ).toArray(AuthorityEntity[]::new);
            userAuthEntity.addAuthorities(authorities);

            UserDataEntity userDataEntity = UserDataEntity
                    .builder()
                    .username(username)
                    .build();

            UserAuthEntity userAuth = userRepository.createInAuth(userAuthEntity);
            UserDataEntity userData = userRepository.createInData(userDataEntity);

            extensionContext
                    .getStore(CREATE_USER_NAMESPACE)
                    .put(extensionContext.getUniqueId(),
                            new TestUser(
                                    userAuth.getUsername(),
                                    password,
                                    userData.getFirstname(),
                                    userData.getLastname(),
                                    userData.getAvatar(),
                                    userAuth.getId(),
                                    userData.getId()
                            )
                    );
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(TestUser.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(CREATE_USER_NAMESPACE)
                .get(extensionContext.getUniqueId(), TestUser.class);
    }
}
