package els.genAlgImpl;

import gen_alg.*;

import java.util.ArrayList;

public class DungeonOrder extends Specimen{
    CharDungPair alleleTemplate=new CharDungPair(0,0);
    ArrayList<Double> orderWeight = new ArrayList<>();
    Integer genomeLength = 7;
    public DungeonOrder(Allele template) {
        super(template);
    }

    @Override
    public Double evaluateGenomeValue() {
        Double value=0.0;
        if(this.getGenetics().isInitialised()&&!orderWeight.isEmpty())
            for(int i=0; i<this.getGenetics().getLength();i++) {
                value += this.getAlleles().get(i).evaluateAlleleValue()*orderWeight.get(i);
            }
        score=value;
        return value;
    }

    @Override
    public boolean heuCheck() {
        return false;
    }

    @Override
    public Specimen generateNewSpecimen() throws Exception {
        Specimen newSpecimen = new DungeonOrder(alleleTemplate);
        newSpecimen.initiateGenetics(genomeLength);
        return newSpecimen;
    }

    @Override
    public int compareTo(Specimen specimen) {
        if(this.score> specimen.score)
            return 1;
        else if(this.score<specimen.score)
            return -1;
        else
            return 0;
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
}
