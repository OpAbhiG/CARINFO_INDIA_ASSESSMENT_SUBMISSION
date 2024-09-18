import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CricketMatchesAssignment {
    public static void main(String[] args) {
        String apiUrl = "https://api.cuvora.com/car/partner/cricket-data";
        String apiKey = "test-creds@2320";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apiKey", apiKey);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                    JsonArray matchesArray = jsonResponse.getAsJsonArray("data");

                    int highestScore = 0;
                    String teamWithHighestScore = "";
                    int matchesWith300PlusScore = 0;

                    for (JsonElement matchElement : matchesArray) {
                        JsonObject matchObject = matchElement.getAsJsonObject();

                        int team1Score = Integer.parseInt(matchObject.get("t1s").getAsString().split("/")[0]);
                        int team2Score = Integer.parseInt(matchObject.get("t2s").getAsString().split("/")[0]);
                        String team1Name = matchObject.get("t1").getAsString();
                        String team2Name = matchObject.get("t2").getAsString();

                        if (team1Score > highestScore) {
                            highestScore = team1Score;
                            teamWithHighestScore = team1Name;
                        }

                        if (team2Score > highestScore) {
                            highestScore = team2Score;
                            teamWithHighestScore = team2Name;
                        }

                        if (team1Score + team2Score >= 300) {
                            matchesWith300PlusScore++;
                        }
                    }

                    // System.out.println("API Response: " + response.toString());

                    System.out.println("Highest Score: " + highestScore + " by " + teamWithHighestScore);
                    System.out.println("Number of Matches with total 300 Plus Score: " + matchesWith300PlusScore);
                }
            } else {
                System.out.println("Error: Failed to fetch data from the API. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
