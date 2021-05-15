package com.example.LabOneBusinessLogic;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import com.example.LabOneBusinessLogic.entity.Users;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.example.LabOneBusinessLogic"})
public class DbConfig
{
    JtaTransactionManager jtaTransactionManager;
    @Bean(name = "dataSourceOne")
    public DataSource dataSourceOne() {
        PoolingDataSource ds = new PoolingDataSource();

        ds.setClassName("org.postgresql.xa.PGXADataSource");
        ds.setUniqueName("ds1");
        ds.setMaxPoolSize(10);
        Properties props = new Properties();
        props.put("url", "jdbc:postgresql://localhost:5500/studs");
        props.put("user", "s264431");
        props.put("password", "****");

        ds.setDriverProperties(props);
        ds.setAllowLocalTransactions(true);

        ds.init();
        return ds;
    }
    @Bean(destroyMethod = "shutdown")
    public BitronixTransactionManager bitronixManager() {
        return TransactionManagerServices.getTransactionManager();
    }


    @Bean(name="transactionManager")
    public JtaTransactionManager jtaTransactionManager() {
        JtaTransactionManager jta = new JtaTransactionManager();
        jta.setTransactionManager(bitronixManager());
        jta.setUserTransaction(bitronixManager());
        jtaTransactionManager=jta;
        return jta;
    }
    @Bean
    public TransactionTemplate transactionTemplate() {
        TransactionTemplate transactionTemplate= new TransactionTemplate(jtaTransactionManager());
        return transactionTemplate;
    }

}
