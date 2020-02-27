package imageioclient;

import imageioclient.entities.Guid;
import imageioclient.entities.ImageTicket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImageioClientTest {

    public static String UNIX_SOCKET_PATH = "/path/to/file.sock";

    private ImageioClient imageioClient = new ImageioClient(UNIX_SOCKET_PATH);;
    private ImageTicket imageTicket = getTestTicket();

    @BeforeAll
    static void setUp() {
        // ToDo: start imageio service
    }

    @AfterAll
    static void tearDown() {
        // ToDo: stop imageio service
    }

    @BeforeEach
    void putTicket() {
        imageioClient.putTicket(imageTicket);
    }

    @Test
    void getTicket() {
        ImageTicket ticketFromServer = imageioClient.getTicket(imageTicket.getId());
        assertEquals(ticketFromServer.getId(), imageTicket.getId());
    }

    @Test
    void deleteTicket() {
        imageioClient.deleteTicket(imageTicket.getId());
        Throwable exception = assertThrows(RuntimeException.class, () ->
                imageioClient.getTicket(imageTicket.getId()));
        assertEquals(String.format("No such ticket '%s'", imageTicket.getId()), exception.getMessage());
    }

    private ImageTicket getTestTicket() {
        return new ImageTicket(
                Guid.createGuidFromString("799030aa-97c3-4354-871e-0adf0556fcbf"),
                1073741824L,
                "file:///path/to/image",
                3000,
                new String[]{"read", "write"});
    }
}