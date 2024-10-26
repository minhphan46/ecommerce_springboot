package com.demo.ecommerce.data.interceptors;
import com.demo.ecommerce.data.annotations.DataSource;
import com.demo.ecommerce.data.config.DynamicDataSourceHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Aspect for DataSource Annotation. Sets the data source in the thread
 */

@Aspect
@Component
public class DataSourceAspect {

    @Before(value = "@annotation(dataSource)")
    public void dataSourcePoint(JoinPoint joinPoint, DataSource dataSource) {
        DynamicDataSourceHolder.putDataSource(dataSource.value());
    }
}