package tst;

import java.util.List;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Word;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.WordData;


public class playground3 {
	private static SpeechToText service;
	
	public static void main(String[] args) {
		String endpoint = "https://stream.watsonplatform.net/speech-to-text/api";
		String username = "80c55ecb-9c6e-40d9-94a2-094d705fecc3";
		String password = "QbVVGDfTk3bW";
		String id = "5a14c660-0bd5-11e7-85ff-a5ff8b892e09";
		
		service = new SpeechToText();
		service.setUsernameAndPassword(username, password);
		service.setEndPoint(endpoint);
		getWords(id);
		
	}
	
	public static void getWords(String id) {
		try {
			List<WordData> result = service.getWords(id, Word.Type.ALL).execute();
			System.out.println("現在 " + result.size() + " 個の単語が登録されています。\n");
			for (WordData word : result) {
				System.out.println(word.getWord() + "," + word.getSoundsLike() + "," + word.getDisplayAs()+"  /  "+word.getError()+", "+word.getSource());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
