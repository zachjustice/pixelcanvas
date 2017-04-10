package canvas.pixel;

import canvas.db.PixelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pixels")
public class PixelController {
    @Autowired
    private PixelRepository pixelRepository;
    private final SimpMessagingTemplate webSocket;

    @Autowired
    public PixelController(SimpMessagingTemplate webSocket) {
        this.webSocket = webSocket;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void postPixel(@RequestBody Pixel newPixel) {
        List<Pixel> pixels = pixelRepository.findByXAndY(newPixel.getX(), newPixel.getY());

        if(pixels.isEmpty()) {
            pixelRepository.save(newPixel);
        }
        else
        {
            Pixel oldPixel = pixels.get(0);
            oldPixel.setColor(newPixel.getColor());
            pixelRepository.save(oldPixel);
        }

        webSocket.convertAndSend("/topic/canvas", newPixel);
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Iterable<Pixel> getPixels() {
        return pixelRepository.findAll();
    }
}
