package gen_alg;

import java.util.ArrayList;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class Calendar {
    IncubatorBase incubator;
    Integer generationCount=0;
    Integer maxGenerationCount=100;
    Integer delayInMs=100;
    boolean go=true;
    Heuristic condition;
    boolean endOnCondition;
    ArrayList<Flow.Subscriber<? super GenerationCompleteEvent>> subscribers= new ArrayList<>();
    SubmissionPublisher<GenerationCompleteEvent> notifier;

    public void UpdateCondition(Heuristic condition){
        this.condition=condition;

    }

    Calendar(boolean endOnCondition, IncubatorBase incubator){this.endOnCondition=endOnCondition;this.incubator=incubator;}
    Calendar(boolean endOnCondition, Heuristic condition, IncubatorBase incubator){this.endOnCondition=endOnCondition;this.condition=condition;this.incubator=incubator;}
    public void addSubscriber(Flow.Subscriber<GenerationCompleteEvent> generationPeeker){notifier.subscribe(generationPeeker);}
    public void start() throws Exception {
        while(generationCount<maxGenerationCount&&(endOnCondition&&(condition.check()))){
            if(go) {
                incubator.specimenArrayList=incubator.splicer.progressToNextGeneration();
                notifier.submit(new GenerationCompleteEvent());
                generationCount++;
                try {
                    wait(delayInMs);
                } catch (InterruptedException ignored) {

                }
            }
        }
    }
}
