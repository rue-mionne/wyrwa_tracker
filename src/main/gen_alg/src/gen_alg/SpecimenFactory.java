package gen_alg;

import java.util.ArrayList;

public class SpecimenFactory {
    Specimen template;
    void initiateFactory(Specimen template){
        this.template=template;
    }

    ArrayList<Specimen> generateSpecimenNest(Integer populationSize) throws Exception {
        ArrayList<Specimen> specimenNest=new ArrayList<>();
        for(int i=0;i<populationSize;i++){
            Specimen newSpec = template.generateNewSpecimen();
            specimenNest.add(newSpec);
            newSpec.score= newSpec.evaluateGenomeValue();
        }
        return specimenNest;
    }
}
