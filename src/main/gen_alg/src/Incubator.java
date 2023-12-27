public interface Incubator {
    Integer getGenerationCount();
    void setMaxGenerationCount(Integer maxGenerationCount);
    void setPopulation(Integer population);
    void setMutationProbability(Double mutationProbability);
    void setSpecimenFactory(SpecimenFactory specimenFactory);
    SpecimenFactory getSpecimenFactory();
    void setGeneticCrossReference(GeneCrossTemplate geneticCrossReference);
}
