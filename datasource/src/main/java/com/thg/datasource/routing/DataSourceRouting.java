package com.thg.datasource.routing;

import lombok.Data;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by tanhuigen
 * Date 2022-09-03
 * Description
 */

@Data
public class DataSourceRouting extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceKeyHolder.get();
    }

}
