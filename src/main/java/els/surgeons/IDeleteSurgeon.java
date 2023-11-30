package els.surgeons;

public interface IDeleteSurgeon extends ISurgeon {
    void scheduleDelete();
    void reverseChanges();
}
