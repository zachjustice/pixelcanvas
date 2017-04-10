package canvas.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RethinkDBConfiguration {
    // connect to docker
    public static final String DBHOST = "localhost";
    public static final int PORT = 28015;


    @Bean
    public RethinkDBConnectionFactory connectionFactory() {
        return new RethinkDBConnectionFactory(DBHOST, PORT);
    }

    @Bean
    DbInitializer dbInitializer() {
        return new DbInitializer();
    }
}