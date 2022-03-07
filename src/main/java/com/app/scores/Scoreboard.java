package com.app.scores;

import com.app.controller.GameController;
import com.app.controller.ScoreController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Management of the csv files for the scoreboard
 *
 */
public class Scoreboard {

    final int mapSetNameIndex = 0;
    final int levelNameIndex = 1;
    final int levelNumberIndex = 2;
    final int usernameIndex = 3;
    final int moveCountIndex = 4;
    final int timeIndex = 5;

    private String mapName;
    private int levelNo;

    private final GameController gameController;
    private final ScoreController scoreController;

    String sortedFile;

    /**
     * scoreboard constructor
     *
     * @param gameController instance of game controller
     * @param scoreController instance of the score controller
     */
    public Scoreboard(GameController gameController,
                      ScoreController scoreController) {
        this.gameController = gameController;
        this.scoreController = scoreController;
    }

    /**
     * the function to call write to csv, passing necessary parameters
     */
    public void callWriteRawCSV() {
        writeRaw(
                gameController.getMapSetName(),
                gameController.getCurrentLevel().getName(),
                String.valueOf(gameController.getCurrentLevel().getIndex()),
                gameController.getUsername(),
                String.valueOf(gameController.getMoveCount()),
                gameController.getTimer().getText()
        );
    }

    /**
     * function to write the the raw csv file
     *
     * @param mapName map set name
     * @param levelName level name
     * @param levelNumber level number
     * @param username user name
     * @param moves moves completed
     * @param time time completed
     */
    public void writeRaw(String mapName,
                             String levelName, String levelNumber,
                             String username, String moves, String time) {

        File whereWrite = new File(System.getProperty("user.dir")
                + "/src/main/resources/scores/ScoreboardRaw.csv");

        try {
            FileWriter fw = new FileWriter(whereWrite, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            FileReader fr = new FileReader(whereWrite);
            BufferedReader br = new BufferedReader(fr);

            if(br.readLine() == null) {
                pw.println(
                        "MapSetName,"
                                + "LevelName," + "LevelNumber,"
                                + "Username," + "Moves," + "Time"
                );
            }
            br.close();

            pw.println(mapName + "," + levelName + "," + levelNumber + ","
                    + username + "," + moves + "," + time);
            pw.flush();
            pw.close();

            initSortRaw();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * initializes sorting of file
     *
     * @throws IOException catch error from parse raw to sort
     */
    private void initSortRaw() throws IOException {
        String inFile = "ScoreboardRaw";

        mapName = gameController.getMapSetName();
        levelNo = gameController.getCurrentLevel().getIndex();

        //Text RegExp by highlight>Alt+Enter>CheckRegExp --This is an example of language injection
        String mapNameVal = mapName.replaceAll("[^a-zA-Z0-9_!-]", "");
        String levelNum = String.valueOf(levelNo);

        //System.out.println(mapNameVal);
        //System.out.println(levelNum);

        sortedFile = mapNameVal + "_" + levelNum;

        parseRawToSort(
                inFile + ".csv",
                sortedFile + ".csv"
        );
    }

    /**
     * return the sorted file as string
     *
     * @return sorted file
     */
    public String getSortedFile() {
        return sortedFile;
    }

    /**
     * parse the file for sorting
     *
     * @param fileIn input file url as string
     * @param fileOut output file url as string
     * @throws IOException catch exceptions from write sorted function
     */
    private void parseRawToSort(String fileIn, String fileOut) throws IOException {
        File input = new File(System.getProperty("user.dir")
                + "/src/main/resources/scores/" + fileIn);

        File output = new File(System.getProperty("user.dir")
                + "/src/main/resources/scores/sorted/" + fileOut);

        List<String> unsortedScores = readRaw(input);
        String columnHeaders = unsortedScores.get(0);
        List<ScoreboardManager> sortedScores = sortValues(unsortedScores);

        writeSorted(output, sortedScores, columnHeaders);
    }

    /**
     * read the raw csv file
     *
     * @param input the input file object
     * @return list of strings read in
     * @throws IOException catch exceptions from read all lines
     */
    private List<String> readRaw(File input) throws IOException {
        Path path = Paths.get(input.toURI());
        return Files.readAllLines(path);
    }

    /**
     * function to write the sorted csv file
     *
     * @param output output file object
     * @param scores the scores to write
     * @param columnHeaders column header as string
     * @throws IOException catch exceptions while writing
     */
    private void writeSorted(File output, List<ScoreboardManager> scores,
                             String columnHeaders) throws IOException {
        Path path = Paths.get(output.toURI());
        List<String> sortedScores = new ArrayList<>(scores.size());

        sortedScores.add(0, columnHeaders);

        for (ScoreboardManager s : scores) {
            sortedScores.add(s.toString());
        }

        Files.write(path, sortedScores);
    }

    /**
     * main function that sorts the values of the scores
     *
     * @param rawList raw list of rows as strings
     * @return list of scores
     */
    private List<ScoreboardManager> sortValues(List<String> rawList) {
        List<ScoreboardManager> scores = new ArrayList<>(rawList.size());

        for (int i = 1; i < rawList.size(); i++) {
            String row = rawList.get(i);
            String[] columns = row.split(",");

            ScoreboardManager newRow = new ScoreboardManager(
                    columns[mapSetNameIndex],
                    new LevelInfo(
                            columns[levelNameIndex],
                            Integer.parseInt(columns[levelNumberIndex])
                    ),
                    new PlayerStats(
                            columns[usernameIndex],
                            Integer.parseInt(columns[moveCountIndex]),
                            columns[timeIndex]
                    )
            );

            //filters for level and mapSet
            if(columns[mapSetNameIndex].equals(mapName)
                    && Integer.parseInt(columns[levelNumberIndex]) == levelNo)
                scores.add(newRow); //adds new row to list
        }

        Collections.sort(scores); //sorts the list into a new list
        return scores; //outputs the new List
    }
}
