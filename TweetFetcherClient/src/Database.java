/**
 * Created by Admin on 11/10/15.
 */

import twitter4j.Status;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static String DB_URL;

    //  Database credentials
    static String USER;
    static String PASS;

    Database() {
        try {
            String settingsJSON = new TweetFetcherClient().readFile("database.json");
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


    public void insert_status(Status status) {

        String queryString = "INSERT INTO `tweets` (`id`, `tweet_id`, `created_at`, `tweet`, `in_reply_to_status_id_str`, `in_reply_to_user_id_str`, `in_reply_to_screen_name`, `user_id`, `user_name`, `user_screen_name`, `user_location`, `language`, `retweet_count`, `favorite_count`) VALUES (NULL, " +
                status.getId() + ", " +
                "'" + status.getCreatedAt().toString() + "', " +
                "'" + status.getText() + "', " +
                status.getInReplyToStatusId() + ", " +
                status.getInReplyToUserId() + ", " +
                "'" + status.getInReplyToScreenName() + "', " +
                status.getUser().getId() + ", " +
                "'" + status.getUser().getName() + "', " +
                "'" + status.getUser().getScreenName() + "', " +
                "'" + status.getUser().getLocation() + "', " +
                "'" + status.getLang() + "', " +
                status.getRetweetCount() + ", " +
                status.getFavoriteCount() +
                ");";
        execute_sql(queryString);
    }


    public List<Long> get_in_reply_to_ids() {
        String query_sql = "SELECT `in_reply_to_status_id_str` FROM `tweets` WHERE `in_reply_to_status_id_str` != -1";
        Connection conn = null;
        Statement stmt = null;

        List<Long> longArrayList = new ArrayList<Long>();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query_sql);

            while (resultSet.next()) {
                long status_id = resultSet.getLong("in_reply_to_status_id_str");
                longArrayList.add(status_id);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return longArrayList;


    }

    public void execute_sql(String sql) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            stmt.execute(sql);

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
    }


}
