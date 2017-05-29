package com.qiqi.config.datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * druid配置
 * 
 * @author qc_zhong
 */
@Configuration
@Data
@Slf4j
public class DruidConfig {
	@Value("${datasource.url}")
	private String url;

	@Value("${datasource.username}")
	private String username;

	@Value("${datasource.password}")
	private String password;

	@Value("${datasource.driverClassName}")
	private String driverClassName;

	@Value("${datasource.initialSize}")
	private int initialSize;

	@Value("${datasource.minIdle}")
	private int minIdle;

	@Value("${datasource.maxActive}")
	private int maxActive;

	@Value("${datasource.maxWait}")
	private int maxWait;

	@Value("${datasource.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;

	@Value("${datasource.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;

	@Value("${datasource.validationQuery}")
	private String validationQuery;

	@Value("${datasource.testWhileIdle}")
	private boolean testWhileIdle;

	@Value("${datasource.testOnBorrow}")
	private boolean testOnBorrow;

	@Value("${datasource.testOnReturn}")
	private boolean testOnReturn;

	@Value("${datasource.poolPreparedStatements}")
	private boolean poolPreparedStatements;

	@Value("${datasource.maxPoolPreparedStatementPerConnectionSize}")
	private int maxPoolPreparedStatementPerConnectionSize;

	@Value("${datasource.filters}")
	private String filters;

	@Value("${datasource.connectionProperties}")
	private String connectionProperties;

	@Bean
	@Primary
	public DataSource dataSource() {
		DruidDataSource datasource = new DruidDataSource();

		datasource.setUrl(this.url);
		datasource.setUsername(this.username);
		datasource.setPassword(this.password);
		datasource.setDriverClassName(this.driverClassName);
		datasource.setInitialSize(this.initialSize);
		datasource.setMinIdle(this.minIdle);
		datasource.setMaxActive(this.maxActive);
		datasource.setMaxWait(this.maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
		datasource.setValidationQuery(this.validationQuery);
		datasource.setTestWhileIdle(this.testWhileIdle);
		datasource.setTestOnBorrow(this.testOnBorrow);
		datasource.setTestOnReturn(this.testOnReturn);
		datasource.setPoolPreparedStatements(this.poolPreparedStatements);
		datasource.setMaxPoolPreparedStatementPerConnectionSize(this.maxPoolPreparedStatementPerConnectionSize);
		try {
			datasource.setFilters(this.filters);
		} catch (SQLException e) {
			log.error("druid configuration initialization filter", e);
		}
		datasource.setConnectionProperties(this.connectionProperties);

		return datasource;

	}

	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*");
		reg.addInitParameter("loginUsername", "admin");
		reg.addInitParameter("loginPassword", "666666");
		return reg;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		filterRegistrationBean.addInitParameter("profileEnable", "true");
		filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
		filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
		return filterRegistrationBean;
	}
}