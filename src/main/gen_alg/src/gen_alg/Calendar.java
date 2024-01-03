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
    ArrayList<Flow.Subscriber<? super GenerationCompleteEvent>> subscribers= new ArrayList<>();
    SubmissionPublisher<GenerationCompleteEvent> notifier;
    Calendar(IncubatorBase base){incubator=base;}
    public void addSubscriber(Flow.Subscriber<GenerationCompleteEvent> generationPeeker){notifier.subscribe(generationPeeker);}
    public void start() throws Exception {
        while((generationCount<maxGenerationCount)&&(incubator.splicer.isFoundPerfect())){
            if(go) {
                incubator.specimenArrayList = incubator.splicer.progressToNextGeneration();
                notifier.submit(new GenerationCompleteEvent());
                generationCount++;
                try {
                    wait(delayInMs);
                } catch (InterruptedException ignored) {

                }
            }
        }
        GenerationCompleteEvent event = new GenerationCompleteEvent();
        event.lastGen=true;
        notifier.submit(event);
    }
}
