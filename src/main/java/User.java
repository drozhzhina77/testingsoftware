import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class User implements UserImpl {

    private String username;
    private String password;
    private int acessLevel;
    private static User curUser;
    private DictionaryWords dictionaryWords;

    public User() {
        username = "IvanovIvanIPB20";
        password = "qwertyii20";
        acessLevel = 0;
    }

    public User(String username, String password, int accessLevel) {
        this.username = username;
        this.password = password;
        this.acessLevel = accessLevel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAcessLevel() {
        return acessLevel;
    }

    public void setAcessLevel(int acessLevel) {
        this.acessLevel = acessLevel;
    }

    public static void setCurUser(User cur) {
        curUser = cur;
    }

    public static User getCurUser() {
        return curUser;
    }

    public DictionaryWords getDictionary() {
        return dictionaryWords;
    }

    public void setDictionary(DictionaryWords dictionaryWords) {
        this.dictionaryWords = dictionaryWords;
    }

    @Override
    public void setDictionary() throws MyFileException {
        String login = this.username;
        login += ".txt";
        DictionaryWords dict = new DictionaryWords();
        File f = new File(login);
        if (f.exists() && f.isFile()) {
            this.dictionaryWords = DictionaryWords.makeGeneralDictionary(dict, login);
        } else {
            this.dictionaryWords = dict;
        }
    }

    public static ArrayList<User> getListUsers() throws MyFileException {
        ArrayList<User> listUsers = new ArrayList<>();
        //чтение пользователей из файла учетных записей
        //vladim_63~12J44~0 - пример строки из файла: 1) username 2) password 3) accessLevel
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
                            User us = new User();
                            pos = curStr.indexOf("~");
                            if (pos != -1) {
                                us.setUsername(curStr.substring(0, pos));
                                curStr = curStr.substring(pos + 1);
                                pos = curStr.indexOf("~");
                                if (pos != -1) {
                                    us.setPassword(curStr.substring(0, pos));
                                    curStr = curStr.substring(pos + 1);
                                    us.setAcessLevel(Integer.parseInt(curStr));
                                    if (sc.hasNextLine()) {
                                        sc.nextLine();
                                    }
                                    listUsers.add(us);
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

    @Override
    public double calcMark(ArrayList<Exercise> listExerc) { //вычисление процентов
        double mark = ExerciseWithVariants.getMaxCountExerc();
        int rightAnsw = 0;
        for (int i = 0; i < listExerc.size(); i++) {
            if (listExerc.get(i).getUserAnswer().trim().equals(listExerc.get(i).getRightAnswer())) {
                rightAnsw++;
            }
        }
        return rightAnsw / mark;
    }

    @Override
    public int convertBallsToMark(double mark){
        if (mark < 0.61) {
            return 2;
        } else if ((mark >= 0.61) && (mark < 0.76)) {
            return 3;
        } else if ((mark >= 0.76) && (mark < 0.91)) {
            return 4;
        } else {
            return 5;
        }
    }

    @Override
    public int calcMark(ArrayList<Exercise> listExerc, int rightAnsw) { //вычисление количества верных
        for (int i = 0; i < listExerc.size(); i++) {
            if (listExerc.get(i).getUserAnswer().trim().equals(listExerc.get(i).getRightAnswer())) {
                rightAnsw++;
            }
        }
        return rightAnsw;
    }

    @Override
    public void saveUsersResultsIntoFile(ArrayList<Exercise> listExerc) throws MyFileException {
        DictionaryWords dictTest = WinChooseCategory.getTestDict();
        ArrayList<Category> listCurCateg = dictTest.getListCategory();
        User us = User.getCurUser();
        try (FileWriter writer = new FileWriter("statistics.txt", true)) {
            writer.append("\n" + User.getCurUser().getUsername() + "~");
            //записываем в файл название категорий, по которым проводился опрос
            for (int i = 0; i < listCurCateg.size(); i++) {
                writer.append(listCurCateg.get(i).getName().trim() + "~");
            }
            //calcMark(listExerc);
            writer.append(Integer.toString(convertBallsToMark(us.calcMark(listExerc))));
            writer.flush();
        } catch (IOException e) {
            throw new MyFileException();
        }
    }

    public static ArrayList<Results> getListUsersResults() throws MyFileException {
        ArrayList<Results> listUsersResults = new ArrayList<>();
        ArrayList<User> listUsers = new ArrayList<>(Student.getListUsers());
        //чтение пользователей из файла статистики
        //vladim_63~природа~фрукты и ягоды~семья~5 - пример строки из файла: 1) username 2) nameCategories 3) mark
        try (FileReader reader = new FileReader("statistics.txt")) {
            Scanner sc = new Scanner(reader);
            while (sc.hasNextLine()) {
                String curStr = sc.nextLine();
                Results res = new Results();
                if (!curStr.isEmpty()) {
                    //vladim_63~природа~фрукты и ягоды~семья~5
                    int pos = curStr.indexOf("~");
                    if (pos != -1) {
                        String username = curStr.substring(0, pos);
                        User us = new User();
                        us = User.seekUserInSystem(username, listUsers);
                        if (us == null) {
                            continue;
                        }
                        res.setUser(us);

                        curStr = curStr.substring(pos + 1);
                        int mark = Character.getNumericValue(curStr.charAt(curStr.length() - 1));
                        res.setMark(mark);
                        curStr.substring(0, curStr.length() - 2);

                        ArrayList<String> namesCateg = new ArrayList<>();
                        pos = curStr.indexOf("~");
                        while (pos != -1) {
                            namesCateg.add(curStr.substring(0, pos));
                            curStr = curStr.substring(pos + 1);
                            pos = curStr.indexOf("~");
                        }
                        res.setNamesCateg(namesCateg);
                    }
                    listUsersResults.add(res);
                }
            }
        } catch (IOException ex) {
            //System.out.println(ex.getMessage());
            throw (new MyFileException());
        }
        return listUsersResults;
    }

    //ищет пользователя в списке пользователей по username
    public static User seekUserInSystem(String usname, ArrayList<User> listUsers) {
        for (int i = 0; i < listUsers.size(); i++) {
            if (listUsers.get(i).getUsername().equals(usname)) {
                return listUsers.get(i);
            }
        }
        return null;
    }

    /* Пояснение для регулярного выражения по проверке логина и пароля
    (?=.*[0-9]) цифра должна встречаться хотя бы один раз
    (?=.*[a-z]) строчная буква должна встречаться хотя бы один раз
    (?=.*[A-Z]) заглавная буква должна встречаться хотя бы один раз
    (?=.*[@#$%^&+=]) специальный символ должен встречаться хотя бы один раз
    (?=\\S+$) во всей строке не допускается наличие пробелов
    .{8,10} не менее 8 символов, не более 10 */

    public static boolean isUsernameValid(String usname) {
        //логин должен содержать хотя бы одну цифру, хотя бы одну строчную лат.букву,
        //не содержать пробелов, быть не менее 8 и не более 10 символов
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,10}";
        return usname.matches(pattern);
    }

    public static boolean isPasswordValid(String passwd) {
        //пароль должен содержать хотя бы одну цифру, хотя бы одну лат.букву (прописную и строчную),
        //хотя бы один специальный символ, не содержать пробелов, быть не менее 8 и не более 10 символов
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,10}";
        return passwd.matches(pattern);
    }
}
