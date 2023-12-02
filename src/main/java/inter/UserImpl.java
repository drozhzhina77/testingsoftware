package inter;

import model.*;
import java.util.ArrayList;

public interface UserImpl {
    void setDictionary() throws MyFileException;

    double calcMark(ArrayList<Exercise> listExerc);

    int convertBallsToMark(double mark);

    int calcMark(ArrayList<Exercise> listExerc, int rightAnsw);

    void saveUsersResultsIntoFile(ArrayList<Exercise> listExerc) throws MyFileException;
}
