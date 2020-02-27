package imageioclient.entities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ImageTicket implements BusinessEntity<Guid> {

    private static final long serialVersionUID = 6233250895873388748L;

    private ImageTicketInformation imageTicketInformation;

    private String[] ops;

    private boolean sparse;

    private String transferId;

    private String filename;

    public ImageTicket() {
        imageTicketInformation = new ImageTicketInformation();
    }

    public ImageTicket(Guid uuid, Long size, String url, int timeout, String[] ops) {
        imageTicketInformation = new ImageTicketInformation();
        imageTicketInformation.setSize(size);
        imageTicketInformation.setUrl(url);
        imageTicketInformation.setTimeout(timeout);
        setId(uuid);
        setOps(ops);
    }

    public ImageTicket(ImageTicketInformation imageTicketInformation) {
        this.imageTicketInformation = imageTicketInformation;
    }

    public ImageTicketInformation getImageTicketInformation() {
        return imageTicketInformation;
    }

    public void setImageTicketInformation(ImageTicketInformation imageTicketInformation) {
        this.imageTicketInformation = imageTicketInformation;
    }

    @Override
    public Guid getId() {
        return imageTicketInformation.getId();
    }

    @Override
    public void setId(Guid id) {
        imageTicketInformation.setId(id);
    }

    public String[] getOps() {
        return ops;
    }

    public void setOps(String[] ops) {
        this.ops = ops;
    }

    public boolean isSparse() {
        return sparse;
    }

    public void setSparse(boolean sparse) {
        this.sparse = sparse;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Map<String, Object> toDict() {
        Map<String, Object> ticketDict = new HashMap<>();
        ticketDict.put("uuid", imageTicketInformation.getId().toString());
        ticketDict.put("timeout", imageTicketInformation.getTimeout());
        ticketDict.put("size", imageTicketInformation.getSize());
        ticketDict.put("url", imageTicketInformation.getUrl());
        ticketDict.put("ops", ops);
        ticketDict.put("sparse", sparse);
        ticketDict.put("transfer_id", transferId);
        // filename is null by default, and only specified by the UI
        if (filename != null) {
            ticketDict.put("filename", filename);
        }
        return ticketDict;
    }

    public JsonObject toJson() {
        String json = new Gson().toJson(toDict());
        return new Gson().fromJson(json, JsonObject.class);
    }

    public static ImageTicket fromJson(String json) {
        JsonObject ticketJson = new Gson().fromJson(json, JsonObject.class);

        ImageTicketInformation ticketInformation = new ImageTicketInformation();
        if (ticketJson.has("uuid")) {
            ticketInformation.setId(Guid.createGuidFromString(ticketJson.get("uuid").getAsString()));
        }
        if (ticketJson.has("timeout")) {
            ticketInformation.setTimeout(ticketJson.get("timeout").getAsInt());
        }
        if (ticketJson.has("size")) {
            ticketInformation.setSize(ticketJson.get("size").getAsLong());
        }
        if (ticketJson.has("url")) {
            ticketInformation.setUrl(ticketJson.get("url").getAsString());
        }
        if (ticketJson.has("idle_time")) {
            ticketInformation.setIdleTime(ticketJson.get("idle_time").getAsInt());
        }

        ImageTicket ticket = new ImageTicket(ticketInformation);
        if (ticketJson.has("sparse")) {
            ticket.setSparse(ticketJson.get("sparse").getAsBoolean());
        }
        if (ticketJson.has("ops")) {
            ticket.setOps(jsonToArray(ticketJson.get("ops").getAsJsonArray()));
        }

        return ticket;
    }

    private static String[] jsonToArray(JsonArray jsonArray) {
        Gson converter = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> list =  converter.fromJson(jsonArray, type);
        return list.toArray(new String[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) return false;
        ImageTicket that = (ImageTicket) o;
        return sparse == that.sparse &&
                Objects.equals(imageTicketInformation, that.imageTicketInformation) &&
                Arrays.equals(ops, that.ops) &&
                Objects.equals(transferId, that.transferId) &&
                Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(imageTicketInformation, sparse, transferId, filename);
        result = 31 * result + Arrays.hashCode(ops);
        return result;
    }
}
