/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matt.maventesstospeech;

import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoadLibs;

/**
 *
 * @author mwill
 */
public class TessHelper {
    static Tesseract instance = new Tesseract();
    
    
    public TessHelper(){
        File tmpFolder = LoadLibs.extractTessResources("win64");
        System.setProperty("java.library.path", tmpFolder.getPath()); 
        instance.setLanguage("eng");
        instance.setOcrEngineMode(1);
        System.setProperty("TESSDATA_PREFIX", "");
    }
}
