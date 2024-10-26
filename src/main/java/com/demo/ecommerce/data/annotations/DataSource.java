package com.demo.ecommerce.data.annotations;
import com.demo.ecommerce.data.enums.DataSourceType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface DataSource {
    DataSourceType value() default DataSourceType.MASTER;
}