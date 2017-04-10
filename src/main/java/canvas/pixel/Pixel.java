package canvas.pixel;

public class Pixel {

    private Long x;
    private Long y;
    private String color;

    public Pixel() {
    }

    public Pixel(Long x, Long y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
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
}
