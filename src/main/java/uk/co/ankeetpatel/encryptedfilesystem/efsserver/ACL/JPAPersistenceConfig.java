package uk.co.ankeetpatel.encryptedfilesystem.efsserver.ACL;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA Base Config
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository")
@PropertySource("classpath:application.properties")
@EntityScan(basePackages={ "uk.co.ankeetpatel.encryptedfilesystem.efsserver.models" })
public class JPAPersistenceConfig {

}