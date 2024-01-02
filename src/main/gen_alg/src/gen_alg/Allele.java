package gen_alg;

public abstract class Allele {
    public abstract Integer evaluateAlleleValue();
    protected abstract Allele generateNewAllele();
}
