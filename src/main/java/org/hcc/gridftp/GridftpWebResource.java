package org.hcc.gridftp;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onosproject.rest.AbstractWebResource;
import com.fasterxml.jackson.databind.JsonNode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
import static org.onlab.util.Tools.nullIsNotFound;

/**
 * Manage the mapping between ip/port and GridFTP file transfer application level info
 */
@Path("gridftp")
public class GridftpWebResource extends AbstractWebResource {
    /**
     * Get the GridFTP application level information with IP/Port
     *
     * @param ipAddr host IP address
     * @param port host port
     * @return 200 OK
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{ipAddr}/{port}")
    public Response queryApplicationLevelInfo(@PathParam("ipAddr") String ipAddr,
                                              @PathParam("port") String port) {
        GridftpService service = get(GridftpService.class);
        final GridftpAppInfo appInfo = service.getAppLevelInfo(ipAddr, port);
        final ObjectNode root = codec(GridftpAppInfo.class).encode(appInfo, this);
        return Response.ok(root).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addApplicationLevelInfo(InputStream stream) throws URISyntaxException {
        GridftpAppInfo newAppInfo = jsonToAppInfo(stream);
        return get(GridftpService.class).addApplicationLevelInfo(newAppInfo) ?
                Response.ok().build():
                Response.serverError().build();
    }

    /**
     * Remove Gridftp application level info for an ip + port combination
     */
    @DELETE
    @Path("{ipAddr}/{port}")
    public Response removeAppLevelInfo(@PathParam("ipAddr") String ipAddr,
                                       @PathParam("port") String port) {
        get(GridftpService.class).removeAppLevelInfo(ipAddr, port);
        return Response.ok().build();
    }


    /**
     * Remove the GridFTP application level info dictionary.
     *
     * return 200 OK
     */
    @DELETE
    public Response clearGridftpAppInfoDict() {
        get(GridftpService.class).clearGridftpAppInfoDict();
        return Response.ok().build();
    }

    /**
     * Turns a JSON string into an GridftpAppInfo instance.
     */
    private GridftpAppInfo jsonToAppInfo(InputStream stream) {
        JsonNode node;
        try {
            node = mapper().readTree(stream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to parse Gridftp app info request", e);
        }

        String ipAddr = node.path("ipAddr").asText(null);
        String port = node.path("port").asText(null);
        String username = node.path("username").asText(null);
        String filename = node.path("filename").asText(null);
        String direction = node.path("direction").asText(null);

        if (ipAddr!=null && port!=null && username!=null && filename!=null && direction!=null) {
            return new GridftpAppInfo(ipAddr, port, username, filename, direction);
        }
        else {
            throw new IllegalArgumentException("arguments must not be null.");
        }
    }
}
