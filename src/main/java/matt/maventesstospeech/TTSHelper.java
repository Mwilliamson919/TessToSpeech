/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matt.maventesstospeech;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private static AdvancedPlayer currentAudio = null;
    private static AmazonPollyClient polly = null;
    
   public TTSHelper(){
       initService();
   }
    
    public void run(){
        try {
            currentAudio.play();
        } catch (JavaLayerException ex) {
            Logger.getLogger(TTSHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initService(){
        String accessKeyId = "AKIAYAWY5Q6NPPWHUDFO";
        String secretAccessKey = "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";

        // Initialize Amazon Polly client
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        polly = (AmazonPollyClient) AmazonPollyClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_1)
                .build();  
    }
    
    public static InputStream stringToInputStream(String tts){
        InputStream speechStream = synthesizeSpeech(polly, tts, OutputFormat.Mp3);
        return speechStream;
    }
    
    public static InputStream synthesizeSpeech(AmazonPolly pollyClient, String text, OutputFormat format) {
        SynthesizeSpeechRequest synthesizeSpeechRequest = new SynthesizeSpeechRequest()
                .withText(text)
                .withOutputFormat(format)
                .withVoiceId("Joanna"); // Specify the voice you want to use

        SynthesizeSpeechResult synthesizeSpeechResult = pollyClient.synthesizeSpeech(synthesizeSpeechRequest);
        return synthesizeSpeechResult.getAudioStream();
    }
    
    
    public static AdvancedPlayer convertToPlayer(InputStream audioStream) {
        try {
            AdvancedPlayer player = new AdvancedPlayer(audioStream);
            return player;
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void setCurrentAudio(String tts){
        currentAudio = convertToPlayer(stringToInputStream(tts));
    }
    
}


