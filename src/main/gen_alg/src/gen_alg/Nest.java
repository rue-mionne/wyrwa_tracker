package gen_alg;

import java.util.ArrayList;

public class Nest {
    Integer population;
    SpecimenFactory specimenGenerator;

    Nest(Specimen template){
        specimenGenerator=new SpecimenFactory();
        specimenGenerator.initiateFactory(template);
    }
    ArrayList<Specimen> generateStartPopulation() throws Exception {
        if(specimenGenerator.template!=null){
            return specimenGenerator.generateSpecimenNest(population);
        }
        else{
            throw new Exception("gen_alg.Nest: gen_alg.Specimen factory not initiated!");
        }
    }

    void setPopulationSize(Integer populationSize){
        population=populationSize;
    }
}
