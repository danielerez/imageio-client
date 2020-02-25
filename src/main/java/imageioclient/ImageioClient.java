package imageioclient;

import imageioclient.entities.Guid;
import imageioclient.entities.ImageTicket;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.util.EntityUtils;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ImageioClient {

    public static String TICKETS_URI = "/tickets/";
    public static int CLIENT_BUFFER_SIZE = 8 * 1024;

    String socketPath;
    DefaultBHttpClientConnection conn;

    public ImageioClient(String socketPath) {
        this.socketPath = socketPath;
    }

    public String getTicket(Guid ticketUUID) {
        // Create request
        BasicHttpEntityEnclosingRequest request = getRequest("GET", TICKETS_URI + ticketUUID);

        // Send request and get response
        HttpEntity entity = sendRequest(request);

        try {
            // Get content
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            String ticket = reader.lines().collect(Collectors.joining());
            consumeEntity(entity);
            return ticket;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void putTicket(ImageTicket ticket) {
        // Create request
        BasicHttpEntityEnclosingRequest request = getRequest("PUT", TICKETS_URI + ticket.getId());

        // Create byte array from ticket
        StringEntity entity = new StringEntity(ticket.toJson().toString(), StandardCharsets.UTF_8);
        request.setEntity(entity);
        request.setHeader("content-length", String.valueOf(request.getEntity().getContentLength()));

        // Send request and get response
        HttpEntity requestEntity = sendRequest(request);
        consumeEntity(requestEntity);
    }

    public void deleteTicket(String ticketUUID) {
        // Create request
        BasicHttpEntityEnclosingRequest request = getRequest("DELETE", TICKETS_URI + ticketUUID);

        // Send request and get response
        HttpEntity entity = sendRequest(request);
        consumeEntity(entity);
    }

    private DefaultBHttpClientConnection getConnection() {
        if (conn != null) {
            return conn;
        }

        File socketFile = new File(socketPath);
        AFUNIXSocket socket;
        try {
            // Create unix socket
            socket = AFUNIXSocket.newInstance();
            socket.connect(new AFUNIXSocketAddress(socketFile));
            // Bind socket to HTTP client
            conn = new DefaultBHttpClientConnection(CLIENT_BUFFER_SIZE);
            conn.bind(socket);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return conn;
    }

    private HttpEntity sendRequest(BasicHttpEntityEnclosingRequest request) {
        try {
            DefaultBHttpClientConnection conn = getConnection();

            // Send request
            conn.sendRequestHeader(request);
            conn.sendRequestEntity(request);
            conn.flush();

            // Get response
            HttpResponse response = conn.receiveResponseHeader();
            conn.receiveResponseEntity(response);

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new RuntimeException("Empty response");
            }

            // Return the HTTP entity
            return entity;
        } catch (HttpException | IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private BasicHttpEntityEnclosingRequest getRequest(String method, String uri) {
        return new BasicHttpEntityEnclosingRequest(method, uri, HttpVersion.HTTP_1_1);
    }

    private void consumeEntity(HttpEntity entity) {
        // Ensure content is consume, so that the underlying connection can be re-used
        try {
            EntityUtils.consume(entity);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
