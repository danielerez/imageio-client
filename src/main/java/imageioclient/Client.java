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
                imageioClient.putTicket(ImageTicket.fromJson(args[2]));
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
