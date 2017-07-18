package stt;

import java.io.File;
import java.io.FileInputStream;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

public class SpeechToTextExampleKIT {

	public static void main(String[] args) throws Exception {

		String endPoint = "https://stream.watson-j.jp/speech-to-text/api";
		String username = "307e4ab4-a72c-443d-b876-64ff52ccca7b";
		String password = "q1SzIzepUogs";
		String wavFile = "file/input.wav";
		String model = "ja-JP_BroadbandModel";

		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword(username, password);

		RecognizeOptions recognizeOptions = new RecognizeOptions.Builder().contentType(HttpMediaType.AUDIO_WAV) //
				.continuous(true) //
				.interimResults(true) //
				.model(model) //
				.build();

		// File audio = new File(wavFile);
		// service.setEndPoint(endPoint);
		//
		// long time1 = System.currentTimeMillis();
		// SpeechResults transcript = service.recognize(audio, recognizeOptions)
		// .execute();
		// long time2 = System.currentTimeMillis();
		// System.err.println("time:" + (time2 - time1));
		//
		// // System.out.println(transcript);
		// // System.out.println();
		// for (Transcript t : transcript.getResults()) {
		// // System.out.println(t.toString());
		// for (SpeechAlternative a : t.getAlternatives()) {
		// System.out.println(a.getTranscript());
		// }
		// }

		FileInputStream audio = new FileInputStream(new File(wavFile));
		service.setEndPoint(endPoint);

		service.recognizeUsingWebSocket(audio, recognizeOptions, new BaseRecognizeCallback() {
			@Override
			public void onTranscription(SpeechResults speechResults) {
				System.out.println(speechResults);
			}
		});

		// wait 20 seconds for the asynchronous response
		Thread.sleep(20000);

	}
}
