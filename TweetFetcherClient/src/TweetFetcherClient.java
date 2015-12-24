import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by jayaneetha on 12/25/15.
 */
public class TweetFetcherClient {
    static Database database;
    static ConfigurationBuilder cb;
    static TwitterFactory tf;
    static Twitter twitter;

    static String HASHTAG;
    static String STATUS_FILE;
    static String OAuthAccessToken;
    static String OAuthAccessTokenSecret;
    static String OAuthConsumerKey;
    static String OAuthConsumerSecret;
    static int COUNT;
    static int RATE_LIMIT;
    static int ITERATIONS;

    public static void main(String[] args) {

        /*Load the settings from the settings.json
        Example of settings.json
        {"hashtag":"#love","count":100,"rate_limit":180, "status_file":"queryStatus.json","OAuthAccessToken":"218770908-31E3ZSChUcBuRfGlxPXgZP4xbuphdyUjSeAKcnQF","OAuthAccessTokenSecret":"eCuHd9YXSY3qoaKTGVN2mMpSgSnR921Y6EcaFrTHAfdMF","OAuthConsumerKey":"uX3fwNVbaQNdC3g7uPMZFN4gQ","OAuthConsumerSecret":"rSudqxCxIoq6db0zM1sHqQ5K33zxIUSvjOBlWNGiOyN9W5OB7l"}
         */
        try {
            String settingsJSON = new TweetFetcherClient().readFile("settings.json");
            org.json.JSONObject settingsJSONobject = new org.json.JSONObject(settingsJSON);

            if (!settingsJSONobject.isNull("hashtag")) {
                HASHTAG = settingsJSONobject.getString("hashtag");
            }
            if (!settingsJSONobject.isNull("OAuthAccessToken")) {
                OAuthAccessToken = settingsJSONobject.getString("OAuthAccessToken");
            }
            if (!settingsJSONobject.isNull("OAuthAccessTokenSecret")) {
                OAuthAccessTokenSecret = settingsJSONobject.getString("OAuthAccessTokenSecret");
            }
            if (!settingsJSONobject.isNull("OAuthConsumerKey")) {
                OAuthConsumerKey = settingsJSONobject.getString("OAuthConsumerKey");
            }
            if (!settingsJSONobject.isNull("OAuthConsumerSecret")) {
                OAuthConsumerSecret = settingsJSONobject.getString("OAuthConsumerSecret");
            }
            if (!settingsJSONobject.isNull("count")) {
                COUNT = settingsJSONobject.getInt("count");
            }
            if (!settingsJSONobject.isNull("rate_limit")) {
                RATE_LIMIT = settingsJSONobject.getInt("rate_limit");
            }
            if (!settingsJSONobject.isNull("iterations")) {
                ITERATIONS = settingsJSONobject.getInt("iterations");
            }
            if (!settingsJSONobject.isNull("status_file")) {
                STATUS_FILE = settingsJSONobject.getString("status_file");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Settings file not found");
            System.exit(0);
        }

        database = new Database();

        //Configuration builder is an utility class helps to initiate OAuth Credentials
        cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthAccessToken(OAuthAccessToken)
                .setOAuthAccessTokenSecret(OAuthAccessTokenSecret)
                .setOAuthConsumerKey(OAuthConsumerKey)
                .setOAuthConsumerSecret(OAuthConsumerSecret);
        tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();


        switch (Integer.valueOf(args[0])) {
            case 1:
                new TweetFetcherClient().search();
                break;
            case 2:
                new TweetFetcherClient().acquire();
                break;
            default:
                System.out.println("Invalid Parameter");
                System.exit(0);
        }


    }

    /**
     * Search for the tweets with the HASHTAG in Twitter
     */
    public void search() {
        for (int j = 0; j < ITERATIONS; j++) {
            int i = 0;
            do {

                Query query = new Query();
                QueryResult result = null;

                //Read the last saved query parameters from the queryStatus file.
                //For the first time, Query object initiate with the HASHTAG and COUNT parameters from the settings.json
                try {
                    String queryJSON = new TweetFetcherClient().readFile(STATUS_FILE);
                    org.json.JSONObject q = new org.json.JSONObject(queryJSON);

                    if (!q.isNull("query")) {
                        query.setQuery(q.getString("query"));
                    }
                    if (!q.isNull("lang")) {
                        query.setLang(q.getString("lang"));
                    }
                    if (!q.isNull("locale")) {
                        query.setLocale(q.getString("locale"));
                    }
                    if (!q.isNull("maxId")) {
                        query.setMaxId(q.getLong("maxId"));
                    }
                    if (!q.isNull("count")) {
                        query.setCount(q.getInt("count"));
                    }
                    if (!q.isNull("since")) {
                        query.setSince(q.getString("since"));
                    }
                    if (!q.isNull("sinceId")) {
                        query.setSinceId(q.getLong("sinceId"));
                    }
                    if (!q.isNull("until")) {
                        query.setUntil(q.getString("until"));
                    }

                } catch (FileNotFoundException e) {
                    query.setQuery(HASHTAG);
                    query.setCount(COUNT);
                }

                try {
                    //Request from TwitterAPI
                    System.out.println("Result requested");

                    result = twitter.search(query);

                    System.out.println("Result received");

                } catch (TwitterException e) {
                    e.printStackTrace();
                }


                if (result != null) {

                    long last_tweet_id = -1;

                    //Insert the statues in to the database
                    for (Status status : result.getTweets()) {
                        database.insert_status(status);
                        last_tweet_id = status.getId();
                    }

                    //Set the new maxID in the query object.
                    query.setMaxId(last_tweet_id);

                    // new JSONObject to store the queryStatus
                    org.json.JSONObject jsonObject = new org.json.JSONObject();

                    try {
                        jsonObject.put("query", query.getQuery());
                        jsonObject.put("lang", query.getLang());
                        jsonObject.put("locale", query.getLocale());
                        jsonObject.put("maxId", query.getMaxId());
                        jsonObject.put("count", query.getCount());
                        jsonObject.put("since", query.getSince());
                        jsonObject.put("sinceId", query.getSinceId());
                        jsonObject.put("until", query.getUntil());

                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }

                    // Write the query status to the file.
                    new TweetFetcherClient().writeFile(jsonObject.toString(), STATUS_FILE);

                }
                i++;

            } while (i <= RATE_LIMIT);

            // Rate limit exceeded.

            System.out.println("Sleeping ...");
            try {
                //Sleeping for 16 Minutes
                TimeUnit.MINUTES.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Acquire the tweets
     */
    public void acquire(){
        List<Long> longList = database.get_in_reply_to_ids();

        int count = 0;
        for (Long aLongList : longList) {

            if (count < RATE_LIMIT) {

                long tweetID = aLongList;
                try {
                    System.out.println("Requested");
                    Status status = twitter.showStatus(tweetID);
                    database.insert_status(status);
                    System.out.println("Stored : " + status.getText());
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                count++;

            } else {
                //Rate limit Exceeded
                try {
                    //Sleeping for 16 Minutes
                    TimeUnit.MINUTES.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = 0;
            }

        }
    }


    String readFile(String fileName) throws FileNotFoundException {

        // This will reference one line at a time
        String line = null;
        StringBuffer stringBuffer = new StringBuffer();


        // FileReader reads text files in the default encoding.
        FileReader fileReader
                = new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader
                = new BufferedReader(fileReader);

        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

    void writeFile(String text, String fileName) {

        try {
            // Assume default encoding.
            FileWriter fileWriter
                    = new FileWriter(fileName, false);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter
                    = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write(text);

            // Always close files.
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println(
                    "Error writing to file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }
}
