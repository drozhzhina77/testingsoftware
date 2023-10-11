import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Student<T> extends User {
    private String name;
    private String group;
    private T course;

    public Student() {
        super();
        name = "Иванов Иван";
        group = "ИПБ-20";
        course = null;
    }

    public Student(String username, String password, String name, String group, T course) {
        super(username, password, 0);
        this.name = name;
        this.group = group;
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public T getCourse() {
        return course;
    }

    public void setCourse(T course) {
        this.course = course;
    }

    public static ArrayList<User> getListUsers() throws MyFileException {
        ArrayList<User> listUsers = new ArrayList<>();
        //чтение личных данных из файла зарегистрированных учеников
        //yourname~12J44~0
        //Васильев Иван Константинович~ИПБ-20~4
        //или
        //Васильев Иван Константинович~ИПБ-20~четвёртый
        try (FileReader reader = new FileReader("accounts.txt")) {
            Scanner sc = new Scanner(reader);
            String curStr = sc.nextLine();
            if (!curStr.isEmpty()) {
                User adm = new User();
                int pos = curStr.indexOf("~");
                if (pos != -1) {
                    adm.setUsername(curStr.substring(0, pos));
                    curStr = curStr.substring(pos + 1);
                    pos = curStr.indexOf("~");
                    if (pos != -1) {
                        adm.setPassword(curStr.substring(0, pos));
                        curStr = curStr.substring(pos + 1);
                        adm.setAcessLevel(Integer.parseInt(curStr));
                        listUsers.add(adm);
                        while (sc.hasNextLine()) {
                            curStr = sc.nextLine();
                            //username~password~accessLevel
                            Student stud = new Student();
                            pos = curStr.indexOf("~");
                            if (pos != -1) {
                                stud.setUsername(curStr.substring(0, pos));
                                curStr = curStr.substring(pos + 1);
                                pos = curStr.indexOf("~");
                                if (pos != -1) {
                                    stud.setPassword(curStr.substring(0, pos));
                                    curStr = curStr.substring(pos + 1);
                                    stud.setAcessLevel(Integer.parseInt(curStr));
                                    //name~group~course
                                    if (sc.hasNextLine()) {
                                        curStr = sc.nextLine();
                                        pos = curStr.indexOf("~");
                                        if (pos != -1) {
                                            stud.setName(curStr.substring(0, pos));
                                            curStr = curStr.substring(pos + 1);
                                            pos = curStr.indexOf("~");
                                            if (pos != -1) {
                                                stud.setGroup(curStr.substring(0, pos));
                                                curStr = curStr.substring(pos + 1);
                                                stud.setCourse(curStr);
                                            }
                                        }
                                    }
                                    listUsers.add(stud);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            //System.out.println(ex.getMessage());
            throw (new MyFileException());
        }
        return listUsers;
    }

    //фио не должно содержать цифр
    public static boolean isNameValid(String fio) {
        boolean flag = true;
        char[] chars = fio.toCharArray();
        for (char character : chars) {
            if (Character.isDigit(character)) {
                flag = false;
            }
        }
        return flag;
    }
}
