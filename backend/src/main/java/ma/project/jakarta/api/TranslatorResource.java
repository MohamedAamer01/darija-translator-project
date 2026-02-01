package ma.project.jakarta.api;

import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import org.json.JSONArray;

@Path("/translator")
public class TranslatorResource {

    private final String GEMINI_API_KEY = "VOTRE_CLE_API_ICI";
    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + GEMINI_API_KEY;
    
    //  MODE SIMULATION - Mettez à true pour tester sans Gemini
    private final boolean SIMULATION_MODE = false ;

    @POST
    @Path("/translate")
    @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Response translate(String jsonInput) {
        try {
            JSONObject inputBody = new JSONObject(jsonInput);
            String englishText = inputBody.getString("text");

            //  MODE SIMULATION
            if (SIMULATION_MODE) {
                String simulatedTranslation = simulateDarijaTranslation(englishText);
                JSONObject result = new JSONObject()
                        .put("original", englishText)
                        .put("translation", simulatedTranslation)
                        .put("mode", "simulation");
                return Response.ok(result.toString()).build();
            }

            // MODE REEL AVEC GEMINI
            String prompt = "Translate the following English text to Moroccan Darija (Arabic dialect). " +
                           "Provide only the translation: " + englishText;
            
            JSONObject textPart = new JSONObject().put("text", prompt);
            JSONArray partsArray = new JSONArray().put(textPart);
            JSONObject contentObj = new JSONObject().put("parts", partsArray);
            JSONArray contentsArray = new JSONArray().put(contentObj);
            JSONObject payload = new JSONObject().put("contents", contentsArray);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return Response.status(response.statusCode())
                        .entity(response.body())
                        .build();
            }

            JSONObject responseJson = new JSONObject(response.body());
            String translatedText = responseJson
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            JSONObject result = new JSONObject()
                    .put("original", englishText)
                    .put("translation", translatedText)
                    .put("mode", "gemini");

            return Response.ok(result.toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // Fonction de simulation simple
    private String simulateDarijaTranslation(String englishText) {
    // Quelques traductions simulées pour les tests
        switch (englishText.toLowerCase()) {
            case "hello":
                return "سلام (salam)";
            case "good morning":
                return "صباح الخير (sbah lkhir)";
            case "how are you":
                return "كيداير؟ (kidayer?)";
            case "thank you":
                return "شكرا (shukran)";
            case "goodbye":
                return "بسلامة (bslama)";
            default:
                return "[SIMULATION] Traduction darija de: " + englishText;
        }
    }
}