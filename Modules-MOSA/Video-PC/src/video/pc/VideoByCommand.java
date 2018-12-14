package video.pc;

import com.wet.wired.jsr.converter.RecordingConverter;
import com.wet.wired.jsr.recorder.DesktopScreenRecorder;
import com.wet.wired.jsr.recorder.ScreenRecorder;
import com.wet.wired.jsr.recorder.ScreenRecorderListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

/**
 *
 * @author Jesimar Arantes
 */
public class VideoByCommand {
    
    private static final String START = "start";
    private static final String STOP = "stop";

    public static void main(String[] args) throws IOException {
        VideoByCommand main = new VideoByCommand();
    }

    public VideoByCommand() throws IOException {
        try {
            //Temp file for the Recorder
            String dir = "./videos/";
            File file = new File(dir + "video.cap");
            OutputStream out = new FileOutputStream(file);
            ScreenRecorderListener listener = new ScreenRecorderListener() {
                @Override
                public void frameRecorded(boolean bln) throws IOException {
                    
                }
                @Override
                public void recordingStopped() {
                    
                }
            };
            ScreenRecorder screenRecorder = new DesktopScreenRecorder(out, listener);
            Scanner sc = new Scanner(System.in);
            boolean record = false;
            while (sc.hasNext()){
                String read = sc.next();
                if (read.equals(START)){
                    System.out.println("start-video");
                    screenRecorder.startRecording();
                    record = true;
                }
                if (read.equals(STOP)){
                    if (record){
                        System.out.println("stop-video");
                        screenRecorder.stopRecording();
                        //We reformat the video to .mov file
                        RecordingConverter.main(new String[]{file.getAbsolutePath()});
                    }
                    out.close();
                    System.exit(0);
                }
            }           
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
