package canvas.pixel;

import canvas.db.RethinkDBConnectionFactory;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pixels")
public class PixelController {
    private final RethinkDBConnectionFactory connectionFactory;
    private static final RethinkDB r = RethinkDB.r;
    private final Logger log = LoggerFactory.getLogger(PixelController.class);

    @Autowired
    public PixelController(RethinkDBConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void postPixel(@RequestBody Pixel pixel) {
        System.out.println("Post message " + pixel.toString());

        Long x = pixel.getX();
        Long y = pixel.getY();
        String color = pixel.getColor();

        // get canvas at x and y position
        Cursor<Pixel> pixels = r.db("canvas")
                .table("pixels")
                .filter(
                row -> row.g("x").eq(x).and(
                        row.g("y").eq(y)
                )
        ).run(connectionFactory.createConnection());

        if(pixels.toList().isEmpty())
        {
            System.out.println("  New canvas " + pixel);
            // if no pixels are at x and y, insert
            Object run = r.db("canvas")
                    .table("pixels")
                    .insert(pixel)
                    .run(connectionFactory.createConnection());
            log.info("Insert {}", run);
        }
        else
        {
            System.out.println("  Update canvas " + pixel);

            // if a canvas is at x and y, update the color
            r.db("canvas")
                    .table("pixels")
                    .filter(
                        row -> row.g("x").eq(x).and(
                                row.g("y").eq(y)
                        )
                    ).update(
                            r.hashMap("color", color)
                    )
                    .run(connectionFactory.createConnection());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Pixel> getPixels() {
        System.out.println("get pixels");

        Cursor<Pixel> cur = r.db("canvas")
                .table("pixels")
                .run(connectionFactory.createConnection(), Pixel.class);

        return cur.toList();
    }
}
