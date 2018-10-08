package uav.map.converter;

import java.io.File;
import uav.map.converter.reader.ReaderMapFull;
import uav.map.converter.map.Map;
import uav.map.converter.reader.ReaderMapNFZ;

/**
 *
 * @author Jesimar S. Arantes
 */
public class UAVMapConverter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String dir = "maps/";
        String type = "2";
        boolean printPrettyFormat = true;
        String typeConversion = "JSON";
        if (args.length > 0){
            if (args[0].equals("--help")){
                System.out.println("Command: java -jar UAV-Map-Converter.jar DIR TYPE_MAP PRINT_PRETTY_FORMAT TYPE_CONVERSION");
                System.out.println("\tDIR -> dir where are the maps.");
                System.out.println("\t\tExample: mission/");
                System.out.println("\tTYPE_MAP -> 1 - NFZ Map       2 - Full Map");
                System.out.println("\t\tExample: 1");
                System.out.println("\tPRINT_PRETTY_FORMAT -> true or false");
                System.out.println("\t\tExample: true");
                System.out.println("\tTYPE_CONVERSION -> JSON or XML");
                System.out.println("\t\tExample: JSON");
                System.exit(0);
            }else{
                dir = args[0];
                type = args[1];
                printPrettyFormat = Boolean.parseBoolean(args[2]);
                typeConversion = args[3];
            }
        }
        for (File f : new File(dir).listFiles()){
            if (f.getName().contains(".sgl")){
                if (type.equals("1")){//1 -> NFZ
                    Map map = new Map();
                    String nameFile = f.getName();
                    String onlyName = nameFile.replace(".sgl", "");
                    ReaderMapNFZ reader = new ReaderMapNFZ(map, dir + nameFile);
                    reader.read();
                    if (printPrettyFormat){
                        if (typeConversion.equals("JSON")){
                            Converter.convertToJSONinPrettyFormat(map, dir + onlyName + ".json");
                        }else if (typeConversion.equals("XML")){
                            Converter.convertToXMLinPrettyFormat(map, dir + onlyName + ".xml");
                        }
                    }else{
                        if (typeConversion.equals("JSON")){
                            Converter.convertToJSON(map, dir + onlyName + "_simple.json");
                        }else if (typeConversion.equals("XML")){
                            Converter.convertToXML(map, dir + onlyName +"_simple.xml");
                        }
                    }            
                }else if (type.equals("2")){//2 -> Full Map
                    Map map = new Map();
                    String nameFile = f.getName();
                    String onlyName = nameFile.replace(".sgl", "");
                    ReaderMapFull reader = new ReaderMapFull(map, dir + nameFile);
                    reader.read();
                    if (printPrettyFormat){
                        if (typeConversion.equals("JSON")){
                            Converter.convertToJSONinPrettyFormat(map, dir + onlyName + ".json");
                        }else if (typeConversion.equals("XML")){
                            Converter.convertToXMLinPrettyFormat(map, dir + onlyName + ".xml");
                        }
                    }else{
                        if (typeConversion.equals("JSON")){ 
                            Converter.convertToJSON(map, dir + onlyName +"_simple.json");
                        }else if (typeConversion.equals("XML")){
                            Converter.convertToXML(map, dir + onlyName + "_simple.xml");
                        }
                    }
                }
            }
        }  
    }
    
}
