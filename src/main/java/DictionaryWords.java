import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryWords implements DictionaryWordsImpl {
    private ArrayList<Category> category = new ArrayList<>();

    public DictionaryWords() {
        ArrayList<Category> category = new ArrayList<>();
    }

    public DictionaryWords(Category category) { //агрегация
        this.category.add(category);
    }

    public void addCategory(Category category) { //агрегация
        this.category.add(category);
    }

    public ArrayList<Category> getListCategory() {
        return category;
    }

    public Category getCategoryByName(String nameCateg) {
        Category categ = null;
        ArrayList<Category> list = getListCategory();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(nameCateg.trim())) {
                categ = list.get(i);
            }
        }
        return categ;
    }

    public Word getWordInDict(Word seekWord) {
        Category categ = getCategoryByName(seekWord.getNameCategory());
        if (categ != null) {
            ArrayList<Word> listWords = categ.getListWord();
            for (int i = 0; i < listWords.size(); i++) {
                String eng = listWords.get(i).getEngValue();
                String transc = listWords.get(i).getRightTransc();
                String ru = listWords.get(i).getRuValue();
                if (eng.equals(seekWord.getEngValue()) && (transc.equals(seekWord.getRightTransc())) &&
                        (ru.equals(seekWord.getRuValue()))) {
                    return listWords.get(i);
                }
            }
        }
        return null;
    }

    //создание словаря
    public static DictionaryWords makeGeneralDictionary(DictionaryWords dict, String filename) throws MyFileException {
        //Dictionary dict = new Dictionary();
        try (FileReader reader = new FileReader(filename)) {
            Scanner sc = new Scanner(reader);
            if (sc.hasNextLine()) {
                String nameCateg = sc.nextLine().trim().toLowerCase().replaceAll("\\s+", " ");
                if (!nameCateg.isEmpty()) {
                    Category categ = new Category(nameCateg);
                    while (sc.hasNextLine()) {
                        String curStr = sc.nextLine().trim().toLowerCase().replaceAll("\\s+", " ");
                        if (curStr.equals("") && sc.hasNextLine()) { //если следующая категория
                            nameCateg = sc.nextLine().trim().toLowerCase().replaceAll("\\s+", " ");
                            dict.addCategory(categ);
                            categ = new Category(nameCateg);
                        } else {
                            int pos = curStr.indexOf("[");
                            if (pos != -1) {
                                Word word = new Word(curStr.substring(0, pos).trim().toLowerCase().
                                        replaceAll("\\s+", " "),
                                        "", "", nameCateg);
                                curStr = curStr.substring(pos); //удалили из строки англ.слово
                                pos = curStr.indexOf("]");
                                if (pos != -1) {
                                    //считываем первую часть транскрипции
                                    String buf = curStr.substring(0, pos + 1).trim().toLowerCase().
                                            replaceAll("\\s+", " ");
                                    //удалили из строки первую часть транскрипци
                                    curStr = curStr.substring(pos + 1).trim().toLowerCase().
                                            replaceAll("\\s+", " ");
                                    pos = curStr.indexOf("]");
                                    if (pos != -1) {
                                        buf += curStr.substring(0, pos + 1).trim().toLowerCase().
                                                replaceAll("\\s+", " ");
                                        //удалили из строки вторую часть транскрипции
                                        curStr = curStr.substring(pos + 1).trim().toLowerCase().
                                                replaceAll("\\s+", " ");
                                    }
                                    word.setRightTransc(buf);
                                    //если словарь пользователя, считываем еще количество правильных ответов слова
                                    if (!filename.equals("dictionary.txt")) {
                                        try {
                                            word.setRuValue(curStr.substring(0, curStr.length() - 1).trim().toLowerCase().
                                                    replaceAll("\\s+", " ")); //с текущего по предпоследний
                                            word.setkRightAnsw(Integer.parseInt(curStr.substring(curStr.length() - 1))); //последний
                                        } catch (NumberFormatException e) {
                                            word.setRuValue(curStr.trim().toLowerCase().
                                                    replaceAll("\\s+", " "));
                                        }
                                    } else { //если общий словарь
                                        word.setRuValue(curStr);
                                    }
                                }
                                categ.addWordIntoCategory(word); //добавляем слово в категорию
                            }
                        }
                    }
                    dict.addCategory(categ); //добавили последнюю категорию
                }
            }
        } catch (IOException ex) {
            throw (new MyFileException());
        }
        return dict;
    }

    //обновить файл словаря
    public static void apdateFileDictionary(DictionaryWords dict, String fileName) throws MyFileException {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            ArrayList<Category> listCateg = new ArrayList<>(dict.getListCategory());
            for (int i = 0; i < listCateg.size(); i++) {
                writer.append(listCateg.get(i).getName());
                ArrayList<Word> listWords = new ArrayList<>(listCateg.get(i).getListWord());
                for (int j = 0; j < listWords.size(); j++) {
                    writer.append("\n" + listWords.get(j).getEngValue() + " " + listWords.get(j).getRightTransc() +
                            " " + listWords.get(j).getRuValue());
                    //если словарь пользователя, то добавляем еще количество правильных ответов слова
                    if (!fileName.equals("dictionary.txt")) {
                        writer.append(" " + listWords.get(j).getkRightAnsw());
                    }
                }
                if (i != listCateg.size() - 1) { //если последнее слово категории
                    writer.append("\n\n");
                }
            }
            writer.flush();
        } catch (IOException e) {
            throw new MyFileException();
        }
    }

    //поиск нового слова в категории
    public static boolean seekWordInCategory(Category categ, Word newWord) {
        ArrayList<Word> listWords = categ.getListWord();
        for (int j = 0; j < listWords.size(); j++) {
            if (listWords.get(j).getEngValue().equals(newWord.getEngValue())) {
                return true;
            }
        }
        return false;
    }

    //добавить в общий словарь слово и обновить файл словаря
    public static DictionaryWords addWordIntoGeneralDictionary(DictionaryWords dict, String nameCateg, Word newWord,
                                                               String fileName) throws MyFileException {
        ArrayList<Category> listCateg = dict.getListCategory();
        nameCateg = nameCateg.trim().toLowerCase().replaceAll("\\s+", " ");
        boolean isCategExist = false;
        for (int i = 0; i < listCateg.size(); i++) {
            //если категория найдена, добавляем слово
            if (listCateg.get(i).getName().equals(nameCateg)) {
                isCategExist = true;
                //если нет этого слова в категории, то добавляем
                if (!seekWordInCategory(listCateg.get(i), newWord)) {
                    listCateg.get(i).addWordIntoCategory(newWord);
                }
                break;
            }
        }
        if (!isCategExist) { //добавляем новую категорию и в нее слово
            Category categ = new Category(nameCateg, newWord);
            dict.addCategory(categ);
        }
        DictionaryWords.apdateFileDictionary(dict, fileName);
        return dict;
    }

    //добавить в словарь пользователя слово (оно берется из тестов и экзаменов)
    @Override
    public void addWordIntoUserDictionary(Word newWord) throws MyFileException {
        User us = User.getCurUser();
        String login = us.getUsername();
        login += ".txt";
        DictionaryWords.addWordIntoGeneralDictionary(this, newWord.getNameCategory(), newWord, login);
        us.setDictionary(this);
    }

    @Override
    public void delWordFromUserDictionary(Word delWord) throws MyFileException {
        User us = User.getCurUser();
        String login = us.getUsername();
        login += ".txt";
        this.delWordFromGeneralDictionary(delWord, login);
        us.setDictionary(this);
    }

    public void delWordFromGeneralDictionary(Word delWord, String filename) throws MyFileException {
        DictionaryWords dict = this;
        ArrayList<Category> listCateg = dict.getListCategory();
        for (int i = 0; i < listCateg.size(); i++) {
            if (listCateg.get(i).getName().equals(delWord.getNameCategory())) {
                ArrayList<Word> listWords = listCateg.get(i).getListWord();
                for (int j = 0; j < listWords.size(); j++) {
                    if (listWords.get(j).getEngValue().equals(delWord.getEngValue())) {
                        dict.getListCategory().get(i).getListWord().remove(j); //удаляем слово
                        j--;
                    }
                }
            }
            if (listCateg.get(i).getListWord().size() < 1) { //если в категории нет слов
                dict.getListCategory().remove(i);
                i--;
            }
        }
        DictionaryWords.apdateFileDictionary(dict, filename);
    }
}
