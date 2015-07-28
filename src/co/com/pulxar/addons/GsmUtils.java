package co.com.pulxar.addons;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.tritonus.sampled.convert.gsm.GSMFormatConversionProvider;

public class GsmUtils {
	
	private LoadProperties properties;
	private String archivoEntrada;
	private String directorioEntrada;
	
	public GsmUtils(String archivoEntrada,String dierctorios) {
		this.archivoEntrada = archivoEntrada;
		this.directorioEntrada = dierctorios;
		properties = new LoadProperties();
	}

	public String obtenerNombreArchivoEntrada(){
        String respuesta = properties.getAsteriskdirgsm();
        respuesta += directorioEntrada;
        respuesta += archivoEntrada; 
        return respuesta;
    }
	
	public String obtenerNombreArchivoSalida(){
        String respuesta = properties.getAsteriskdirwav();
        respuesta += obtenerNombreWav();
        return respuesta;
    }
	
	public String obtenerNombreWav() {
		String respuesta = archivoEntrada.replace("gsm","wav");
		return respuesta;
	}
	
    public void convetirGsmWav(){
        AudioInputStream audioInputStream = null;
        AudioInputStream audioOutputStream = null;
        try {
            File fileIn = new File(obtenerNombreArchivoEntrada());
            File fileOut = new File(obtenerNombreArchivoSalida());
            audioInputStream = AudioSystem.getAudioInputStream(fileIn);
            audioOutputStream = getConvertedStream(audioInputStream);
            AudioSystem.write(audioOutputStream, AudioFileFormat.Type.WAVE, fileOut);
        }catch(Exception ex){
        	Logger.getLogger(GsmUtils.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try{
                audioInputStream.close();
                audioOutputStream.close();
            }catch (Exception ex){
            	Logger.getLogger(GsmUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private AudioInputStream getConvertedStream(AudioInputStream sourceStream)throws Exception {
        
        AudioFormat sourceFormat = sourceStream.getFormat();
        System.out.println("Input format: " + sourceFormat);
        AudioInputStream targetStream = null;
        AudioFormat intermediateFormat = new  AudioFormat(AudioFormat.Encoding.PCM_SIGNED,8000.0f,16,1,2,44100.0f,true);
        targetStream = new GSMFormatConversionProvider.DecodedGSMAudioInputStream(intermediateFormat, sourceStream); 
        if (targetStream == null) {
           throw new Exception("conversion not supported");
        }
        System.out.println("Output format: " + targetStream.getFormat());
        return targetStream;
    }
    
    public void obtenerDuracionAudio(File inFile){
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(inFile);
            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = inFile.length();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            float durationInSeconds = audioFileLength / (frameSize * frameRate);
            System.out.print(durationInSeconds);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                audioInputStream.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public void borrarWav(){
        try {
            String result;
            String command= "ls "+properties.getAsteriskdirwav();
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader listWav = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((result = listWav.readLine()) != null) {
                if(result.contains(".wav")){
                    File fileBorrar = new File(properties.getAsteriskdirwav()+result);
                    fileBorrar.delete();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GsmUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
