package canvas.pixel;

import canvas.db.RethinkDBConnectionFactory;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PixelChangesListener {
    private final Logger log = LoggerFactory.getLogger(PixelChangesListener.class);
    private static final RethinkDB r = RethinkDB.r;

    private final RethinkDBConnectionFactory connectionFactory;

    private final SimpMessagingTemplate webSocket;

    @Autowired
    public PixelChangesListener(RethinkDBConnectionFactory connectionFactory, SimpMessagingTemplate webSocket) {
        this.connectionFactory = connectionFactory;
        this.webSocket = webSocket;
    }

    @Async
    public void pushChangesToWebSocket() {
        System.out.println("push changes to web socket");
        Cursor<Pixel> cursor = r.db("canvas").table("pixels").changes()
                .getField("new_val")
                .run(connectionFactory.createConnection(), Pixel.class);

        while (cursor.hasNext()) {
            Pixel pixel = cursor.next();
            log.info("New message: {}", pixel.toString());
            webSocket.convertAndSend("/topic/canvas", pixel);
        }
    }
}
