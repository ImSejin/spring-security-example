package io.github.imsejin.example.googleotp.common.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import io.github.imsejin.example.googleotp.Application;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Cannot refer to both {@link SqlSessionTemplate} and {@link SqlSessionFactory} together.
 * If you do, {@link SqlSessionFactory} is ignored.
 */
@Slf4j
@org.springframework.context.annotation.Configuration
@MapperScan(annotationClass = Mapper.class, basePackageClasses = Application.class, sqlSessionFactoryRef = "sqlSessionFactory")
class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    static DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean("sqlSessionFactory")
    static SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        // Supports column names with underscore in a result set to be converted with camelcase.
        Configuration configuration = new Configuration();
        configuration.setMapUnderscoreToCamelCase(true);

        factoryBean.setDataSource(dataSource);
        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();
    }

}
