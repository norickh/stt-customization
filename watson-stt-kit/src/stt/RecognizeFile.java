package stt;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;

public class RecognizeFile {
	private SpeechToText service;
	private String status = null;


	public RecognizeFile(String username, String password, String endpoint) {
			try {
				service = new SpeechToText();
				service.setUsernameAndPassword(username, password);
				service.setEndPoint(endpoint);
			} catch (Exception e) {
				this.status = e.getLocalizedMessage() + "接続できませんでした。endpoint, username, passwordが正しいものか確認してください。\n";
				e.printStackTrace();
			}
	}

}
