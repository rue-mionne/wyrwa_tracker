package gen_alg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Splicer {
    int population;
    boolean foundPerfect=false;
    double mutationProbability=0.1;
    ArrayList<Specimen> subjects;
    GeneCrossTemplate geneticCrossReference;
    Splicer(ArrayList<Specimen> subjects) throws Exception {
        this.subjects=subjects;
        Collections.sort(this.subjects);
        population=subjects.size();
        generateDefaultCrossTemplate();
    }

    ArrayList<Specimen> progressToNextGeneration() throws Exception {
        Integer usedSpots=0;
        Double setSize;
        Integer numberOfSets = geneticCrossReference.percentage.size();
        ArrayList<Double> spinningWheel=new ArrayList<>();
        spinningWheel.add(geneticCrossReference.weights.get(0));
        ArrayList<Specimen> newGen = new ArrayList<>();
        //generate "spinning wheel" - array of cumulated weights used to choose random specimen
        for(int i=1;i<geneticCrossReference.weights.size();i++){
            double temp = spinningWheel.get(i-1);
            double next = geneticCrossReference.weights.get(i);
            spinningWheel.add(temp+next);
        }
        //calculate sectors/sets of different cross patterns
        for(int i=0;i<numberOfSets-1;i++){
            setSize=geneticCrossReference.percentage.get(i)*population;
            setSize = Math.floor(setSize);
            usedSpots+= setSize.intValue();
            generateCrossSetSpecimens(setSize, i, spinningWheel, newGen);
        }
        Integer setNumber = numberOfSets-1;
        Integer lastSetSize = population-usedSpots;
        generateCrossSetSpecimens(lastSetSize.doubleValue(),setNumber,spinningWheel,newGen);
        Collections.sort(newGen);
        subjects=newGen;
        return newGen;
    }

    private void generateCrossSetSpecimens(Double setSize, int setNumber, ArrayList<Double> spinningWheel, ArrayList<Specimen> newGen) throws Exception {
        for(int j = 0; j< setSize; j++){
            LinkedList<Integer> crossModel = geneticCrossReference.crossModel.get(setNumber);
            Specimen parent1 = getRandomSpecimen(spinningWheel);
            Specimen parent2 = getRandomSpecimen(spinningWheel);
            Specimen newSpecimen = parent1.generateNewSpecimen();
            createNewSpecimenGenome(crossModel, newSpecimen, parent1, parent2);
            localMutationProbability=mutationProbability;
            checkForMutations(newSpecimen);
            newGen.add(newSpecimen);
            newSpecimen.score = newSpecimen.evaluateGenomeValue();
            foundPerfect= newSpecimen.heuCheck();
        }
    }
    double localMutationProbability;
    private void checkForMutations(Specimen newSpecimen) throws Exception {
        if(Math.random()<localMutationProbability) {
            Allele template = newSpecimen.genetics.template;
            int mutatedAllele = (int) (Math.random() * newSpecimen.genetics.alleles.size());
            newSpecimen.genetics.alleles.set(mutatedAllele, template.generateNewAllele());
            localMutationProbability=localMutationProbability;
            checkForMutations(newSpecimen);
        }
    }

    private static void createNewSpecimenGenome(LinkedList<Integer> crossModel, Specimen newSpecimen, Specimen parent1, Specimen parent2) throws Exception {
        for(int k = 0; k< crossModel.size(); k++){
            if(crossModel.get(k)==1){
                newSpecimen.genetics.alleles.set(k, parent1.genetics.alleles.get(k));
            }
            else if(crossModel.get(k)==2){
                newSpecimen.genetics.alleles.set(k, parent2.genetics.alleles.get(k));
            }
            else{
                throw new Exception("gen_alg.Splicer: Wrong model configuration");
            }
        }
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

    boolean isFoundPerfect(){return foundPerfect;}

    //HELPERS
    void generateDefaultCrossTemplate() throws Exception {
        LinkedList<Double> defaultPerc = new LinkedList<>();
        defaultPerc.add(0.1);
        defaultPerc.add(0.9);
        LinkedList<LinkedList<Integer>> defaultCrossModel = new LinkedList<>();
        LinkedList<Integer> strongestSurvive = new LinkedList<>();
        LinkedList<Integer> defaultGeneCross = new LinkedList<>();
        for(int i = 0;i<subjects.get(0).genetics.alleles.size();i++){
            if(i%2==0)
                defaultGeneCross.add(1);
            else
                defaultGeneCross.add(2);
            strongestSurvive.add(1);
        }
        defaultCrossModel.add(strongestSurvive);
        defaultCrossModel.add(defaultGeneCross);
        Double array[] =new Double[population];
        Arrays.fill(array,1.0);
        ArrayList<Double> defaultWeights = new ArrayList<>(Arrays.asList(array));
        defaultWeights.set(0,8.0);
        defaultWeights.set(1,3.0);
        int half = defaultWeights.size()/2;
        for(int i=half;i<defaultWeights.size();i++){
            defaultWeights.set(i,0.0);
        }
        geneticCrossReference = new GeneCrossTemplate(defaultPerc,defaultCrossModel,defaultWeights,false);
    }
}
