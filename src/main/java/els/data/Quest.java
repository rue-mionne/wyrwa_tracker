package els.data;

import java.util.EnumSet;

public class Quest {
    int ID;
    String QuestName;
    public enum CompletionType{REPEAT, WEEKLY, DAILY, ONETIME};
    CompletionType completionType;
    public enum CompletionRange{ACCOUNT, CHARACTER};
    CompletionRange completionRange;
    int MaxCompletionCount;
    int ED;
    int EXP;
    int EP;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQuestName() {
        return QuestName;
    }

    public void setQuestName(String questName) {
        QuestName = questName;
    }

    public CompletionType getCompletionType() {
        return completionType;
    }

    public void setCompletionType(CompletionType completionType) {
        this.completionType = completionType;
    }

    public CompletionRange getCompletionRange() {
        return completionRange;
    }

    public void setCompletionRange(CompletionRange completionRange) {
        this.completionRange = completionRange;
    }

    public int getMaxCompletionCount() {
        return MaxCompletionCount;
    }

    public void setMaxCompletionCount(int maxCompletionCount) {
        MaxCompletionCount = maxCompletionCount;
    }

    public int getED() {
        return ED;
    }

    public void setED(int ED) {
        this.ED = ED;
    }

    public int getEXP() {
        return EXP;
    }

    public void setEXP(int EXP) {
        this.EXP = EXP;
    }

    public int getEP() {
        return EP;
    }

    public void setEP(int EP) {
        this.EP = EP;
    }

    public Quest getPreviousQuest() {
        return previousQuest;
    }

    public void setPreviousQuest(Quest previousQuest) {
        this.previousQuest = previousQuest;
    }

    public Quest getNextQuest() {
        return nextQuest;
    }

    public void setNextQuest(Quest nextQuest) {
        this.nextQuest = nextQuest;
    }

    public RewardTable getListaPrzedmiotow() {
        return listaPrzedmiotow;
    }

    Quest previousQuest;
    Quest nextQuest;
    public enum CharacterType{LABY,NOAH,ELPACK,ALL};
    RewardTable listaPrzedmiotow=new RewardTable();

    public Quest(int ID, String questName, CompletionType completionType, CompletionRange completionRange, int ED, int EXP, int EP) {
        this.ID = ID;
        QuestName = questName;
        this.completionType = completionType;
        this.completionRange = completionRange;
        this.ED = ED;
        this.EXP = EXP;
        this.EP = EP;
    }
}
