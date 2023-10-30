package els.filehandlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerListHandler {
    String serverListName;
    FileWriter serverListWriter;
    Scanner serverListReader;
    ArrayList<String> serverList = new ArrayList<String>();

    public ServerListHandler(String serverListName) throws FileNotFoundException{
        try {
            this.serverListName = serverListName;
            File plik = new File(serverListName);
            serverListReader = new Scanner(plik);

            while(serverListReader.hasNextLine()){
                serverList.add(serverListReader.nextLine());
            }
            serverListReader.close();
        }
        catch (IOException e){
            System.err.println("Failed to open file: " + serverListName + "\n" + e.getMessage());
            throw new FileNotFoundException();
        }

    }

    public ArrayList<String> getServerList(){
        return serverList;
    }

    public void SaveServerList(){
            try {
                serverListWriter = new FileWriter(serverListName);
                serverList.forEach((serverName) -> {
                    try {
                        serverListWriter.write(serverName + "\n");
                    } catch (IOException e) {
                        System.err.println("Jaki idiota usunął plik w biegu?");
                    }
                });
                serverListWriter.close();
            }
            catch(IOException e){
                System.err.println("Jaki idiota usunął plik w biegu?");
            }



    }
}
