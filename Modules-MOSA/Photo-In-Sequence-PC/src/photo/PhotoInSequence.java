package photo;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

/**
 *
 * @author Jesimar Arantes
 */
public class PhotoInSequence {

    public static void main(String[] args) throws AWTException, IOException, InterruptedException {
        String name = "screenshot";
        String dir = "./photos-in-sequence/"; 
        int number;
        int delay;
        if (args.length > 1){
            number = Integer.parseInt(args[0]);
            delay = Integer.parseInt(args[1]);
        }else{
            number = 10;
            delay = 5;
        }
        for (int i = 0; i < number; i++){
            String dateHour = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
            BufferedImage image = new Robot().createScreenCapture(
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", new File(dir + name + "_" + dateHour + ".png"));
            Thread.sleep(delay*1000);
        }
    }
}
