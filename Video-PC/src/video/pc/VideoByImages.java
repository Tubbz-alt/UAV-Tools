/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video.pc;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Jesimar Arantes
 */
public class VideoPC {

    public static void main(String[] args) throws AWTException, IOException, InterruptedException {
        String name = "video";
        String dir = "./";
        for (int i = 0; i < 1000; i++){
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", new File(dir + name + "_" + i + ".png"));
            Thread.sleep(1000);
        }
    }
    
}
