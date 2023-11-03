package els.filehandlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class SQLFileHandler {
    LinkedList<String> listaKomend = new LinkedList<>();
    Scanner czytacz;

    public void Inicjuj(String zrodlo){
        File plik = new File(zrodlo);
        try{
        czytacz = new Scanner(plik);
        czytacz.useDelimiter(";");
        while(czytacz.hasNext()){
            listaKomend.add(czytacz.next() + ";");
        }
        czytacz.close();
        }
        catch(FileNotFoundException e){
            System.err.println("Teraz ten plik?");
        }
    }

    public LinkedList<String> getStatementList(){
        return listaKomend;
    }
}
