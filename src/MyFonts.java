import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyFonts {

    Font font_slkscr;
    Font font_title;

    public MyFonts(){
        Load();
    }

    public void Load(){
        try {
            font_slkscr = Font.createFont(Font.TRUETYPE_FONT, new File("resources/rainyhearts.ttf")).deriveFont(16F);
            font_title = Font.createFont(Font.TRUETYPE_FONT, new File("resources/title_font.ttf")).deriveFont(16F);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/rainyhearts.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/title_font.ttf")));
        } catch (IOException | FontFormatException e) {
            System.out.println("Problema na importação da fonte");
            e.printStackTrace();
        }

    }
    
}
