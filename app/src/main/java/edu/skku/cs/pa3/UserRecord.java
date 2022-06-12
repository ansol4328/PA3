package edu.skku.cs.pa3;

public class UserRecord implements Comparable<UserRecord>{
    private String name;
    private Integer sec, rank;

    public UserRecord(String name, Integer sec, Integer rank){
        this.name=name;
        this.sec=sec;
        this.rank=rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSec() {
        return sec;
    }

    public void setSec(Integer sec) {
        this.sec = sec;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public int compareTo(UserRecord userRecord) {
        if(sec<userRecord.sec) return -1;
        else if(sec>userRecord.sec) return 1;
        return name.compareTo(userRecord.name);
    }
}
