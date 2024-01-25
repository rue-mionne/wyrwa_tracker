package els.genAlgImpl;

import gen_alg.*;

import java.util.ArrayList;

public class DungeonOrder extends Specimen{
    CharDungPair alleleTemplate;
    ArrayList<Double> orderWeight = new ArrayList<>();
    Integer genomeLength = 7;
    public DungeonOrder(Allele template) {
        super(template);
    }

    @Override
    public Double evaluateGenomeValue() {
        Double value=0.0;
        if(orderWeight.isEmpty()){
            for(int i = 0;i<genomeLength;i++){
                orderWeight.add(1.0);
            }
        }
        if(this.getGenetics().isInitialised())
            for(int i=0; i<this.getGenetics().getLength();i++) {
                value += this.getAlleles().get(i).evaluateAlleleValue()*orderWeight.get(i);
            }
        score=value;
        System.out.println(score);
        return score;
    }

    @Override
    public boolean heuCheck() {
        return false;
    }

    @Override
    public Specimen generateNewSpecimen() throws Exception {
        DungeonOrder newSpec = new DungeonOrder(alleleTemplate);
        newSpec.setAlleleTemplate(alleleTemplate);
        newSpec.initiateGenetics(genomeLength);
        Specimen newSpecimen = newSpec;
        System.out.println("New gen");
        return newSpecimen;
    }

    @Override
    public int compareTo(Specimen specimen) {
        return specimen.score.compareTo(score);
    }

    public ArrayList<Double> getOrderWeight() {
        return orderWeight;
    }

    public void setOrderWeight(ArrayList<Double> orderWeight) {
        this.orderWeight = orderWeight;
    }

    public Integer getGenomeLength() {
        return genomeLength;
    }

    public void setGenomeLength(Integer genomeLength) {
        this.genomeLength = genomeLength;
    }

    public CharDungPair getAlleleTemplate() {
        return alleleTemplate;
    }

    public void setAlleleTemplate(CharDungPair alleleTemplate) {
        this.alleleTemplate = alleleTemplate;
    }

    public ArrayList<? extends Allele> getOrder(){return getGenetics().getAlleles();}
}
