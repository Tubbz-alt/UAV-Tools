package uav.mission_creator.struct;

/**
 * Classe com métodos úteis sobre string.
 * @author Jesimar S. Arantes
 */
public class UtilString {
    
    public static String defineSeparator(String separator){
        switch (separator) {
            case "space":
                return " ";
            case "tab":
                return "\t";
            case "semicolon":
                return ";";
            case "comma":
                return ",";
            case "barn":
                return "\n";
            default:
                return "-1";
        }
    }
    
}
