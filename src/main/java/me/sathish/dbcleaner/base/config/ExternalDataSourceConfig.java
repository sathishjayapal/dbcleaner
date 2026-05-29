package me.sathish.dbcleaner.base.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class ExternalDataSourceConfig {

    @Bean
    @Qualifier("runsAppJdbc")
    public JdbcTemplate runsAppJdbc(
            @Value("${app.datasources.runs-app.url}") String url,
            @Value("${app.datasources.runs-app.username}") String username,
            @Value("${app.datasources.runs-app.password}") String password) {
        return new JdbcTemplate(buildDataSource(url, username, password, "runs-app-pool"));
    }

    @Bean
    @Qualifier("eventsTrackerJdbc")
    public JdbcTemplate eventsTrackerJdbc(
            @Value("${app.datasources.eventstracker.url}") String url,
            @Value("${app.datasources.eventstracker.username}") String username,
            @Value("${app.datasources.eventstracker.password}") String password) {
        return new JdbcTemplate(buildDataSource(url, username, password, "eventstracker-pool"));
    }

    @Bean
    @Qualifier("runsAiJdbc")
    public JdbcTemplate runsAiJdbc(
            @Value("${app.datasources.runs-ai.url}") String url,
            @Value("${app.datasources.runs-ai.username}") String username,
            @Value("${app.datasources.runs-ai.password}") String password) {
        return new JdbcTemplate(buildDataSource(url, username, password, "runs-ai-pool"));
    }

    private DataSource buildDataSource(String url, String username, String password, String poolName) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setPoolName(poolName);
        ds.setMaximumPoolSize(3);
        ds.setConnectionTimeout(30000);
        return ds;
    }

}
