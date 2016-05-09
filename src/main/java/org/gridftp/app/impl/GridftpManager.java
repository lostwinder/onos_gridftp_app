package org.gridftp.app.impl;

import org.gridftp.app.GridftpAppInfo;
import org.gridftp.app.GridftpService;
import org.gridftp.app.GridftpStore;
import org.onosproject.core.IdGenerator;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onosproject.core.CoreService;
import org.gridftp.app.GridftpService;
import org.onosproject.core.ApplicationId;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Gridftp app service implementation
 */

@Component(immediate = true)
@Service
public class GridftpManager implements GridftpService {

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected GridftpStore gridftpStore;

    private final Logger log = getLogger(getClass());
    private ApplicationId appId;
    private IdGenerator idGenerator;



    @Activate
    public void activate() {
        appId = coreService.registerApplication("org.gridftp.app");
        idGenerator = coreService.getIdGenerator("gridftp-ids");
        log.info("Started");

    }

    @Deactivate
    public void deactivate() {
        log.info("Stopped");
    }

    @Override
    public GridftpAppInfo getAppLevelInfo(String ipAddr, String port) {
        return gridftpStore.getAppLevelInfo(ipAddr, port);
    }

    @Override
    public boolean addApplicationLevelInfo(GridftpAppInfo newAppInfo) {
        if (newAppInfo == null) {
            return false;
        }
        gridftpStore.addApplicationLevelInfo(newAppInfo);
        log.info("New GridFTP file transfer application level information for {} is added.", newAppInfo.ipAddrPort());
        return true;
    }

    @Override
    public void removeAppLevelInfo(String ipAddr, String port) {
        gridftpStore.removeAppLevelInfo(ipAddr, port);
        log.info("GridFTP application level information for IP:{} and port:{} is removed.", ipAddr, port);
    }

    @Override
    public void clearGridftpAppInfoDict() {
        gridftpStore.clearGridftpAppInfoDict();
        log.info("GridFTP application level info dictionary is cleared.");
    }

}