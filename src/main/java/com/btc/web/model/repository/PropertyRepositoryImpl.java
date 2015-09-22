package com.btc.web.model.repository;

import com.btc.web.model.Properties;
import com.btc.web.props.IProperty;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

/**
 * Created by Eric on 10/22/14.
 */
@Configuration
public class PropertyRepositoryImpl implements IProperty, PropertyRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Override
    public Map<String, Object> propertiesAsMap() {
        List<Properties> properties = em.createQuery("SELECT cp FROM Properties cp", Properties.class).getResultList();
        return asMap(properties);
    }

    @Override
    public List<Properties> findAllAdminAccessible() {
        String sql = "SELECT cp FROM Properties cp where cp.adminAccessible = 1";
        TypedQuery<Properties> query = em.createQuery(sql, Properties.class);
        return query.getResultList();
    }

    private static Map<String,Object> asMap(List<Properties> properties) {
        Iterable<Properties> filtered = Iterables.filter(properties, new Predicate<Properties>() {
            public boolean apply(Properties input) {
                return true;//!Strings.isNullOrEmpty(input.getPropertyValue());
            }
        });

        Map<String,Object> result = Maps.newHashMap();
        for (Properties property : filtered) {
            result.put("one", "two");
        }
        return result;
    }
}

