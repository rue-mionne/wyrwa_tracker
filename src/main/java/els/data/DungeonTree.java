package els.data;

import java.util.ArrayList;

public class DungeonTree {
    ArrayList<Region> listaRegionow = new ArrayList<Region>();

    public boolean RegionExists(String RegionName){
        for(Region region:listaRegionow){
            if(region.getName().equals(RegionName))
                return true;
        }
        return false;

    }

    public Region getRegion(String name){
        for(Region region:listaRegionow){
            if(region.getName().equals(name))
                return region;
        }
        return null;
    }

    public void addRegion(Region region){
        listaRegionow.add(region);
    }

    public ArrayList<Region> getListaRegionow(){
        return listaRegionow;
    }
}
