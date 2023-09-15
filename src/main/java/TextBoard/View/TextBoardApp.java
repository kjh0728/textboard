
package TextBoard.View;

import TextBoard.INI.*;
import TextBoard.Model.*;
import TextBoard.Controller.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class TextBoardApp {

    private static final TextBoardApp instance = new TextBoardApp();

    public static TextBoardApp getInstance()
    {
        return instance;
    }

    Scanner scan = new Scanner(System.in);
    Login login = new Login(false);
    String title = "";
    String body = "";
    SQLDataBase db = new SQLDataBase();

    TextBoardCtrl ctrl;

    public void start()
    {
        ctrl = TextBoardCtrl.getInstance();
        String menu = "";

        var idx = -1;

        while (!menu.equals("exit")) {
            System.out.print("메뉴를 선택해 주세요.");
            if(login.isStatus())
            {
                System.out.print("["+login.getId()+"(" +
                        ctrl.NotifyData(INI.CD_DOC, "Select NICKNAME from member where ID = " + login.getId()).getData() + ")" + "]");
            }

            System.out.println(" (signup : 회원가입, login : 로그인, add : 게시글 추가, list : 게시글 조회, update : 게시글 수정, delete : 게시글 삭제, exit : 프로그램 종료)");

            menu = scan.nextLine();

            switch (menu) {
                case "signup" ->
                {
                    signup();
                }
                case "login" ->
                {
                    login();
                }
                case "logout"->
                {
                    logout();
                }
                case "add" -> {
                    add();
                } case "list" -> {
                    listAll();
                }
                case "update" -> {
                    if (!loginStatus() || isEmpty())
                    {
                        continue;
                    }

                    System.out.println("수정할 게시글의 번호를 입력해 주세요. : ");
                    idx = scan.nextInt();
                    scan.nextLine();

                    update(idx);
                }
                case "delete" -> {
                    if ( !loginStatus() || isEmpty() )
                    {
                        continue;
                    }
                    System.out.println("삭제할 게시글의 번호를 입력해 주세요. : ");
                    idx = scan.nextInt();
                    scan.nextLine();

                    delete(idx);

                }
                case "detail" -> {
                    if(isEmpty())
                    {
                        continue;
                    }
                    System.out.println("상세보기 할 게시글의 번호를 입력해 주세요. : ");
                    idx = scan.nextInt();
                    scan.nextLine();

                    detail(idx);
                }
                case "search" -> {
                    if(isEmpty())
                    {
                        continue;
                    }
                    System.out.println("검색 키워드를 입력해주세요. :");
                    String str = scan.nextLine();
                    // 재구현 필요
                }
                case "exid" -> {
                }
                default -> System.out.println("없는 메뉴입니다.");
            }
        }

        System.out.println("프로그램을 종료합니다.");
    }

    public void signup()
    {
        System.out.println("==== 회원 가입을 진행합니다 ===");
        while (true)
        {
            System.out.println("ID를 입력해 주세요.");


            String  id = scan.nextLine();

            String pw;
            String name;

            if(ctrl.NotifyData(INI.CD_DOC, "SELECT ID FROM MEMBER WHERE ID = '" + id + "'") == null)
            {
                System.out.println("사용할 수 있는 ID 입니다.");

                System.out.println("비밀번호를 입력해 주세요.");
                pw =  scan.nextLine();

                System.out.println("닉네임을 입력해 주세요.");
                name =  scan.nextLine();

                ArrayList<DataObject> arr = new ArrayList<DataObject>();
                arr.add(new DataObject(id));
                arr.add(new DataObject(pw));
                arr.add(new DataObject(name));
                ctrl.Modify(INI.CD_DOC,INI.DT_MEMBER, INI.QC_INSERT, arr);

                //DataCenter.SetMember(id, new Member(pw, name));
                System.out.println("==== 회원 가입이 완료되었습니다. ===");
                break;
            }
            else {
                System.out.println("중복된 ID 입니다. \n다시 입력해 주세요.");
            }
        }
    }

    public void login()
    {
        System.out.println("ID를 입력해 주세요.");
        String id = scan.nextLine();
        System.out.println("PW를 입력해 주세요.");
        String pw = scan.nextLine();
        var member = ctrl.NotifyDatas(INI.CD_DOC, "SELECT * FROM MEMBER WHERE ID = '" + id + "'", INI.MC_END);
        if(member != null)
        {
            if(member[INI.MC_PW].getData().equals(pw))
            {
                login.login(id);
                System.out.println(member[INI.MC_NICKNAME].getData() + "님 환영합니다.");
            }
        }
        else{
            System.out.println("없는 회원정보입니다.\nID와 비밀번호를 확인해 주세요.");
        }
    }

    public boolean loginStatus()
    {
        if(!login.isStatus())
        {
            System.out.println("로그인이 필요한 서비스 입니다.");
            return false;
        }
        return true;
    }

    public void logout()
    {
        if(!loginStatus())
        {
            return;
        }

        login.logout();
        System.out.println("로그아웃 되었습니다.");
    }

    public boolean idCompair(int idx)
    {
        var count = ctrl.NotifyData(INI.CD_DOC, "SELECT COUNT(ID) FROM article WHERE num = " + idx).getData();
        if(!count.equals("0"))
        {
            return true;
        }
        System.out.println("본인이 쓴 글이 아닙니다.");
        return  false;
    }

    public void add() {
        if(!loginStatus())
        {
            return;
        }

        System.out.println("제목을 입력해 주세요. : ");
        title = scan.nextLine();
        System.out.println("내용을 입력해 주세요. : ");
        body = scan.nextLine();
        ArrayList<DataObject> datas = new ArrayList<DataObject>();
        datas.add(new DataObject(null));
        datas.add(new DataObject(login.getId()));
        datas.add(new DataObject(title));
        datas.add(new DataObject(body));
        datas.add(new DataObject(LocalDateTime.now()));
        datas.add(new DataObject(0));
        datas.add(new DataObject(0));
        if (db.InsertDB("article", datas)) {
            System.out.println("게시물이 추가 되었습니다.");
        }
    }

    public void listAll() {
        var articles = ctrl.NotifyDatasArray(INI.CD_DOC, "SELECT * FROM article", INI.AC_END);
        for (int i = 0; i < articles.size(); i++) {
            System.out.println("=============================");
            System.out.printf("번호 : %s \n", articles.get(i)[INI.AC_NUM].getData());
            System.out.printf("제목 : %s \n", articles.get(i)[INI.AC_TITLE].getData());

        }
        System.out.println("=============================");
    }

    public void list(boolean detail, int num)
    {
        var article = ctrl.NotifyDatas(INI.CD_DOC, "Select * from article where NUM = " + num, INI.AC_END);
        if(detail)
        {
            System.out.println("=============================");

            System.out.printf("번호 : %s \n", article[INI.AC_NUM].getData());
            System.out.printf("제목 : %s \n", article[INI.AC_TITLE].getData());
            System.out.printf("내용 : %s \n", article[INI.AC_CONTENT].getData());
            System.out.printf("등록시간 : %s \n", article[INI.AC_ADD_TIME].getData());
            System.out.printf("조회수 : %s \n", article[INI.AC_VIEWNUM].getData());
            System.out.printf("추천수 : %s \n", article[INI.AC_GOOD].getData());

            var comms = ctrl.NotifyDatasArray(INI.CD_DOC, "Select * from comments where ARTICLE_NUM =" + num, INI.CC_END);
            if(comms.size() != 0)
            {
                for(int i = 0; i<comms.size(); i++)
                {
                    System.out.println("=============================");
                    System.out.println("댓글 내용 : " + comms.get(i)[INI.CC_CONTENT].getData());
                    System.out.println("댓글 작성일 : " + comms.get(i)[INI.CC_ADD_TIME].getData());
                }
            }
        }
        else
        {
            System.out.println("=============================");
            System.out.printf("번호 : %s \n", article[INI.AC_NUM].getData());
            System.out.printf("제목 : %s \n", article[INI.AC_TITLE].getData());
        }
    }

    public boolean isEmpty()
    {
        var count = ctrl.NotifyData(INI.CD_DOC, "SELECT COUNT(*) FROM article").getData();
        if(count.equals("0"))
        {
            System.out.println("등록된 게시글이 없습니다.");
            return true;
        }
        return false;
    }

    public boolean findNewsIDX(int idx) {
        if(!isEmpty())
        {
            var count = ctrl.NotifyData(INI.CD_DOC, "SELECT COUNT(*) FROM article").getData();
            if(!count.equals("0"))
            {
                return true;
            }
            System.out.println("없는 게시글 번호 입니다.");
        }
        return false;
    }

    public void update(int idx) {
        if(!idCompair(idx))
        {
            System.out.println("본인이 쓴 글만 수정할 수 있습니다.");
            return;
        }

        if (findNewsIDX(idx)) {
            System.out.println("제목을 입력해 주세요. : ");
            title = scan.nextLine();
            System.out.println("내용을 입력해 주세요. : ");
            body = scan.nextLine();

            // 재구현 필요
//            if(SQLParser.UpdateNews(idx, title,body))
//            {
//                System.out.println("게시글이 수정되었습니다.");
//            }
//            else {
//                System.out.println("게시글 수정에서 에러가 발생했습니다.");
//            }
        }
    }

    public void delete(int idx) {

        // 재구현 필요
//        if(!idCompair(idx))
//        {
//            System.out.println("본인이 쓴 글만 삭제할 수 있습니다.");
//            return;
//        }
//
//        if (findNewsIDX(idx)) {
//            try {
//                SQLParser.removeNews(idx);
//            } catch (Exception e) {
//                System.out.println("게시글 삭제에서 에러가 발생했습니다.");
//            }
//            System.out.println("게시글이 삭제되었습니다.");
//        }
    }

    public void detail(int idx)
    {
        if(findNewsIDX(idx))
        {
            int sub_menu = -1;
            while (sub_menu != 5) {

                list(true, idx);

                System.out.println("상세보기 추가 기능을 선택해주세요.(1. 댓글 등록, 2. 추천, 3. 수정, 4. 삭제, 5. 목록으로) :");
                try {
                    sub_menu = scan.nextInt();
                    scan.nextLine();
                } catch (Exception e) {
                    scan.nextLine();
                    System.out.println("상세보기 추가 기능은 숫자로만 입력할 수 있습니다.");
                    sub_menu = 0;
                    continue;
                }

                sub_menu = detailMenu(idx, sub_menu);
            }
        }
    }


    public int detailMenu(int idx, int menu_idx)
    {
        switch (menu_idx) {
            case 1 -> {
                if(!loginStatus())
                {
                    return 0;
                }

                System.out.println("댓글 내용 : ");
                body = scan.nextLine();
                ArrayList<DataObject> datas = new ArrayList<DataObject>();
                datas.add(new DataObject(idx));
                datas.add(new DataObject(body));
                datas.add(new DataObject(LocalDateTime.now()));
                db.InsertDB("comments", datas);
            }
            case 2 ->
            {
                // 재구현 필요
//                if(!loginStatus())
//                {
//                    return 0;
//                }
//                SQLParser.getNews(idx).GoodPush();
            }
            case 3 -> {
                if(!loginStatus())
                {
                    return 0;
                }
                update(idx);
            }
            case 4 -> {
                if(!loginStatus())
                {
                    return 0;
                }

                delete(idx);
                System.out.println("메인 메뉴로 돌아갑니다.");
                menu_idx = 5;
            }
            case 5 -> {
            }
            default -> System.out.println("없는 메뉴입니다.");
        }
        return menu_idx;
    }
}

