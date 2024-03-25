package guru.qa.rococo.service;

import guru.qa.grpc.rococo.grpc.User;
import guru.qa.rococo.data.UserEntity;
import guru.qa.rococo.data.repository.UserRepository;
import guru.qa.rococo.model.KafkaUserJson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "users", groupId = "userdata")
    public void kafkaListener(@Payload KafkaUserJson user) {
        LOG.info("### Kafka topic [users] received message: " + user.username());
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.username());
        userRepository.save(userEntity);
        LOG.info(String.format(
                "### User '%s' successfully saved to database with id: %s",
                user.username(),
                userEntity.getId()
        ));
    }

    //TODO добавить исключение если не найден
    public User getUser(String username) {
        return userRepository
                .findByUsername(username)
                .toGrpc();
    }
}
