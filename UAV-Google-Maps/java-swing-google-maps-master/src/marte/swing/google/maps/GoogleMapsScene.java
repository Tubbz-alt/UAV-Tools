package marte.swing.google.maps;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JFrame;
import marte.swing.google.maps.events.MapsEvent;
import marte.swing.google.maps.models.LatLng;
import netscape.javascript.JSObject;

/**
 *
 * @author Marcio
 */
public class GoogleMapsScene extends Application{
    private static GoogleMapsScene singleton;
    private static File maps_html;
    private WebView browser;
    private WebEngine engine;
    private Scene scene;
    private Stage stage;

    public static GoogleMapsScene getInstance(){
        if(singleton==null){
            throw new IllegalArgumentException("You must call launch(file, args) first");
        }
        return singleton;
    }
    public static GoogleMapsScene launch(File maps_html, String... args) throws InterruptedException{
        if(singleton!=null){
            throw new IllegalArgumentException("You can have only one instance of this class, call without args getInstance()");
        }
        GoogleMapsScene.maps_html = maps_html.exists() ? maps_html : new File("./html/maps.html");
        Executors.newSingleThreadExecutor().execute(()->{
            Application.launch(GoogleMapsScene.class, args);
        });
        int time_out_count = 0;
        while(singleton==null && time_out_count<100){
            Thread.sleep(100);
            time_out_count++;
        }
        if(singleton==null){
            throw new IllegalStateException("Application.launch doesn't work");
        }
        return singleton;
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        browser = new WebView();
        engine = browser.getEngine();
        engine.setJavaScriptEnabled(true);
        //System.out.println(this.maps_html);
        engine.load(maps_html.toURI().toString());
        scene = new Scene(browser);//,1200,720);
        singleton = this;
    }
    public void addLoadListener(MapsEvent<Boolean> handle){
        engine.setOnStatusChanged((event)->{
            try{
                if((Boolean)engine.executeScript("started;")){
                    handle.handle(Boolean.TRUE);
                }
            }catch(Throwable ex){
                handle.handle(Boolean.FALSE);
            }
        });
    }
    public void addClickListener(MapsEvent<LatLng> handle){
        browser.setOnMouseClicked((event) -> {
            if(event.getButton()==MouseButton.PRIMARY){
                Object obj = engine.executeScript("event_click.shift();");
                if(obj instanceof JSObject){
                    JSObject js = (JSObject) obj;
                    LatLng latlng = new LatLng(js.eval("this.latLng.lat()").toString(), js.eval("this.latLng.lng()").toString());
                    handle.handle(latlng);
                }
            }
        });
    }

    public void attach(JFXPanel fxPanel){
        fxPanel.setScene(scene);
    }
    
    private String undefined(String val){
        return val==null ? "undefined" : "'"+val+"'";
    }
    public void setFullScreen(JFrame frame, JFXPanel fxPanel){
        Platform.runLater(()->{
            stage.setFullScreenExitHint("Pressione Alt+F4 ou fechar para sair do modo de tela cheia");
            stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("??"));
            stage.setOnCloseRequest((event)->{
                fxPanel.setScene(scene);
                frame.setVisible(true);
            });
            frame.setVisible(false);
            stage.setFullScreen(true);
            stage.setScene(scene);
            stage.show();
        });
    }
    public void addMarker(double lat, double lng, String key, String label){
        Platform.runLater(()->{
            engine.executeScript("addMarker("+lat+","+lng+",'"+key+"',"+undefined(label)+");");
        });
    }
    public void delMarker(String key){
        Platform.runLater(()->{
            engine.executeScript("delMarker('"+key+"');");
        });
    }
    public void addPolygon(String strokeColor, double strokeOpacity, double strokeWeight,
            String fillColor, double fillOpacity, Point2D... points){
        Platform.runLater(()->{
            String coords = "var coords = [\n"+Arrays.stream(points).map(p->"{lat: "+p.getX()+", lng: "+p.getY()+"}").reduce("", (a, c)->a+",\n\t"+c).substring(2)+"];\n";
//            System.out.println(coords);
            engine.executeScript(
                coords+
                "var poly = new google.maps.Polygon({\n" +
                "   paths: coords,\n" +
                "   strokeColor: '"+strokeColor+"',\n" +
                "   strokeOpacity: "+strokeOpacity+",\n" +
                "   strokeWeight: "+strokeWeight+",\n" +
                "   fillColor: '"+fillColor+"',\n" +
                "   fillOpacity: "+fillOpacity+"\n" +
                "});\n" +
                "poly.setMap(map);"
            );
            
        });
    }
    
    public void addLine(String strokeColor, double strokeOpacity, double strokeWeight, 
            Point2D... points){
        Platform.runLater(()->{
            String lines = "var lines = [\n"+Arrays.stream(points).map(p->"{lat: "+p.getX()+", lng: "+p.getY()+"}").reduce("", (a, c)->a+",\n\t"+c).substring(2)+"];\n";
//            System.out.println(lines);
            engine.executeScript(
                lines+
                "var line = new google.maps.Polyline({\n" +
                "   path: lines,\n" +
                "   strokeColor: '"+strokeColor+"',\n" +
                "   strokeOpacity: "+strokeOpacity+",\n" +
                "   strokeWeight: "+strokeWeight+",\n" +
                "   map: map\n" +
                "});"
            );
            
        });
    }
    
    public void setCenter(double lat, double lng){
        Platform.runLater(()->{
            engine.executeScript("map.setCenter({lat: "+lat+", lng: "+lng+"})");
        });
    }
}
