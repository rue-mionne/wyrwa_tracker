package gen_alg;

import java.util.ArrayList;
//import java.util.Iterator;
import java.util.LinkedList;

//
//crossModel and percentage are checked on construction; even though the last percentage spot is not used, it is necessary for coherency check
//weights aren't checked, and it is on user's part to check if length is the same as population count
//
public class GeneCrossTemplate {
    boolean autoEnabled;
    LinkedList<Double> percentage; //percent of new specimen using crossModel[index]
    LinkedList<LinkedList<Integer>> crossModel;//scheme of passing down the alleles 1=parent1, 2=parent2 only 1 or 2 possible
    ArrayList<Double> weights;//weight of a specimen after sorting specimen list
    //GCTIterator iterator;
    GeneCrossTemplate(LinkedList<Double> percentage, LinkedList<LinkedList<Integer>> crossModel, ArrayList<Double> weights , boolean autoEnabled) throws Exception {
        Double suma=0.0;
        for(Double perc : percentage){
            suma+=perc;
        }
        if(suma>1.0){
            throw new Exception("GENE_CROSS_TEMPLATE: Incorrect percentage values: total probability above 100%");
        }
        if(suma<1.0&&autoEnabled){
            if(percentage.size()== crossModel.size()){
                percentage.set(percentage.lastIndexOf(percentage.getLast()),1.0-suma);
            }
            if(percentage.size()== crossModel.size()-1){
                percentage.addLast(1.0-suma);
            }
        }
        else{
            if(percentage.size()!=crossModel.size()){
                throw new Exception("GENE_CROSS_TEMPLATE: List size mismatch and/or auto fill not enabled");
            }
        }
        this.autoEnabled = autoEnabled;
        this.percentage = percentage;
        this.crossModel = crossModel;
        this.weights=weights;
        //iterator = new GCTIterator(percentage.iterator(), crossModel.iterator());
    }
}
/*
class GCTIterator{
    public Iterator<Double> itPerc;
    public Iterator<gen_alg.GeneScheme> itMod;

    GCTIterator(Iterator<Double> itPerc, Iterator<gen_alg.GeneScheme> itMod){this.itPerc=itPerc;this.itMod=itMod;}

    public Pair<Double, gen_alg.GeneScheme> next(){
        gen_alg.GeneScheme mod = itMod.next();
        Double perc = itPerc.next();
        return new Pair<>(perc,mod);
    }

    public boolean hasNext(){
        return (itMod.hasNext()&& itPerc.hasNext());
    }
}*/
