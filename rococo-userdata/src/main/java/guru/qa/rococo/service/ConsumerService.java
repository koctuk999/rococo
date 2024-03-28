package guru.qa.rococo.service;

import guru.qa.rococo.data.UserEntity;
import guru.qa.rococo.data.repository.UserRepository;
import guru.qa.rococo.model.KafkaUserJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    private static final Logger LOG = LoggerFactory.getLogger(ConsumerService.class);

    private final UserRepository userRepository;

    @Autowired
    public ConsumerService(UserRepository userRepository) {
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
}
