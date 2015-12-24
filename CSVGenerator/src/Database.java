/**
 * Created by Admin on 11/10/15.
 */

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static String DB_URL;
//    = "jdbc:mysql://127.0.0.1:3306/myResearch";

    //  Database credentials
    static String USER;
    static String PASS;

    Database() {
        try {
            String settingsJSON = new CSVGenerator().readFile("database.json");
            org.json.JSONObject settingsJSONobject = new org.json.JSONObject(settingsJSON);

            if (!settingsJSONobject.isNull("db_url")) {
                DB_URL = settingsJSONobject.getString("db_url");
            }
            if (!settingsJSONobject.isNull("user")) {
                USER = settingsJSONobject.getString("user");
            }
            if (!settingsJSONobject.isNull("pass")) {
                PASS = settingsJSONobject.getString("pass");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("database.json file not found");
            System.exit(0);
        }
    }

    public List<Status> get_all_tweets() {
        String query_sql = "SELECT * FROM `tweets`";

        Connection conn = null;
        Statement stmt = null;

        List<Status> statuses = new ArrayList<Status>();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query_sql);

            while (resultSet.next()) {
                Status status = new Status();

                status.setId(resultSet.getLong("id"));
                status.setTweet_id(resultSet.getLong("tweet_id"));
                status.setCreated_at(resultSet.getString("created_at"));
                status.setTweet(resultSet.getString("tweet"));
                status.setIn_reply_to_status_id_str(resultSet.getLong("in_reply_to_status_id_str"));
                status.setIn_reply_to_user_id(resultSet.getLong("in_reply_to_user_id_str"));
                status.setIn_reply_to_screen_name(resultSet.getString("in_reply_to_screen_name"));
                status.setUser_id(resultSet.getLong("user_id"));
                status.setUser_name(resultSet.getString("user_name"));
                status.setUser_screen_name(resultSet.getString("user_screen_name"));
                status.setUser_location(resultSet.getString("user_location"));
                status.setLanguage(resultSet.getString("language"));
                status.setRetweet_count(resultSet.getInt("retweet_count"));
                status.setFavorite_count(resultSet.getInt("favorite_count"));

                statuses.add(status);

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statuses;

    }
}
