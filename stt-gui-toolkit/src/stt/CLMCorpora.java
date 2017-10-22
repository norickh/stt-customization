package stt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Corpus;

public class CLMCorpora {
	private List<String> listCorpora = null;
	private SpeechToText service;
	private String status;

	public CLMCorpora(String username, String password, String endpoint) {
		try {
			service = new SpeechToText();
			service.setUsernameAndPassword(username, password);
			service.setEndPoint(endpoint);
		} catch (Exception e) {
			this.status = e.getLocalizedMessage() + "接続できませんでした。endpoint, username, passwordが正しいものか確認してください。\n";
			e.printStackTrace();
		}
	}

	public void setCorpas(String customId, String filepath, String corpasName) throws IOException {
		try {
			File txt = new File(filepath); // CSVデータファイル
			if (txt.isFile() == false || filepath.endsWith("txt") == false) {
				this.status = "ファイルが無効です。txtファイルを選択してください。\n\n";
				return;
			}
			//TODO: UTF-8での読み込み			

			service.addCorpus(customId, corpasName, txt, true).execute();
			this.status = "コーパスが登録されました。List Corporaで確認しましょう。\n";
			
		} catch (Exception e) {
			this.status = e.getLocalizedMessage() + "コーパスを追加できません。辞書IDとコーパス名を正しく選択してください。\n";
			e.printStackTrace(); 
		}
	}

	public List<String> getCorpora(String id) {
		try {
			List<Corpus> result = service.getCorpora(id).execute();
			listCorpora = new ArrayList<String>();
			for (Corpus crps : result) {
				this.listCorpora.add(crps.getName() + ",  " + crps.getStatus() );
				System.out.println(crps.getName() + ",  " + crps.getStatus());
			}
			this.status = "現在 " + result.size() + " 個のコーパスが登録されています。\n";
			return listCorpora;
		} catch (Exception e) {
			this.status = e.getLocalizedMessage() + "コーパスを確認できません。有効な辞書IDを選択してください。\n";
			e.printStackTrace();
			return listCorpora;
		}
	}

	public boolean deleteCorpus(String id, String corpusName) {
		try {
			service.deleteCorpus(id, corpusName).execute();
			System.out.println("deleted the corpus");
			this.status = "コーパスは削除されました。\n";
			return true;
		} catch (RuntimeException e1) {
			this.status = e1.getLocalizedMessage() + "コーパスを削除できません。辞書IDとコーパス名が正しい確認してください。また、BEING_PROCESSEDのコーパスは削除できないのでANALYSEDになってから試してください。\n";
			e1.printStackTrace();
			return false;
		}
	}

	public String getStatus() {
		return status;
	}

}
