package els.surgeons;

public interface IDeleteSurgeon extends ISurgeon {
    void scheduleDelete(Object deleted);
    void reverseChanges();
}
