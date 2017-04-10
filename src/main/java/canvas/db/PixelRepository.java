package canvas.db;

import canvas.pixel.Pixel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by zachjustice on 4/9/17.
 */
public interface PixelRepository extends CrudRepository<Pixel, Long> {
    List<Pixel> findByXAndY(Integer x, Integer y);
}
