package org.gridftp.app.impl;

import org.apache.felix.scr.annotations.*;
import org.gridftp.app.GridftpAppInfo;
import org.gridftp.app.GridftpStore;
import org.onlab.util.KryoNamespace;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.store.AbstractStore;
import org.onosproject.store.serializers.KryoNamespaces;
import org.onosproject.store.service.ConsistentMap;
import org.onosproject.store.service.Serializer;
import org.onosproject.store.service.StorageService;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Implementation of the GridFTP store service.
 */
@Component(immediate = true)
@Service
public class DistributedGridftpStore extends AbstractStore implements GridftpStore {

    private final Logger log = getLogger(getClass());

    private ConsistentMap<String, GridftpAppInfo> gridftpAppInfoDict;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected StorageService storageService;
    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected CoreService coreService;

    @Activate
    public void activate() {
        ApplicationId appId = coreService.getAppId("org.gridftp.app");


        gridftpAppInfoDict = storageService.<String, GridftpAppInfo>consistentMapBuilder()
                .withSerializer(Serializer.using(serializer.build()))
                .withName("gridftp-app-info-dict")
                .withApplicationId(appId)
                .withPurgeOnUninstall()
                .build();
    }

}
