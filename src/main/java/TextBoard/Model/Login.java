package TextBoard.Model;

public class Login {
    private boolean status;
    private String id;

    public Login(boolean status) {
        this.status = status;
        this.id = "";
    }

    public void login(String id) {
        this.status = true;
        this.id = id;
    }

    public void logout() {
        this.status = false;
        this.id = "";
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
