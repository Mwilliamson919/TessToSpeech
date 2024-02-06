/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matt.maventesstospeech;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

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
    
    
    public static AdvancedPlayer getCurrentAudioFromResponse(Response response) throws IOException, JavaLayerException{
        ResponseBody body = response.body();
        byte[] audioData = body.bytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(audioData);
        AdvancedPlayer player = new AdvancedPlayer(inputStream);
        return player;
    }
    
    public static String buildUrl(String textToSpeech){
        String url = String.format("https://voicerss-text-to-speech.p.rapidapi.com/?hl=en-us&src=%s&key=a2d4316d19e047a186c6515ec203f85e&f=8khz_8bit_mono&c=mp3&r=0", textToSpeech);
        return url;
    }
    
    public static Response buildAndSend(String textToSpeech) throws IOException{
        String url = buildUrl(textToSpeech);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "77f5b5ea09mshe5d2f2dfbc3fc0ep1b4ffajsn5661442c909b")
                .addHeader("X-RapidAPI-Host", "voicerss-text-to-speech.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response);
        return response;
    }
}


