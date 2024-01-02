package gen_alg;

import java.util.ArrayList;

public class GeneScheme {
    Allele template;
    AlleleFactory alleleGenerator;
    ArrayList<Allele> alleles=new ArrayList<>();
    void initializeAlleleFactory(Allele template){
        this.template=template;
        alleleGenerator.initiateAlleleFactory(template);
    }

    void generateAlleles(Integer length) throws Exception {
        for(int i=0;i<length;i++){
            alleleGenerator.generateAllele();
        }
    }
}
