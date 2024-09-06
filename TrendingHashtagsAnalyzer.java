import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrendingHashtagsAnalyzer {
    public static void main(String[] args) {
        String datasetFilePath = "D:\\database.txt"; 

        List<String> instagramData = readSection(datasetFilePath, "Instagram");
        Map<String, Integer> instagramHashtagCount = analyzeHashtags(instagramData);

        List<String> twitterData = readSection(datasetFilePath, "Twitter");
        Map<String, Integer> twitterHashtagCount = analyzeHashtags(twitterData);

        List<String> youtubeData = readSection(datasetFilePath, "YouTube");
        Map<String, Integer> youtubeHashtagCount = analyzeHashtags(youtubeData);

        displayTrendingHashtags(instagramHashtagCount, twitterHashtagCount, youtubeHashtagCount, 5);
    }

    public static List<String> readSection(String filePath, String sectionName) {
        List<String> lines = new ArrayList<>();
        boolean isInTargetSection = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("# " + sectionName)) {
                    isInTargetSection = true;
                } else if (isInTargetSection && line.trim().startsWith("# ")) {
                    // Reached another section
                    break;
                } else if (isInTargetSection && !line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return lines;
    }

    public static Map<String, Integer> analyzeHashtags(List<String> dataList) {
        Map<String, Integer> hashtagCount = new HashMap<>();
        Pattern pattern = Pattern.compile("#\\w+");

        for (String text : dataList) {
            Matcher matcher = pattern.matcher(text.toLowerCase());

            while (matcher.find()) {
                String hashtag = matcher.group();
                hashtagCount.put(hashtag, hashtagCount.getOrDefault(hashtag, 0) + 1);
            }
        }

        return hashtagCount;
    }

    public static void displayTrendingHashtags(Map<String, Integer> instagramHashtags, Map<String, Integer> twitterHashtags, Map<String, Integer> youtubeHashtags, int limit) {
        System.out.println("Instagram:");
        displayPlatformHashtags(instagramHashtags, limit);
        System.out.println("Twitter:");
        displayPlatformHashtags(twitterHashtags, limit);
        System.out.println("YouTube:");
        displayPlatformHashtags(youtubeHashtags, limit);
    }

    private static void displayPlatformHashtags(Map<String, Integer> hashtagCount, int limit) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(hashtagCount.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        int counter = 0;
        for (Map.Entry<String, Integer> entry : list) {
            if (counter >= limit) {
                break;
            }
            System.out.println(entry.getKey() + ": " + entry.getValue());
            counter++;
        }
        System.out.println();
    }
}
