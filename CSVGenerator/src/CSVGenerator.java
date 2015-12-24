import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayaneetha on 12/25/15.
 */
public class CSVGenerator {

    public static List<Status> statuses;


    public static void main(String[] args) {

        Database database = new Database();
        String LINE_SEPERATOR = "\n";
        String DELEMETER = ",";
        FileWriter fileWriter = null;

        statuses = database.get_all_tweets();
        List<Long> tweetIds = new ArrayList<Long>();

        for(Status status: statuses){
            tweetIds.add(status.getTweet_id());
        }

        List<String> columnNames = new ArrayList<String>();

        for (Long tweetId : tweetIds) {
            columnNames.add(tweetId.toString());
        }

        try {
            fileWriter = new FileWriter("export.csv");

            fileWriter.append("TweetIDs,");
            for (String s : columnNames) {
                fileWriter.append(s + ",");
            }
            fileWriter.append(LINE_SEPERATOR);

            int iteration_count = 1;
            for (Long tweetId : tweetIds) {
                fileWriter.append(tweetId.toString());
                fileWriter.append(DELEMETER);

                for (Long t : tweetIds) {
                    if (new CSVGenerator().is_a_reply(tweetId, t)) {
                        fileWriter.append("T");
                        fileWriter.append(DELEMETER);

                    } else {
                        fileWriter.append("F");
                        fileWriter.append(DELEMETER);

                    }
                }

                System.out.println(Float.valueOf((Float.valueOf(iteration_count) / Float.valueOf(statuses.size())) * 100) + "%");

                fileWriter.append(LINE_SEPERATOR);
            }
            System.out.println("CSV file was created successfully !!!");

        } catch (IOException e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }

    private boolean is_a_reply(long tweet_id, long t) {
        Status status = get_tweet(tweet_id);
        if (status != null) {
            if (status.getIn_reply_to_status_id_str() == t) {
                return true;
            }
        }
        return false;
    }

    private Status get_tweet(long tweet_id) {
        for (Status status : statuses) {
            if (status.getId() == tweet_id) {
                return status;
            }
        }
        return null;
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

}
