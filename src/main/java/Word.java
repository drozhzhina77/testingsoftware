import java.util.Stack;

public class Word {
    private String engValue;
    private String rightTransc;
    private String ruValue;
    private String nameCategory;
    private int kRightAnsw; //количество правильных ответов в тесте: 0..3 (0 - добавили в словарь, 3 - выучили)

    Word() {
        this.engValue = "";
        this.rightTransc = "";
        this.ruValue = "";
        this.nameCategory = "";
        this.kRightAnsw = 0;
    }

    Word(String engValue, String rightTransc, String ruValue, String category) {
        this.engValue = engValue;
        this.rightTransc = rightTransc;
        this.ruValue = ruValue;
        this.nameCategory = category;
        this.kRightAnsw = 0;
    }

    public String getEngValue() {
        return engValue;
    }

    public void setEngValue(String engValue) {
        this.engValue = engValue;
    }

    public String getRightTransc() {
        return rightTransc;
    }

    public void setRightTransc(String rightTransc) {
        this.rightTransc = rightTransc;
    }

    public String getRuValue() {
        return ruValue;
    }

    public void setRuValue(String ruValue) {
        this.ruValue = ruValue;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public int getkRightAnsw() {
        return kRightAnsw;
    }

    public void setkRightAnsw(int kRightAnsw) {
        this.kRightAnsw = kRightAnsw;
    }

    //слово не должно содержать цифр
    public static boolean isWordValid(String str) {
        boolean flag = true;
        char[] chars = str.toCharArray();
        for (char character : chars) {
            if (Character.isDigit(character)) {
                flag = false;
            }
        }
        return flag;
    }

    //транскрипция должна быть без цифр и в квадратных скобках, квадратные скобки должны быть уравновешены
    public static boolean isTranscValid(String str) {
        boolean flag = isWordValid(str);
        if (str.length() < 3)
            return false;
        Stack<Character> brackets = new Stack<>();
        Character symb;
        boolean flag2 = true;
        for (int i = 0; i < str.length(); i++) {
            if ((str.charAt(i) == '(') || (str.charAt(i) == '{') || (str.charAt(i) == '['))
                brackets.push(str.charAt(i));
            if ((str.charAt(i) == ')') || (str.charAt(i) == '}') || (str.charAt(i) == ']')) {
                if (brackets.empty()) {
                    flag2 = false;
                    break;
                } else {
                    symb = brackets.pop();
                    if (((symb == '(') && (str.charAt(i) != ')')) || ((symb == '{') && (str.charAt(i) != '}')) ||
                            ((symb == '[') && (str.charAt(i) != ']'))){
                        flag2 = false;
                        break;
                    }
                }
            }
        }
        if (!brackets.empty()) {
            flag2 = false;
        }

        if (str.charAt(0) != '[' || str.charAt(str.length() - 1) != ']') {
            flag = false;
        }
        return flag && flag2;
    }
}
