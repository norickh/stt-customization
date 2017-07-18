package stt;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechModel;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Customization;

public class CLMCreate {
	private String customId;
	private String status = "";

	public CLMCreate(String username, String password, String endpoint, String name,
			String description, String speechModel) {

		SpeechModel baseModel = new SpeechModel(speechModel);
		System.out.println(baseModel);
		
		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword(username, password);
		service.setEndPoint(endpoint); 
		

		// Create customization
		try {
			Customization model = service.createCustomization(name, baseModel, description).execute();
			this.customId = model.getId();
			this.status = "辞書が新たに作成されました。リストアップして確認してください。\n";
			System.out.println(model);
		} catch (Exception e) {
			this.status = e.getLocalizedMessage() +"\n辞書を新たに作成できませんでした。endpoint, username, passwordが正しいものか、またはnameが入力されているか確認してください。\n";
			e.printStackTrace();
		}

	}

	public String getCustomizationId() {
		return customId;
	}

	public String getStatus() {
		return status;
	}
}
