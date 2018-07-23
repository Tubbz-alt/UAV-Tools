package uav.plot.mission.structs;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe que modela a rota do drone em coordenadas cartesianas.
 * @author Jesimar S. Arantes
 */
public class Route3D {
    
    private final List<Position3D> route = new LinkedList<>();
    
    /**
     * Class constructor.
     */
    public Route3D(){
        
    }
    
    public void addPosition3D(Position3D wpt){
        route.add(wpt);
    }
    
    public List<Position3D> getRoute(){
        return route;
    }
    
    public Position3D getPosition3D(int i){
        return route.get(i);
    }
    
    public void removePosition3D(){
        route.remove(route.size()-1);
    }
    
    public void removePosition3D(int i){
        route.remove(i);
    }
    
    public int size(){
        return route.size();
    }
    
    public void printRoute(){
        System.out.println("Route3D");
        for (Position3D wpt: route){
            System.out.println(wpt.toString());
        }
    }
}
