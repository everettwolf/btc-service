package com.btc.web.model.repository;

import com.btc.web.model.Properties;
import com.btc.web.props.IProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Eric on 10/22/14.
 */
@Repository
public interface PropertyRepository extends IProperty, PropertyRepositoryCustom, CrudRepository<Properties, Long> {
}
