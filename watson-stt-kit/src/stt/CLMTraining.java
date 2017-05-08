package stt;

import com.ibm.watson.developer_cloud.service.exception.BadRequestException;
import com.ibm.watson.developer_cloud.service.exception.ConflictException;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Customization;

public class CLMTraining {
	private SpeechToText service;
	private String status = null;
	private String error1 = null;
	private String error2 = null;

	
	public CLMTraining(String username, String password, String endpoint) {
		try {
			service = new SpeechToText();
			service.setUsernameAndPassword(username, password);
			service.setEndPoint(endpoint);
		} catch (Exception e) {
			this.status = e.getLocalizedMessage() + "接続できませんでした。endpoint, username, passwordが正しいものか確認してください。\n";
			e.printStackTrace();
		}
	}

	public void train(String customId){
		try{
			service.trainCustomization(customId , Customization.WordTypeToAdd.ALL).execute();
			this.status = "トレーニングのリクエストが送信されました。Get Statusで状態を確認しましょう。AVAILABLEになったら使用可能です。\n";
		}catch (ConflictException e1) {
			this.error1 = e1.getMessage() + "再度試してください。";	
			e1.printStackTrace();
		}catch(BadRequestException e2){
			this.error2 = e2.getMessage() + "トレーニングができませんでした。辞書IDが正しいか、トレーニング用の単語とコーパスが登録されているか確認してください。" ;	
			e2.printStackTrace();
		}catch (Exception e) {
			this.status = e.getLocalizedMessage() + "接続できませんでした。endpoint, username, passwordが正しいものか確認してください。\n";
		}
	}
	
	public String getTrainingStatus(String customId){
		try {
			Customization c = service.getCustomization(customId).execute();
			String trngstts = c.getStatus().toString();
			this.status = "辞書の学習状況は "+trngstts+" です。\n";
			return trngstts;
		} catch (Exception e) {
			this.status = e.getLocalizedMessage() + "接続できませんでした。endpoint, username, passwordが正しいものか確認してください。\n";
			return "";
		}
	}
	
	public String getStatus(){
		return status;
	}
	
	public String getError1(){
		return error1;
	}
	
	public String getError2(){
		return error2;
	}
}
