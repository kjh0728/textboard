package TextBoard.Controller;

import TextBoard.INI.DataObject;
import TextBoard.INI.INI;
import TextBoard.Model.TextBoardDoc;
import TextBoard.View.TextBoardApp;

import java.util.ArrayList;
import java.util.HashMap;


public class TextBoardCtrl {
    private static final TextBoardCtrl instance = new TextBoardCtrl();

    public static TextBoardCtrl getInstance()
    {
        return instance;
    }

    private TextBoardDoc doc;
    private TextBoardApp app;

    TextBoardCtrl()
    {
        this.doc = TextBoardDoc.getInstance();
        this.app = TextBoardApp.getInstance();
    }

    public void Modify(int CallDest, String DataType, int QueryCommend, ArrayList<DataObject> datas)
    {
        switch (CallDest)
        {
            case INI.CD_VIEW:
                Modify(INI.DT_MEMBER, INI.QC_INSERT, datas);
                break;
            case INI.CD_DOC:
                break;
        }
    }

    public void Modify(String DataType, int QueryCommend, ArrayList<DataObject> datas)
    {
        switch (QueryCommend)
        {
            case INI.QC_INSERT ->
            {
                doc.Insert(DataType, datas);
            }
            case INI.QC_UPDATE -> {

            }
            case INI.QC_DELETE -> {

            }
        }
    }

    public ArrayList<DataObject[]> NotifyDatasArray(int CallDest, String Query, int col_size)
    {
        return doc.SelectDatasArray(Query, col_size);
    }

    public DataObject[] NotifyDatas(int CallDest, String Query, int col_size)
    {
        return doc.SelectDatas(Query, col_size);
    }

    public DataObject NotifyData(int CallDest, String Query)
    {
        return doc.SelectData(Query);
    }

    public DataObject getData(String Table, String[] OutDataName, HashMap<String, DataObject> InData)
    {
        return doc.getData(Table, OutDataName, InData);
    }


}





