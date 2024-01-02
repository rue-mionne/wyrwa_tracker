package gen_alg;

import java.util.ArrayList;

public abstract class IncubatorBase implements Incubator{
    //Modules
    Specimen template;
    Splicer splicer;
    ArrayList<Specimen> specimenArrayList = new ArrayList<>();
    Calendar generationFlow=new Calendar(false,this);
    Nest nest;

    //MAIN INIT
    IncubatorBase(Specimen template){
        this.template=template;
    }

    //OPTIONAL SETTERS/GETTERS
    //Nest setters/getters
    void setCalendar(boolean onCondition){
        generationFlow = new Calendar(onCondition,this);
    }

    void setCalendar(boolean onCondition, Heuristic heuristic){
        generationFlow = new Calendar(onCondition,heuristic,this);
    }

    @Override
    public Integer getGenerationCount() {
        return generationFlow.generationCount;
    }

    @Override
    public Integer getMaxGenerationCount() {
        return generationFlow.maxGenerationCount;
    }

    @Override
    public void setMaxGenerationCount(Integer maxGenerationCount) {
        generationFlow.maxGenerationCount=maxGenerationCount;
    }

    /*
    @Override
    public void initializePopulation(Integer populationCount) throws Exception {
        nest=new Nest(template);
        nest.setPopulationSize(populationCount);
        specimenArrayList=nest.generateStartPopulation();
    }

    @Override
    public void initializeSplicer() {
        splicer = new Splicer(specimenArrayList);
        AlleleFactory alleleFactory=specimenArrayList.get(0).genetics.alleleGenerator;
        splicer.alleleMutationGenerator=alleleFactory;
    }*/
}
