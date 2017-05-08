package stt;

import java.io.File;
import java.util.List;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Customization;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Customization.Status;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechModel;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Word;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.WordData;

public class STTCustomizeUtil {

	String username;
	String password;
	SpeechToText service = new SpeechToText();

	String customizationId;

	public STTCustomizeUtil(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		service.setUsernameAndPassword(username, password);
	}

	/**
	 * @param word
	 *            �Ђ炪��
	 * @param displayAs
	 *            �\�L
	 * @param soundsLike
	 *            �J�^�J�i
	 */
	public void addWord(String word, String displayAs, String soundsLike) {
		// hiragana
		if (word.matches("^[��-�U]+$") == false) {
			System.err.println(word + " is not Hiragana");
			return;
		}

		// katakana
		if (soundsLike.matches("^[�@-��]+$") == false) {
			System.err.println(soundsLike + " is not Katakana");
			return;
		}

		service.addWord(this.customizationId, new Word(word, displayAs, soundsLike)).execute();
	}

	/**
	 * @param name
	 * @param modelname
	 *            "ja-JP_BroadbandModel" or "ja-JP_NarrowbandModel"
	 * @param description
	 * @return
	 */
	public String createID(String name, String modelname, String description) {
		// String name = "IEEE-permanent";
		// SpeechModel model = SpeechModel.JA_JP_BROADBANDMODEL;

		SpeechModel model = null;
		if ("ja-JP_BroadbandModel".equals(modelname)) {
			model = SpeechModel.JA_JP_BROADBANDMODEL;
		} //
		else if ("ja-JP_NarrowbandModel".equals(modelname)) {
			model = SpeechModel.JA_JP_NARROWBANDMODEL;
		}
		// String description = "My customization";
		Customization myCustomization = service.createCustomization(name, model, description).execute();

		String id = myCustomization.getId();

		System.out.println(id);

		this.customizationId = id;

		return id;
	}

	public void delete(String id) {
		service.deleteCustomization(customizationId).execute();
	}

	public String getCustomizationId() {
		return customizationId;
	}

	public void getStatus() {
		Status s = service.getCustomization(customizationId).execute().getStatus();
		System.err.println(s.toString());
	}

	public void getWords() {
		List<WordData> result = service.getWords(customizationId, Word.Type.ALL).execute();
		for (WordData word : result) {
			System.out.println(word);
		}
	}

	public void listIDs() {
		ServiceCall<List<Customization>> o = service.getCustomizations("ja-JP");
		List<Customization> list = o.execute();
		for (Customization c : list) {
			String msg = String.format("id:%s,desc:%s,status:%s", //
					c.getId(), //
					c.getDescription(), //
					c.getStatus());
			System.err.println(msg);
		}
	}

	public void recognize(File audio) {
		RecognizeOptions options = new RecognizeOptions.Builder().continuous(true) //
				.model(SpeechModel.JA_JP_BROADBANDMODEL.getName()) //
				.customizationId(customizationId) //
				.build();

		SpeechResults transcript = service.recognize(audio, options).execute();
		System.out.println(transcript);
	}

	public void recognizeTest(File audio) {
		System.err.println("without customize");
		{
			RecognizeOptions options = new RecognizeOptions.Builder().continuous(true) //
					.model(SpeechModel.JA_JP_BROADBANDMODEL.getName()) //
					.build();

			SpeechResults transcript = service.recognize(audio, options).execute();
			System.out.println(transcript);
		}
		System.err.println("with customize");
		{
			RecognizeOptions options = new RecognizeOptions.Builder().continuous(true) //
					.model(SpeechModel.JA_JP_BROADBANDMODEL.getName()) //
					.customizationId(customizationId) //
					.build();

			SpeechResults transcript = service.recognize(audio, options).execute();
			System.out.println(transcript);

		}

	}

	public void setCustomizationId(String customizationId) {
		this.customizationId = customizationId;
	}

	public void train() {
		service.trainCustomization(customizationId, Customization.WordTypeToAdd.ALL).execute();
	}

	public static void main(String[] args) throws Exception {

		String username = "811c6f02-9c1d-454c-ad2c-a73648724337";
		String password = "TitmHH6mX05p";
		STTCustomizeUtil util = new STTCustomizeUtil(username, password);

		util.listIDs();

		System.err.println(SpeechModel.JA_JP_BROADBANDMODEL.getName());
		System.err.println(SpeechModel.JA_JP_NARROWBANDMODEL.getName());

	}

}
