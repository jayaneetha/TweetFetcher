/**
 * Created by Admin on 12/23/15.
 */
public class Status {
    long id;
    long tweet_id;
    String created_at;
    String tweet;
    long in_reply_to_status_id_str;
    long in_reply_to_user_id;
    String in_reply_to_screen_name;
    long user_id;
    String user_name;
    String user_screen_name;
    String user_location;
    String language;
    int retweet_count;
    int favorite_count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(long tweet_id) {
        this.tweet_id = tweet_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public long getIn_reply_to_status_id_str() {
        return in_reply_to_status_id_str;
    }

    public void setIn_reply_to_status_id_str(long in_reply_to_status_id_str) {
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
    }

    public long getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(long in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_screen_name() {
        return user_screen_name;
    }

    public void setUser_screen_name(String user_screen_name) {
        this.user_screen_name = user_screen_name;
    }

    public String getUser_location() {
        return user_location;
    }

    public void setUser_location(String user_location) {
        this.user_location = user_location;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public Status() {
    }

    public Status(long id, long tweet_id, String created_at, String tweet, long in_reply_to_status_id_str, long in_reply_to_user_id, String in_reply_to_screen_name, long user_id, String user_name, String user_screen_name, String user_location, String language, int retweet_count, int favorite_count) {
        this.id = id;
        this.tweet_id = tweet_id;
        this.created_at = created_at;
        this.tweet = tweet;
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
        this.in_reply_to_user_id = in_reply_to_user_id;
        this.in_reply_to_screen_name = in_reply_to_screen_name;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_screen_name = user_screen_name;
        this.user_location = user_location;
        this.language = language;
        this.retweet_count = retweet_count;
        this.favorite_count = favorite_count;
    }

}
