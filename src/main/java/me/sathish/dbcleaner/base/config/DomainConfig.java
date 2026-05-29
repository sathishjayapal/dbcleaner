package me.sathish.dbcleaner.base.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("me.sathish.dbcleaner.base")
@EnableJpaRepositories("me.sathish.dbcleaner.base")
@EnableTransactionManagement
public class DomainConfig {
}
