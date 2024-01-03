package gen_alg;

import java.util.ArrayList;

public abstract class Specimen implements Comparable<Specimen>{
    GeneScheme genetics;
    public Double score;
    public abstract Double evaluateGenomeValue();
    public abstract boolean heuCheck();
    public abstract Specimen generateNewSpecimen() throws Exception;
    public Specimen(Allele template){genetics=new GeneScheme(template);}
    public void initiateGenetics(Integer length) throws Exception {
        genetics.generateAlleles(length);
    }
    protected GeneScheme getGenetics(){return genetics;}
    protected ArrayList<Allele> getAlleles(){return genetics.getAlleles();}
}
