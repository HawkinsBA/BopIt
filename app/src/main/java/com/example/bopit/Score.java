package com.example.bopit;

public class Score {
    private String name;
    private int score;

    public Score(){
        name = "default";
        score = 0;
    }
    public Score(String name, int score){
        this.name = name;
        this.score = score;
    }
    public String toString(){
        return name+":"+score;
    }
    public String dispScore(){
        return "Player: "+name+"\nScore: "+score;
    }
    public String getName(){
        return this.name;
    }
    public int getScore(){
        return this.score;
    }
    public static Score parse(String s){
        String[] parts = s.split(":");
        return new Score(parts[0],Integer.parseInt(parts[1]));
    }

    public boolean isHighscore(){
        //
        return false;
    }
}

