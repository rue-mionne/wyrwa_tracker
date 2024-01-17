package els.genAlgImpl;

import gen_alg.Allele;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import static els.genAlgImpl.PackageRangeSQLConnectionHolder.con;

public class CharDungPair extends Allele{
    ArrayList<Integer> dungeonIDRange=new ArrayList<>();
    ArrayList<Integer> buildIDRange=new ArrayList<>();
    Integer chosenDungeonID;
    Integer chosenBuildID;
    Connection con;
    @Override
    public Double evaluateAlleleValue() {
        Double alleleValue=0.0;
        try {
            alleleValue += estimateEDEPvalues()/1000;
            alleleValue+=estimateSellableValue()/1000;
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return alleleValue;
    }

    public CharDungPair(Integer DungeonID, Integer BuildID){
        chosenBuildID=BuildID;
        chosenDungeonID=DungeonID;
    }

    @Override
    protected Allele generateNewAllele() throws Exception {
        if(dungeonIDRange.isEmpty()||buildIDRange.isEmpty())
            throw new Exception("Allele generation: ID base not initiated! Remember to use initiateIDBases() to provide with database info");
        else {
            Random gen = new Random();
            int indexDungID;
            int indexBuildID;
            do{
                indexDungID = gen.nextInt(dungeonIDRange.size());
                indexBuildID = gen.nextInt(buildIDRange.size());
            }
            while(checkIfNoQuests(indexDungID,indexBuildID));
            CharDungPair newPair= new CharDungPair(dungeonIDRange.get(indexDungID),buildIDRange.get(indexBuildID));
            newPair.initiateIDBases(con);
            return newPair;
        }
    }

    public void initiateIDBases(Connection con) throws SQLException {
        this.con = con;
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select BuildSetID from BuildSetsTable");
        while(rs.next()){
            buildIDRange.add(rs.getInt("BuildSetID"));
        }
        rs = query.executeQuery("Select ID from Dungeons");
        while(rs.next()){
            dungeonIDRange.add(rs.getInt("ID"));
        }
    }

    Double estimateEDEPvalues() throws SQLException {
        Double estimatedValues=0.0;
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("SELECT QuestData.ED, QuestData.EP from QuestData\n" +
                "INNER join QuestDungeonData on QuestDungeonData.QuestID=QuestData.ID\n" +
                "INNER join QuestCompletion on QuestCompletion.QuestID=QuestDungeonData.QuestID\n" +
                "INNER join Characters on QuestCompletion.CharacterID=Characters.ID\n" +
                "INNER join BuildSetsTable on BuildSetsTable.CharacterID=Characters.ID\n" +
                "INNER join Dungeons on Dungeons.ID=QuestDungeonData.DungeonID\n" +
                "WHERE DungeonID=" +chosenDungeonID+" and (BuildSetID= "+chosenBuildID+" or QuestCompletion.CharacterID=1)and EstCP>OptimalCP and Status like 'Active'");
        while(rs.next()){
            estimatedValues+=1*rs.getInt("ED")+0.9*rs.getInt("EP");
        }
        return estimatedValues;
    }

    Double estimateSellableValue() throws SQLException {
        Double estimatedValue = 0.0;
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("SELECT ItemData.SalePrice, RewardData.Amount from QuestData\n" +
                "INNER join QuestDungeonData on QuestDungeonData.QuestID=QuestData.ID\n" +
                "INNER join QuestCompletion on QuestCompletion.QuestID=QuestDungeonData.QuestID\n" +
                "INNER join Characters on QuestCompletion.CharacterID=Characters.ID\n" +
                "INNER join BuildSetsTable on BuildSetsTable.CharacterID=Characters.ID\n" +
                "INNER join Dungeons on Dungeons.ID=QuestDungeonData.DungeonID\n" +
                "INNER join RewardData on RewardData.QuestID=QuestData.ID\n" +
                "INNER join ItemData on ItemData.ItemID=RewardData.RewardID\n" +
                "WHERE DungeonID="+chosenDungeonID+" and (BuildSetID="+chosenBuildID+" or QuestCompletion.CharacterID=1 ) and EstCP>OptimalCP and Sellable=1 and Status like 'Active'");
        while(rs.next()){
            estimatedValue+=rs.getInt("SalePrice")*rs.getInt("Amount");
        }
        return estimatedValue;
    }

    boolean checkIfNoQuests(int chosenDungeonIndex, int chosenBuildIndex) throws SQLException {
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("SELECT QuestData.ID from QuestData\n" +
                "INNER join QuestDungeonData on QuestDungeonData.QuestID=QuestData.ID\n" +
                "INNER join QuestCompletion on QuestCompletion.QuestID=QuestDungeonData.QuestID\n" +
                "INNER join Characters on QuestCompletion.CharacterID=Characters.ID\n" +
                "INNER join BuildSetsTable on BuildSetsTable.CharacterID=Characters.ID\n" +
                "INNER join Dungeons on Dungeons.ID=QuestDungeonData.DungeonID\n" +
                "WHERE DungeonID="+dungeonIDRange.get(chosenDungeonIndex)+" and BuildSetID="+buildIDRange.get(chosenBuildIndex)+" and EstCP>OptimalCP and Status like 'Active'");
        return !rs.next();
    }
}
