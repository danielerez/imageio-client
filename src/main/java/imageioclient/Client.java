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
        ImageioClient imageioClient = new ImageioClient();
        String ticketId;

        switch (args[0]) {
            case "GET":
                ticketId = args[1];
                Guid ticketGuid = new Guid(ticketId);
                String ticketStr = imageioClient.getTicket(ticketGuid);
                System.out.println(ticketStr);
                break;
            case "PUT":
                JsonObject ticketJson = new Gson().fromJson(args[1], JsonObject.class);
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
                ticketId = args[1];
                imageioClient.deleteTicket(ticketId);
                break;
            default:
                usage();
        }
    }

    private static void usage() {
        System.out.println(
                "Usage:\n" +
                        "$ client PUT <ticket_json>\n" +
                        "$ client GET <ticket_uuid>\n" +
                        "$ client DELETE <ticket_uuid>");
    }
}
