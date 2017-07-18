package stt;

import java.io.File;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

public class WavSplitRecognizeSnipet {
	String wavFileName;
	SpeechToText service;

	public WavSplitRecognizeSnipet() {
		WavSplitFixedTime wsft = new stt.WavSplitFixedTime(new File(wavFileName), 10);				
		File dir = new File(wsft.getWavFileSplitDir());
		File[] wavSplits = dir.listFiles();
		
	/*	for (File file : wavSplits){
			SpeechResults transcript = service.recognize(file, options).execute();
			String tmp = new TranscriptToString().setTranscript(transcript);			
			textAreaRecognized.setText(textAreaRecognized.getText() + tmp);
			System.out.println(textAreaRecognized.getText()+tmp);
		}*/
	}

}
