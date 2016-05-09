package org.gridftp.app;

import jdk.internal.util.xml.impl.Input;
import org.onosproject.rest.AbstractWebResource;
import com.fasterxml.jackson.databind.JsonNode;
import javax.ws.rs.Path;
import javax.ws.rs.DELETE;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
/**
 * Manage the mapping between ip/port and GridFTP file transfer application level info
 */
@Path("gridftp")
public class GridftpWebResource extends AbstractWebResource {
    /**
     * Get the GridFTP application level information
     */
    @GET
    public Response queryApplicationLevelInfo(@QueryParam("ip") String ipAddr,
                                              @QueryParam("port") String port) {
        GridftpService service = get(GridftpService.class);
        GridftpAppInfo appInfo = service.getAppLevelInfo(ipAddr, port);
        return Response.ok(appInfo.toString(), MediaType.APPLICATION_JSON_TYPE).build();
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
     * Turns a JSON string into an GridftpAppInfo instance.
     */
    private GridftpAppInfo jsonToAppInfo(InputStream stream) {
        JsonNode node;
        try {
            node = mapper().readTree(stream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to parse Gridftp app info request", e);
        }

        String s = node.path("ipAddr").asText(null);
    }
}
