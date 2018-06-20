package uav.mission_creator.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Jesimar S. Arantes
 */
public class ReaderFileConfigRoute {
    
    private final String dirRoute3D;
    private final String fileRoute3D;
    private final String fileGeoBaseIn;
    private final String fileRouteGeo;
    private final String separatorRoute3D;
    private final String separatorGeoBaseIn;
    private final String separatorRouteGeo;
    private final String isUsedKmlGoogleEarth;
    
    private final Properties prop = new Properties();
    private final InputStream input = new FileInputStream("./config-route.properties");

    public ReaderFileConfigRoute() throws FileNotFoundException, IOException {
        prop.load(input);
        
        dirRoute3D = prop.getProperty("prop.route3dtogeo.dir");
        fileRoute3D = prop.getProperty("prop.route3dtogeo.file_route");
        fileGeoBaseIn = prop.getProperty("prop.route3dtogeo.file_geobase");
        fileRouteGeo = prop.getProperty("prop.route3dtogeo.file_out_routegeo");
        separatorRoute3D = prop.getProperty("prop.route3dtogeo.separator_in_route3d");
        separatorGeoBaseIn = prop.getProperty("prop.route3dtogeo.separator_in_geobase");
        separatorRouteGeo = prop.getProperty("prop.route3dtogeo.separator_out_routegeo"); 
        isUsedKmlGoogleEarth = prop.getProperty("prop.route3dtogeo.is_used_kml_google_earth"); 
    }
    
    public String getDirRoute3D() {
        return dirRoute3D;
    }

    public String getFileRoute3D() {
        return fileRoute3D;
    }      
    
    public String getFileRouteGeo() {
        return fileRouteGeo;
    }

    public String getSeparatorRoute3D() {
        return separatorRoute3D;
    } 
    
    public String getFileGeoBaseIn() {
        return fileGeoBaseIn;
    }

    public String getSeparatorGeoBaseIn() {
        return separatorGeoBaseIn;
    }

    public String getSeparatorRouteGeo() {
        return separatorRouteGeo;
    }

    public boolean getIsUsedKmlGoogleEarth() {
        if (isUsedKmlGoogleEarth.equals("true")){
            return true;
        }else{            
            return false;
        }
    }

}
