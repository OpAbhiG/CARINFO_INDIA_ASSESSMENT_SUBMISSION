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
        try {
            // Send GET request to the API
            URL url = new URL("https://api.cuvora.com/car/partner/cricket-data");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apiKey", "test-creds@2320");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonArray matchesArray = jsonResponse.getAsJsonArray("data");

                // Initialize variables for computations
                int highestScore = 0;
                String teamWithHighestScore = "";
                int matchesWith300PlusScore = 0;

                for (JsonElement matchElement : matchesArray) {
                    JsonObject matchObject = matchElement.getAsJsonObject();

                    // Retrieve scores and team names
                    String team1ScoreStr = matchObject.get("t1s").getAsString().split("/")[0];
                    String team2ScoreStr = matchObject.get("t2s").getAsString().split("/")[0];
                    int team1Score = Integer.parseInt(team1ScoreStr);
                    int team2Score = Integer.parseInt(team2ScoreStr);
                    String team1Name = matchObject.get("t1").getAsString();
                    String team2Name = matchObject.get("t2").getAsString();

                    // Update highest score and team
                    if (team1Score > highestScore) {
                        highestScore = team1Score;
                        teamWithHighestScore = team1Name;
                    }

                    if (team2Score > highestScore) {
                        highestScore = team2Score;
                        teamWithHighestScore = team2Name;
                    }

                    // Count matches with a total score of 300 or more
                    if (team1Score + team2Score >= 300) {
                        matchesWith300PlusScore++;
                    }
                }

                // Print the results
                System.out.println("API Response: " + response.toString());

                System.out.println("Highest Score: " + highestScore + " by " + teamWithHighestScore);
                System.out.println("Number of Matches with total 300 Plus Score: " + matchesWith300PlusScore);

            } else {
                System.out.println("Error: Failed to fetch data from the API. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
