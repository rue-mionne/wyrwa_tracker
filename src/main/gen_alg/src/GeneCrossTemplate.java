import javafx.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;

public class GeneCrossTemplate {
    boolean autoEnabled;
    LinkedList<Double> percentage;
    LinkedList<GeneScheme> crossModel;
    GCTIterator iterator;
    GeneCrossTemplate(LinkedList<Double> percentage, LinkedList<GeneScheme> crossModel, boolean autoEnabled) throws Exception {
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
        iterator = new GCTIterator(percentage.iterator(), crossModel.iterator());
    }
}

class GCTIterator{
    public Iterator<Double> itPerc;
    public Iterator<GeneScheme> itMod;

    GCTIterator(Iterator<Double> itPerc, Iterator<GeneScheme> itMod){this.itPerc=itPerc;this.itMod=itMod;}

    public Pair<Double, GeneScheme> next(){
        GeneScheme mod = itMod.next();
        Double perc = itPerc.next();
        return new Pair<>(perc,mod);
    }

    public boolean hasNext(){
        return (itMod.hasNext()&& itPerc.hasNext());
    }
}
