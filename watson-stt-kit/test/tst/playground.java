/**
 * @author norickh
 *
 */
package tst;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

public class playground {

	String fileName = "";
	static AudioInputStream audioIn = null;
	static AudioFormat format = null;
	static long nBytesRead = 0;
	static long frameSize;
	static long frameLength;
	static byte[] buf;
	static SourceDataLine line = null;
	static DataLine.Info info;

	public static void readWavFile(File file) {
		try {
			if (!file.exists()) {
				return;
			}

			FileInputStream fis = new FileInputStream(file);
			InputStream fileIn = new BufferedInputStream(fis);
			audioIn = AudioSystem.getAudioInputStream(fileIn);
			format = audioIn.getFormat();
			buf = new byte[audioIn.available()];
			System.out.println(audioIn.available() + "  " + buf.length + " "+ format.getFrameRate() );
			audioIn.read(buf, 0, buf.length); //バイト配列にAudioInputStreamを格納
			frameSize = format.getFrameSize();
			frameLength = audioIn.getFrameLength();
			nBytesRead = frameSize * frameLength;
			System.out.println(
					"Frame Size: " + frameSize + "\nFrame Length: " + frameLength + "\nnBytesRead: " + nBytesRead);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void lineOutAudio() {
		try {
			// Construct a DataLine.Info object from the format.
			info = new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine) AudioSystem.getLine(info);
			// Open and start the line.
			line.open(format);
			line.start();
			
			line.write(buf, 0, 32000); // Write the data out to the line.
			try {
				Thread.sleep(3000);
				System.out.println("waiting.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
	/*		line.write(buf, 32000, 32000);
			try {
				Thread.sleep(3000);
				System.out.println("waiting.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	*/		
			line.drain();
			System.out.println("End of line out.");			
		}
		catch (LineUnavailableException e) {	e.printStackTrace();	} 
		finally {	line.close();		}
	}
	
	public static void writeAudio(File tempFile) {
		try {
			InputStream byteIn = new BufferedInputStream( new ByteArrayInputStream(buf, 128000, 220000) );
			AudioInputStream ais = new AudioInputStream(byteIn, format,  byteIn.available());
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, tempFile);
			System.out.println("Write arbital byte length of audio");
		}
		catch (Exception e) {	e.printStackTrace(); } 
	}
	
	public static boolean sttRecognize(File file) {
		try {
			SpeechToText service = new SpeechToText();
			String endPoint = "https://stream.watson-j.jp/speech-to-text/api";
			String username = "307e4ab4-a72c-443d-b876-64ff52ccca7b";
			String password = "q1SzIzepUogs";
			service.setUsernameAndPassword(username, password);
			service.setEndPoint(endPoint);
			RecognizeOptions options = new RecognizeOptions.Builder().contentType(HttpMediaType.AUDIO_WAV) //
					.continuous(true) //
					.interimResults(true) //
					.model("ja-JP_BroadbandModel") //
					.timestamps(true) //
					.customizationId("206d6e60-e8eb-11e6-8814-7160efe43409") //
					.build();

			SpeechResults transcript = service.recognize(file, options).execute();
			System.out.println(transcript);
			return true;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return false;
		}

	}

	
	public static void main(String[] args) {

		String wavFileName = "file/inputSKR-6.wav";
		File wavFile = new File(wavFileName);
		if (wavFile.isFile() == false || wavFileName.endsWith("wav") == false) {
			System.err.println("break");
			return;
		}
		File tempFile = new File("file/temp.wav");

		// Read wav format file.
		readWavFile(wavFile);
		
//		lineOutAudio();
				
		// 指定された長さのByteArrayをFileに書き込む.STTに渡すのはFileなので
		writeAudio(tempFile); 
		
		System.out.println(wavFile.getParent() + "\t"+ wavFile.getPath());
		
//		sttRecognize(tempFile);
		

		String wavFilePathPre = getPreffix(wavFile.getPath());
		File newfile = new File(wavFilePathPre);
		newfile.mkdir();
		
		for (int i = 0; i < 22; i++) {
			String index = String.format("%03d", (i+1));
			String wavSplitPath = getPreffix(wavFile.getAbsolutePath()) + "/split"+ index + ".wav";
			System.out.println(wavSplitPath);
		}

		
		System.exit(0);
	}
	
	public static String getPreffix(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(0, point);
	    } 
	    return fileName;
	}

}