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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Jesimar Arantes
 */
public class VideoByTime {
    
    private static final int TIME = 10000; 

    public static void main(String[] args) throws IOException {
        VideoByTime main = new VideoByTime();
    }

    public VideoByTime() throws IOException {
        try {
            //Temp file for the Recorder
            String dateHour = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
            File file = new File(".", "video_" + dateHour + ".cap");
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
            screenRecorder.startRecording();
            try{
                Thread.sleep(TIME);
                screenRecorder.stopRecording();
                //We reformat the video to .mov file
                RecordingConverter.main(new String[]{file.getAbsolutePath()});
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }           
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
