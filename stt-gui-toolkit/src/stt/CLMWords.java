package stt;

import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Word;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.WordData;

public class CLMWords {
	private List<String> addWords = null;
	private SpeechToText service;
	private String status;

	public CLMWords(String username, String password, String endpoint) {
		try {
			service = new SpeechToText();
			service.setUsernameAndPassword(username, password);
			service.setEndPoint(endpoint);
		} catch (Exception e) {
			this.status = e.getLocalizedMessage() + "接続できませんでした。endpoint, username, passwordが正しいものか確認してください。\n";
			e.printStackTrace();
		}
	}

	public void setAddWords(String customId, String filepath) {
		// Prepare words
		try {
			File csv = new File(filepath); // CSVデータファイル
			if (csv.isFile() == false || filepath.endsWith("csv") == false) {
				this.status = "ファイルが無効です。csvファイルを選択してください。\n\n";
				return;
			}
			
			BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(csv), "UTF-8"));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(csv), "UTF-8"));
			// FileInputStream, InputStreamReaderでUTF-8指定, MS932だとwindowsで読める
		
			// 最終行までカウント
			String line = "";
			int line_count = 0;
			while ((line = br1.readLine()) != null) {
				line_count += 1;
			}
			System.out.println(line_count);
			Word[] w = new Word[line_count]; // csv行数分だけWord型配列の箱を作成。初期値はnull
			br1.close();

			// 一行ごとに
			line = "";
			int count = 0;
			while ((line = br2.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");

				// 1行の各要素を文字列配列に格納
				int i = 0;
				List<String> ln = new ArrayList<String>();
				while (st.hasMoreTokens()) {
					ln.add(st.nextToken());
					System.out.print("[" + i + "]" + ln.get(i) + "\t");
					i++;
				}

				// 各要素をWord型変数に格納
				w[count] = new Word(); // 初期値nullのままメソッドを呼び出すとぬるぽになるので、インスタンス作って入れるイメージ
				w[count].setWord(ln.get(0));
				ln.remove(0);
				w[count].setSoundsLike(ln);
				count++;
			}
			br2.close();

			// Add words to the custom model and display the added words
			service.addWords(customId, w).execute();

			this.status = "単語が登録されました。Get Wordsで確認しましょう。\n";

		} catch (IOException e) {
			this.status = e.getLocalizedMessage() + "ファイルを認識できません。有効なcsvファイルを選択してください。\n";
			e.printStackTrace(); // Fileオブジェクト生成時の例外捕捉
		} catch (RuntimeException e) {
			this.status = e.getLocalizedMessage() + "単語を追加できません。有効な辞書IDを選択してください。\n";
			e.printStackTrace(); // BufferedReaderオブジェクトのクローズ時の例外捕捉
		}
	}

	public List<String> getWords(String id) {
		try {
			List<WordData> result = service.getWords(id, Word.Type.ALL).execute();
			addWords = new ArrayList<String>();
			for (WordData word : result) {
				System.out.println(word.getWord() + "," + word.getSoundsLike() + "," + word.getDisplayAs());
				this.addWords.add(word.getWord() + "," + word.getSoundsLike());
			}
			this.status = "現在 " + result.size() + " 個の単語が登録されています。\n";
			return addWords;
		} catch (Exception e) {
			this.status = e.getLocalizedMessage() + "単語を確認できません。有効な辞書IDを選択してください。\n";
			e.printStackTrace();
			return addWords;
		}
	}

	public void deleteWords(String id) {
		try {
			List<WordData> result = service.getWords(id, Word.Type.ALL).execute();
			for (WordData word : result) {
				String wo = word.getWord();
				System.err.println(wo);
				service.deleteWord(id, wo).execute();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					this.status = e.getLocalizedMessage() + "単語の削除中にエラーが発生しました。再度試してください。\n";
					e.printStackTrace();
				}
			}
			System.out.println("deleted all words");
		} catch (RuntimeException e1) {
			this.status = e1.getLocalizedMessage() + "単語を削除できません。有効な辞書IDを選択してください。\n";
			e1.printStackTrace();
		}
	}

	public String getStatus() {
		return status;
	}

}
