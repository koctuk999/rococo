package guru.qa.rococo.config;

import guru.qa.rococo.model.KafkaUserJson;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfiguration {


  private final KafkaProperties kafkaProperties;

  @Autowired
  public KafkaConsumerConfiguration(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }

  @Bean
  public ConsumerFactory<String, KafkaUserJson> consumerFactory() {
    final JsonDeserializer<KafkaUserJson> jsonDeserializer = new JsonDeserializer<>();
    jsonDeserializer.addTrustedPackages("*");
    return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, KafkaUserJson> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, KafkaUserJson> concurrentKafkaListenerContainerFactory
        = new ConcurrentKafkaListenerContainerFactory<>();
    concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
    return concurrentKafkaListenerContainerFactory;
  }
}
