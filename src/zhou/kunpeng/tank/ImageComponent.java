package zhou.kunpeng.tank;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Created by JA on 2017/5/19.
 * A JComponent that contains a certain image.
 */
public class ImageComponent extends JComponent {
    private int width;

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    private int height;
    private String path = "";
    private BufferedImage image;

    /**
     * Create an image with path, startX, startY, width and height.
     *
     * @param path resource path of the image. If using absolute path, it should starts with '/'.
     * @param sx   the x position of the image in its parent container.
     * @param sy   the y position of the image in its parent container.
     * @param wid  the width of the image. If both wid and hei is zero, it will be loaded in its original size.
     *             Otherwise the image will be rescaled.
     * @param hei  the height of the image.
     */
    public ImageComponent(String path, int sx, int sy, int wid, int hei) {
        super();
        this.setOpaque(false);

        this.path = path;
        loadImage();

        if (wid == 0 && hei == 0) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        } else {
            this.width = wid;
            this.height = hei;
        }
        this.setBounds(sx, sy, this.width, this.height);
    }

    public ImageComponent(String path, int sx, int sy) {
        this(path, sx, sy, 0, 0);
    }

    public ImageComponent(String path) {
        this(path, 0, 0, 0, 0);
    }

    // SO F**KING Foolish! Why there isn't any native ROTATE api for a component?
    public void rotate(double radius) {
        AffineTransform tr = new AffineTransform();
        tr.setToRotation(radius, width / 2, height / 2);
        AffineTransformOp op = new AffineTransformOp(tr, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);
        repaint();
    }

    private void loadImage() {
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(image, 0, 0, this.width, this.height, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.width, this.height);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(this.width, this.height);
    }

}
