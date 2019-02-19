package com.stucom.flx.VCTR_DRRN.model;

public class Player {
    private Integer id;
    private String name;
    private String email;
    private String avatar;
    private String from;
    private String totalScore;
    private String lastLevel;
    private String lastScore;
    private Score[] scores;

    public Player() {
    }


    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAvatar() { return avatar; }
    public String getFrom() { return from; }
    public String getTotalScore() { return totalScore; }
    public String getLastLevel() { return lastLevel; }
    public String getLastScore() { return lastScore; }
    public Score[] getScores() { return scores; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setFrom(String from) { this.from = from; }
    public void setTotalScore(String totalScore) { this.totalScore = totalScore;}
    public void setLastLevel(String lastLevel) { this.lastLevel = lastLevel; }
    public void setLastScore(String lastScore) { this.lastScore = lastScore; }
    public void setScores(Score[] scores) { this.scores = scores; }




}
