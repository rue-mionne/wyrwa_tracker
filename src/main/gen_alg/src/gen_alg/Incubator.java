package gen_alg;

import java.util.ArrayList;
import java.util.concurrent.Flow;

public interface Incubator extends Runnable {
    //Nest setters/getters
    void setPopulation(Integer population);
    //Calendar setters/getters
    void addGenerationPeeker(Flow.Subscriber<GenerationCompleteEvent> generationPeeker);
    Integer getGenerationCount();
    void setMaxGenerationCount(Integer maxGenerationCount);//Integer.MAX_VALUE dla nawiększej możliwej liczby pokoleń
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
    ArrayList<? extends Specimen> getSpecimen();

    //Start func
    void run();
}
