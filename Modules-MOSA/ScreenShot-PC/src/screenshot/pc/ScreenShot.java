/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenshot.pc;

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
public class ScreenShot {

    public static void main(String[] args) throws AWTException, IOException, InterruptedException {
        String dateHour = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        String name = "screenshot";
        String dir = "./pictures/";
        BufferedImage image = new Robot().createScreenCapture(
                new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(image, "png", new File(dir + name + "_" + dateHour + ".png"));
    }
}
