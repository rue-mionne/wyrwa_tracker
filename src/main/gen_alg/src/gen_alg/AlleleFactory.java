package gen_alg;

public class AlleleFactory {
    Allele template;
    void initiateAlleleFactory(Allele template){
        this.template = template;
    }

    Allele generateAllele() throws Exception {
        if(template!=null)
            return template.generateNewAllele();
        else
            throw new Exception("gen_alg.Allele Factory not initiated!");
    }
}
