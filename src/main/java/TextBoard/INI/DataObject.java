package TextBoard.INI;

public class DataObject<T> {
    private T data;

    public DataObject(String str) {
        data = (T) str;
    }

    public DataObject(T d) {
        this.data = d;
    }

    public T getData() {
        return data;
    }
}

