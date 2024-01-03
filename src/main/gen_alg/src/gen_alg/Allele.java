package gen_alg;

public abstract class Allele {
    public abstract Double evaluateAlleleValue();
    protected abstract Allele generateNewAllele() throws Exception;
}
