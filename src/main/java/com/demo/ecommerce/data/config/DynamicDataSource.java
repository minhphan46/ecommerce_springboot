package com.demo.ecommerce.data.config;

import com.demo.ecommerce.data.enums.DataSourceType;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Overridden default db to allow support for datasource routing
 */

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected String determineCurrentLookupKey() {
        if (DynamicDataSourceHolder.getDataSource() != null) {
            return DynamicDataSourceHolder.getDataSource();
        }
        return DataSourceType.MASTER.getType();

    }
}