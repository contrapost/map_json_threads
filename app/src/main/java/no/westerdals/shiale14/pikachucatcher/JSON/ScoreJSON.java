package no.westerdals.shiale14.pikachucatcher.JSON;

/**
 * Created by Alexander on 28.05.2016.
 *
 */
@SuppressWarnings("unused")
public class ScoreJSON {

    private String username;
    private int score;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return username + " has " + score + " score(s)";
    }
}
