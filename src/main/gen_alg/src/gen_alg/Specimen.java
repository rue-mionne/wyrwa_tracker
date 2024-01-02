package gen_alg;

public abstract class Specimen {
    GeneScheme genetics=new GeneScheme();
    public abstract Integer evaluateGenomeValue();
    public abstract Specimen generateNewSpecimen();
    void initiateGenetics(Allele template, Integer length) throws Exception {
        genetics.initializeAlleleFactory(template);
        genetics.generateAlleles(length);
    }
}
