package gen_alg;

import java.util.ArrayList;
import java.util.concurrent.Flow;

public interface Incubator {
    //Nest setters/getters
    void setPopulation(Integer population);
    //Calendar setters/getters
    void addGenerationPeeker(Flow.Subscriber<GenerationCompleteEvent> generationPeeker);
    Integer getGenerationCount();
    void setMaxGenerationCount(Integer maxGenerationCount);
    Integer getMaxGenerationCount();
    void setDelayInMs(Integer delayInMs);
    Integer getDelayInMs();
    void pause();
    void restart();
    boolean isPaused();
    //Splicer setters/getters
    void setMutationProbability(Double mutationProbability);
    Double getMutationProbability();
    void setGeneticCrossReference(GeneCrossTemplate geneticCrossReference);
    GeneCrossTemplate getGeneticCrossReference();

    //monitoring
    ArrayList<Specimen> getSpecimen();

    //Start func
    void Start() throws Exception;
}
