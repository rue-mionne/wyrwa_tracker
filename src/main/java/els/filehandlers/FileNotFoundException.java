package els.filehandlers;

public class FileNotFoundException extends  Exception{
    public String getMessage(){
        return "Failed to find file";
    }
}
