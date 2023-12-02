package inter;

import model.*;

public interface DictionaryWordsImpl {
    //добавить в словарь пользователя слово (оно берется из тестов)
    void addWordIntoUserDictionary(Word newWord) throws MyFileException;

    //удаляет слово из словаря пользователя и обновляет файл словаря
    void delWordFromUserDictionary(Word delWord) throws MyFileException;
}


