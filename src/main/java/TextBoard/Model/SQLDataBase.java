package TextBoard.Model;

import TextBoard.INI.DataObject;
import TextBoard.INI.INI;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SQLDataBase {
    private Connection conn = null;
    private Statement ps = null;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static final String URL = "jdbc:mysql://localhost:3306/textboard";


    public SQLDataBase()
    {
        try{
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            ps = conn.createStatement();
        }
        catch (Exception e)
        {
            try {
                assert conn != null;
                conn.close();
            }
            catch (SQLException se)
            {
                System.out.println("DB 연동 실패");
            }

        }
    }

    public boolean InsertDB(String table,  ArrayList<DataObject> datas) {

        try{
            String query = "insert into " + table + " values (";
            for(var data : datas) {
                if(data.getData() == null)
                {
                    query += "null,";
                    continue;
                }

                String str = data.getData().getClass().getName();
                if(str.equals("java.lang.String") ||
                        str.equals("java.time.LocalDateTime"))
                {
                    query += "'" + data.getData() + "',";
                }
                else{
                    query +=  data.getData()+ ",";
                }
            }
            query = query.substring(0,query.length() - 1);

            query += ")";

            ps.executeUpdate(query);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public boolean UpdateDB(String table, ArrayList<String> col,  ArrayList<DataObject> datas, String where)
    {
        try {
            String query = "update " + table + "set ";
            for (int i = 0; i < col.size(); i++)
            {
                query += col.get(i) + "=";

                if(datas.get(i).getData() == null)
                {
                    query += "null,";
                    continue;
                }
                String str = datas.get(i).getData().getClass().getName();
                if(str.equals("java.lang.String") ||
                        str.equals("java.time.LocalDateTime"))
                {
                    query += "'" + datas.get(i).getData() + "',";
                }
                else{
                    query +=  datas.get(i).getData()+ ",";
                }
            }

            query = query.substring(0,query.length() - 1);

            query += where;

            ps.executeUpdate(query);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public ArrayList<DataObject[]> SelectDatasArray(String query, int size)
    {
        ArrayList<DataObject[]> datas = new ArrayList<DataObject[]>();
        try
        {
            ResultSet res = ps.executeQuery(query);

            int idx;
            while (res.next()) {
                DataObject[] data = new DataObject[size];
                idx = 1;
                while (idx <= size)
                {
                    data[idx-1] = new DataObject(res.getString(idx));
                    idx++;
                }
                datas.add(data);
            }
        }
        catch (Exception e)
        {
            datas = null;
        }
        return datas;
    }

    public DataObject[] SelectDatas(String query, int size)
    {
        DataObject[] data = new  DataObject[size];
        try
        {
            ResultSet res = ps.executeQuery(query);

            int idx;
            while (res.next()) {
                idx = 1;
                while (idx <= size)
                {
                    data[idx - 1] = new DataObject(res.getString(idx));
                    idx++;
                }
            }
        }
        catch (Exception e)
        {
            data = null;
        }
        return data;
    }

    public DataObject SelectData(String query)
    {
        DataObject data = null;
        try
        {
            ResultSet res = ps.executeQuery(query);

            while (res.next()) {
                data = new DataObject(res.getString(1));
            }
        }
        catch (Exception e)
        {
            data = null;
        }
        return data;
    }

    public DataObject SelectData(String Table, String[] OutDataName, HashMap<String, DataObject> InData)
    {
        DataObject data = null;
        String query;
        try
        {
            query = "select ";
            for(String str : OutDataName)
            {
                query += str + ",";
            }

            query.substring(0,query.length() - 1);

            query += " from ";
            query += Table;

            for(Map.Entry map : InData.entrySet())
            {
                query += map.getKey();

                if(map.getValue().equals(INI.Q_IS_NULL) ||
                        map.getValue().equals(INI.Q_IS_NOT_NULL))
                {
                    query += map.getValue();
                }
                else {
                    query += " = ";
                    query += map.getValue();
                }

                query += " and ";
            }
            query.substring(0,query.length() - 4);

            ResultSet res = ps.executeQuery(query);

            while (res.next()) {
                data = new DataObject(res.getString(1));
            }
        }
        catch (Exception e)
        {
            data = null;
        }
        return data;
    }

}


