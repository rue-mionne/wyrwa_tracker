package els.data;

import java.util.ArrayList;

public class BuildSet {
    ArrayList<Build> BuildSetList = new ArrayList<Build>();

    public void AddNewBuildSet(Build build){
        BuildSetList.add(build);
    }

    public ArrayList<String> getSetNameList(){
        ArrayList<String> listaNazwBuildow = new ArrayList<String>();
        BuildSetList.forEach((build)->listaNazwBuildow.add(build.Name));
        return listaNazwBuildow;
    }

    public Build getBuild(String buildName){
        for(Build build : BuildSetList){
            if(build.Name==buildName){
                return build;
            }
        }
        return null;
    }

    public Build getBuild(int buildID){
        for(Build build : BuildSetList){
            if(build.ID==buildID){
                return build;
            }
        }
        return null;
    }

    public ArrayList<Build> getBuildSetList() {
        return BuildSetList;
    }
}
