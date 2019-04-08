package com.example.bopit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

class DataManager {
    private File saveDirScore;

    private static final String PLACE_DIR = "places";

     public DataManager(File saveDir){
         this.saveDirScore = new File(saveDir, PLACE_DIR);
         if (!saveDirScore.exists()) {
             saveDirScore.mkdir();
         }

     }

     public void saveScore(Score p) throws IOException {
         File output = new File(saveDirScore, p.getName()+p.getScore());
         PrintWriter pw = new PrintWriter(new FileWriter(output));
         pw.println(p.toString());
         pw.close();
     }

     public Score openScore(String name) throws IOException {
         return openScore(new File(saveDirScore, name));
     }

     public Score openScore(File input) throws IOException {
        Scanner s = new Scanner(input);
        if (s.hasNextLine()) {
            String text = s.nextLine();
            s.close();
            return Score.parse(text);
        } else {
            return null;
        }
    }

    public ArrayList<Score> loadAllScores() throws IOException {
        ArrayList<Score> result = new ArrayList<>();
        for(File f: saveDirScore.listFiles()){
            if (f.isFile()) {
                Score p = openScore(f);
                if (p != null)
                    result.add(p);
            }
        }
        return result;
    }

    public void removeAllScores(){
        for(File f: saveDirScore.listFiles()) {
            if (f.isFile()) {
                f.delete();
            }
        }
    }
}
