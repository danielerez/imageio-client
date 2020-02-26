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
        ImageioClient imageioClient = new ImageioClient(args[0]);
        ImageTicket ticket;

        switch (args[1]) {
            case "GET":
                ticket = imageioClient.getTicket(new Guid(args[2]));
                System.out.println(ticket.toJson().toString());
                break;
            case "PUT":
                JsonObject ticketJson = new Gson().fromJson(args[2], JsonObject.class);
                ticket = new ImageTicket(
                        Guid.createGuidFromString(ticketJson.get("uuid").getAsString()),
                        ticketJson.get("size").getAsLong(),
                        ticketJson.get("url").getAsString(),
                        ticketJson.get("timeout").getAsInt(),
                        ticketJson.get("ops").getAsJsonArray().toString()
                                .replace("},{", " ,").split(" "));
                imageioClient.putTicket(ticket);
                break;
            case "DELETE":
                imageioClient.deleteTicket(new Guid(args[2]));
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
