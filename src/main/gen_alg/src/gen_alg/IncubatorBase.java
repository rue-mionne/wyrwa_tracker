package gen_alg;

import java.util.ArrayList;
import java.util.concurrent.Flow;

public abstract class IncubatorBase implements Incubator{
    //Modules
    Specimen template;
    Splicer splicer;
    ArrayList<Specimen> specimenArrayList = new ArrayList<>();
    Calendar generationFlow=new Calendar(this);
    Nest nest;

    //MAIN INIT
    public IncubatorBase(Specimen template){
        this.template=template;
        nest=new Nest(template);
    }

    @Override
    public void Start() throws Exception {
        specimenArrayList=nest.generateStartPopulation();
        splicer = new Splicer(specimenArrayList);
        generationFlow.start();
    }

    //OPTIONAL SETTERS/GETTERS
    //Nest setters/getters
    @Override
    public void setPopulation(Integer population){nest.setPopulationSize(population);}
    //Calendar setters/getters
    @Override
    public void addGenerationPeeker(Flow.Subscriber<GenerationCompleteEvent> generationPeeker){
        generationFlow.addSubscriber(generationPeeker);
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
    @Override
    public void setDelayInMs(Integer delayInMs){
        generationFlow.delayInMs=delayInMs;
    }
    @Override
    public Integer getDelayInMs(){
        return generationFlow.delayInMs;
    }

    @Override
    public void pause() {
        generationFlow.go=false;
    }

    @Override
    public void restart() {
        generationFlow.go=true;
    }

    @Override
    public boolean isPaused() {
        return generationFlow.go;
    }

    //Splicer setters/getters
    @Override
    public void setMutationProbability(Double mutationProbability) {
        splicer.mutationProbability=mutationProbability;
    }

    @Override
    public Double getMutationProbability() {
        return splicer.mutationProbability;
    }

    @Override
    public void setGeneticCrossReference(GeneCrossTemplate geneticCrossReference) {
        splicer.geneticCrossReference=geneticCrossReference;
    }

    @Override
    public GeneCrossTemplate getGeneticCrossReference() {
        return splicer.geneticCrossReference;
    }

    @Override
    public ArrayList<Specimen> getSpecimen() {
        return (ArrayList<Specimen>) specimenArrayList.clone();
    }
}
