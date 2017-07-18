package stt;

import java.io.File;
import java.util.ArrayList;
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

public class CLMList {

	String username;
	String password;
	String endpoint;
	SpeechToText service = new SpeechToText();
	String customizationId;
	String status = "";
	String language;

	public CLMList(String username, String password, String endpoint, String model) {
		super();
		this.username = username;
		this.password = password;
		this.endpoint = endpoint;
		service.setUsernameAndPassword(username, password);
		service.setEndPoint(endpoint);
		if(model == "ja-JP_BroadbandModel" || model == "ja-JP_NarrowbandModel"){
			this.language = "ja-JP";
		}else{
			this.language = "en-US";
		}
	}
	
	public List<String> listCLMs(){
		List<String> idList = new ArrayList<String>();
		try {
			List<Customization> E = service.getCustomizations(language).execute();
			 for (Customization clm : E){
				 idList.add("Name: " + clm.getName() + " ,  ID: " + clm.getId());
			 }
			 this.status = "辞書をリストアップしました。\n";
			 return idList;
		} catch (Exception e) {
			this.status = e.getLocalizedMessage() +"\n辞書をリストアップできませんでした。endpoint, username, passwordが正しいものか確認してください。\n";
			e.printStackTrace();
			 return idList;
		}
	}
	
	public boolean deleteCLMs(String id){
		try {
			service.deleteCustomization(id).execute();
			this.status = "選択された辞書を削除しました。\n";
			return true;
		} catch (Exception e) {
			this.status = e.getLocalizedMessage() +"\n選択された辞書を削除できませんでした。endpoint, username, passwordが正しいものか確認してください。\n";
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteAllCLMs(){
		 try {
			List<Customization> E = service.getCustomizations(language).execute();
			 for (Customization clm : E){
				 service.deleteCustomization(clm.getId()).execute();
			 }
				this.status = "辞書を全て削除しました。\n";
				return true;
		} catch (Exception e) {
			this.status = e.getLocalizedMessage() +"\n辞書を全て削除できませんでした。endpoint, username, passwordが正しいものか確認してください。\n";
			e.printStackTrace();
			return false;
		}
	}
	
	public String getStatus() {
		return status;
	}
}

