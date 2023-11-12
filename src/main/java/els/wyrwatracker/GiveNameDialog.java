package els.wyrwatracker;

import javafx.scene.control.TextInputDialog;

public class GiveNameDialog extends TextInputDialog {
    String promptText = "Podaj nazwę";
    public void setPromptTextTarget(String tekst){
        promptText+=" "+tekst;
    }

    GiveNameDialog(String name){
        setPromptTextTarget(name);
        super.setContentText(promptText);
        super.setHeaderText("");
    }
}
