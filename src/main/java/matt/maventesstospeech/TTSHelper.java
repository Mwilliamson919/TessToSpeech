/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matt.maventesstospeech;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.json.JSONObject;

/**
 *
 * @author mwill
 */
public class TTSHelper implements Runnable {
    static OkHttpClient client = new OkHttpClient();
    private AdvancedPlayer currentAudio = null;
    
    
   
    
    public void run(){
        try {
            currentAudio.play();
        } catch (JavaLayerException ex) {
            Logger.getLogger(TTSHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public AdvancedPlayer setCurrentAudioFromString(String text) throws IOException, JavaLayerException{
        Response response = buildAndSend(text);
        currentAudio = getCurrentAudioFromResponse(response);
        return currentAudio;
    }
    
    public static AdvancedPlayer createPlayer(String urlT) throws MalformedURLException, IOException, JavaLayerException{
        URL url = new URL(urlT);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        AdvancedPlayer player = new AdvancedPlayer(bis);
        return player;
    }
    
    public static AdvancedPlayer getCurrentAudioFromResponse(Response response) throws IOException, JavaLayerException{
        String jsonString = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject resultObject = jsonObject.getJSONObject("result");
        String audioUrl = resultObject.getString("audio_url");
        return createPlayer(audioUrl);
    }
    
    public static String buildUrl(String textToSpeech){
        String url = String.format("voice_code=en-KE-1&text=%s&speed=1.00&pitch=1.00&output_type=audio_url", textToSpeech);
        return url;
    }
    
    public static Response buildAndSend(String textToSpeech) throws IOException{
        String url = buildUrl(textToSpeech);
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, url);
        Request request = new Request.Builder()
                .url("https://cloudlabs-text-to-speech.p.rapidapi.com/synthesize")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("X-RapidAPI-Key", "77f5b5ea09mshe5d2f2dfbc3fc0ep1b4ffajsn5661442c909b")
                .addHeader("X-RapidAPI-Host", "cloudlabs-text-to-speech.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }
}


