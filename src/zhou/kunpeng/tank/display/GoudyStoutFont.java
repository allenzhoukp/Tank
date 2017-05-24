package zhou.kunpeng.tank.display;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by JA on 2017/5/21.
 * <p>
 * The font used in Tank Game is special (Goudy Stout).
 * getInstance() of this class returns the font.
 * </p>
 */
public class GoudyStoutFont {
    private static Font ourInstance = null;

    public static Font getInstance() {
        if (ourInstance == null) {
            synchronized (GoudyStoutFont.class) {
                if (ourInstance == null) {
                    loadFont();
                }
            }
        }
        return ourInstance;
    }

    private static void loadFont() {
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            is = GoudyStoutFont.class.getResourceAsStream("/fonts/Goudy Stout.ttf");
            bis = new BufferedInputStream(is);
            ourInstance = Font.createFont(Font.TRUETYPE_FONT, bis);
            ourInstance = ourInstance.deriveFont(14.0F);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private GoudyStoutFont() {
    }
}
