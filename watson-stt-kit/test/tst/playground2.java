package tst;

import java.io.File;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

import stt.WavSplitFixedTime;

public class playground2 {

	public static void main(String[] args) {
		File wavFile = new File("file/Mizuno_ja_1-48.wav");
		float slotSec = (float) 10;
		
		WavSplitFixedTime wsft = new WavSplitFixedTime(wavFile, slotSec);
		
		File dir = new File(wsft.getWavFileSplitDir());
		File[] wavSplits = dir.listFiles();
		for (File file : wavSplits){
			sttRecognize(file);
		}
		
	}
	
	private static void sttRecognize(File file) {
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
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

	}

}
