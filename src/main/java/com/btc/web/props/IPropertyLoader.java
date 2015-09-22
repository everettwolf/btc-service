package com.btc.web.props;

import java.util.List;

/**
 * Created by Eric on 5/26/15.
 */
public interface IPropertyLoader {
    public List<IPropertyKey> loadProjectSpecificList();
}
