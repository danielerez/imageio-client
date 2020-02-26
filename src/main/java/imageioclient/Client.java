package imageioclient;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import imageioclient.entities.Guid;
import imageioclient.entities.ImageTicket;

public class Client {
    public static void main(String[] args) {
        try {
            execute(args);
        } catch (Exception e) {
            usage();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void execute(String[] args) {
        String ticketId;
        ImageioClient imageioClient = new ImageioClient(args[0]);

        switch (args[1]) {
            case "GET":
                ticketId = args[2];
                Guid ticketGuid = new Guid(ticketId);
                String ticketStr = imageioClient.getTicket(ticketGuid);
                System.out.println(ticketStr);
                break;
            case "PUT":
                JsonObject ticketJson = new Gson().fromJson(args[2], JsonObject.class);
                ImageTicket ticket = new ImageTicket(
                        Guid.createGuidFromString(ticketJson.get("uuid").getAsString()),
                        ticketJson.get("size").getAsLong(),
                        ticketJson.get("url").getAsString(),
                        ticketJson.get("timeout").getAsInt(),
                        ticketJson.get("ops").getAsJsonArray().toString()
                                .replace("},{", " ,").split(" "));
                imageioClient.putTicket(ticket);
                break;
            case "DELETE":
                ticketId = args[2];
                imageioClient.deleteTicket(ticketId);
                break;
            default:
                usage();
        }
    }

    private static void usage() {
        System.out.println(
            "Usage:\n" +
            "$ client sock PUT <ticket_json>\n" +
            "$ client sock GET <ticket_uuid>\n" +
            "$ client sock DELETE <ticket_uuid>");
    }
}
