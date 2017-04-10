package canvas.pixel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pixel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private Integer x;
    private Integer y;
    private String color;

    public Pixel() {
    }

    public Pixel(Integer x, Integer y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String toString() {
        return x + ", " + y + ": " + color;
    }

    public Integer getId() {
        return id;
    }

    public Integer getX(){
        return x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y)
    {
        this.y = y;
    }

    public void setX(Integer x)
    {
        this.x = x;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
