package gen_alg;

import java.util.ArrayList;

public class GeneScheme {
    Allele template;
    AlleleFactory alleleGenerator= new AlleleFactory();
    ArrayList<Allele> alleles=new ArrayList<>();
    GeneScheme(Allele template){
        initializeAlleleFactory(template);
    }
    void initializeAlleleFactory(Allele template){
        this.template=template;
        alleleGenerator.initiateAlleleFactory(template);
    }

    void generateAlleles(Integer length) throws Exception {
        for(int i=0;i<length;i++){
            alleles.add(alleleGenerator.generateAllele());
        }
    }
    public boolean isInitialised(){
        return !alleles.isEmpty();
    }

    public Integer getLength(){
        return alleles.size();
    }

    public ArrayList<Allele> getAlleles(){
        return alleles;
    }
}
