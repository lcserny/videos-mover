package net.cserny.videos.mover.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

/**
 * Created by leonardo on 01.07.2017.
 */
@Configuration
@ComponentScan
@EnableJpaRepositories("net.cserny.videos.mover.dao")
@PropertySource("classpath:db-config.properties")
public class DatabaseConfig
{
    @Bean
    public DataSource dataSource() {
        return (new EmbeddedDatabaseBuilder()).addScript("classpath:schema.sql").build();
    }
}
