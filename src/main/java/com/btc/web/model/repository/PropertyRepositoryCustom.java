package com.btc.web.model.repository;

import com.btc.web.model.Properties;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Eric on 10/22/14.
 */
@Repository
public interface PropertyRepositoryCustom {
    Map<String, Object> propertiesAsMap();
    List<Properties> findAllAdminAccessible();
}
