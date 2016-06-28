package org.hcc.gridftp;

import org.onlab.rest.AbstractWebApplication;
import java.util.Set;

/**
 * GridFTP app REST API web application
 */
public class GridftpWebApplication extends AbstractWebApplication{
    @Override
    public Set<Class<?>> getClasses() {
        return getClasses(GridftpWebResource.class);
    }
}
