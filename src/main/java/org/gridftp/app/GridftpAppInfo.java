package org.gridftp.app;

/**
 * Class for GridFTP file transfer application level information
 */
public class GridftpAppInfo {
    private String username;
    private String filename;
    private String direction;

    public GridftpAppInfo(String username, String filename, String direction) {
        this.username = username;
        this.filename = filename;
        this.direction = direction;
    }
}
