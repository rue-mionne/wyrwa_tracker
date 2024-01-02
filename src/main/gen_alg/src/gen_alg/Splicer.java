package gen_alg;

import java.util.ArrayList;
import java.util.LinkedList;

public class Splicer {
    double mutationProbability=0.1;
    ArrayList<Specimen> subjects;
    GeneCrossTemplate geneticCrossReference=null;
    AlleleFactory alleleMutationGenerator;
    Splicer(ArrayList<Specimen> subjects){
        this.subjects=subjects;
    }
    ArrayList<Specimen> progressToNextGeneration() throws Exception {
        Integer population = subjects.size();
        Integer usedSpots=0;
        Double setSize;
        Integer numberOfSets = geneticCrossReference.percentage.size();
        ArrayList<Double> spinningWheel=new ArrayList<>();
        spinningWheel.add(geneticCrossReference.weights.get(0));
        ArrayList<Specimen> newGen = new ArrayList<>();
        for(int i=1;i<geneticCrossReference.weights.size();i++){
            double temp = spinningWheel.get(i-1);
            double next = geneticCrossReference.weights.get(i);
            spinningWheel.add(temp+next);
        }
        for(int i=0;i<numberOfSets-1;i++){
            setSize=geneticCrossReference.percentage.get(i)*population;
            setSize = Math.floor(setSize);
            usedSpots+= setSize.intValue();
            {
                for(int j=0;j<setSize;j++){
                    LinkedList<Integer> crossModel = geneticCrossReference.crossModel.get(i);
                    Specimen parent1 = getRandomSpecimen(spinningWheel);
                    Specimen parent2 = getRandomSpecimen(spinningWheel);
                    Specimen newSpecimen = parent1.generateNewSpecimen();
                    newSpecimen.genetics.initializeAlleleFactory(parent1.genetics.template);
                    for(int k=0; k<crossModel.size();k++){
                        if(crossModel.get(k)==1){
                            newSpecimen.genetics.alleles.set(k,parent1.genetics.alleles.get(k));
                        }
                        else if(crossModel.get(k)==2){
                            newSpecimen.genetics.alleles.set(k,parent2.genetics.alleles.get(k));
                        }
                        else{
                            throw new Exception("gen_alg.Splicer: Wrong model configuration");
                        }
                    }
                    if(Math.random()<mutationProbability) {
                        int mutatedAllele = (int) (Math.random() * crossModel.size());
                        alleleMutationGenerator.initiateAlleleFactory(parent1.genetics.template);
                        newSpecimen.genetics.alleles.set(mutatedAllele, alleleMutationGenerator.generateAllele());
                    }
                    newGen.add(newSpecimen);
                    usedSpots++;

                }
            }
        }
        return newGen;
    }

    Specimen getRandomSpecimen(ArrayList<Double> spinningWheel) throws Exception {
        double shot = Math.random()* spinningWheel.get(spinningWheel.size()-1);
        int index = 0;
        while(shot<spinningWheel.get(spinningWheel.size()-1)){
            if(shot< spinningWheel.get(index))
                return subjects.get(index);
            else
                index++;
        }
        throw new Exception("Spicer: roll out of bounds");
    }
}
