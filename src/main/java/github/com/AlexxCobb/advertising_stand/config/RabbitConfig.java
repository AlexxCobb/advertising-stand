package github.com.AlexxCobb.advertising_stand.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${queue.name}")
    private String queueName;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue productQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "product.dlx")
                .withArgument("x-dead-letter-routing-key", "product.dead")
                .build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("product.dlx");
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("product.dead.queue").build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("product.dead");
    }
}
