package TextBoard.Model;

import TextBoard.INI.*;
import TextBoard.Controller.*;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class TextBoardDoc {
    private static final TextBoardDoc instance = new TextBoardDoc();

    SQLDataBase db;

    public static TextBoardDoc getInstance() {
        return instance;
    }

    TextBoardCtrl ctrl = TextBoardCtrl.getInstance();

    TextBoardDoc() {
        db = new SQLDataBase();
    }

    public void Insert(String DataType, ArrayList<DataObject> datas)
    {
        db.InsertDB(DataType, datas);
    }

    public void Update()
    {

    }

    public void Delte()
    {

    }

    public ArrayList<DataObject[]> SelectDatasArray(String Query, int col_size)
    {
        return db.SelectDatasArray(Query, col_size);
    }


    public DataObject[] SelectDatas(String Query, int col_size)
    {
        return db.SelectDatas(Query, col_size);
    }

    public DataObject SelectData(String Query)
    {
        return db.SelectData(Query);
    }

    public DataObject getData(String Table, String[] OutDataName, HashMap<String, DataObject> InData)
    {
        return db.SelectData(Table, OutDataName, InData);
    }
}
