package app;

import java.io.Serializable;

public class Game implements Serializable {

    private String character, performance, win, score, map, notes, date, user;
    private int winN, id, idM, idC, shared;

    public Game(int idM, int idC, String map, String character, int win, String score, String performance, String date) {
        this.idM = idM;
        this.idC = idC;
        this.character = character;
        this.performance = performance;
        this.score = score;
        this.map = map;
        this.date = date;
        if(win == 1) this.win = "VICTORIA";
        else if(win == 0) this.win = "DERROTA";
        else this.win = "EMPATE";
        winN = win;
        notes = "";
        id = 0;
        shared = 0;
    }

    public Game(int idM, int idC, String map, String character, int win, String score, String performance, String date, String notes) {
        this.idM = idM;
        this.idC = idC;
        this.character = character;
        this.performance = performance;
        this.score = score;
        this.map = map;
        this.date = date;
        if(win == 1) this.win = "VICTORIA";
        else if(win == 0) this.win = "DERROTA";
        else this.win = "EMPATE";
        this.winN = win;
        this.notes = notes;
        shared = 0;
    }

    public Game(int id, int idM, int idC, String map, String character, int win, String score, String performance, String date, String notes) {
        this.id = id;
        this.idM = idM;
        this.idC = idC;
        this.character = character;
        this.performance = performance;
        this.score = score;
        this.map = map;
        this.date = date;
        if(win == 1) this.win = "VICTORIA";
        else if(win == 0) this.win = "DERROTA";
        else this.win = "EMPATE";
        this.winN = win;
        this.notes = notes;
        shared = 0;
    }

    public int getIdM() {
        return idM;
    }

    public void setIdM(int idM) {
        this.idM = idM;
    }

    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
        if(win.equals("DERROTA")) winN = 0;
        else if(win.equals("VICTORIA")) winN = 1;
        else winN = 2;
    }

    public int getWinN(){
        return winN;
    }

    public void setWinN(int winN){
        this.winN = winN;
        if(winN == 0) win = "DERROTA";
        else if(winN == 1) win = "VICTORIA";
        else win = "EMPATE";
    }

    public int getID(){
        return id;
    }

    public void setID(int id){this.id = id;}

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getShared() {
        return shared;
    }

    public void setShared(int shared) {
        this.shared = shared;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
