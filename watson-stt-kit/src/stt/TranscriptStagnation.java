package stt;

public class TranscriptStagnation {
	
	public static String stagnationRemove(String str) {
		String[] rmvee = { "D_エー", "D_エ", "D_ウーン", "D_アノー", "D_ア", "D_マ", "D_オー", "D_ソノ","D_コー", " " };
		for (String regrex : rmvee) {
			str = str.replaceAll(regrex, "");
		}
		return str;
	}
	
	public static String stagnationDetect(String str) {
		String replacedStr = str;
		String[] filler = { "D_エー", "D_エ", "D_ウーン", "D_アノー", "D_ア", "D_マ", "D_オー", "D_ソノ", "D_コー", " " };
		String[] altFiller = { "えー", "え", "うーん", "あのー", "あ", "ま", "おー", "その", "こー","" };
		for (int i=0; i<filler.length; i++) {
			replacedStr = replacedStr.replace(filler[i], altFiller[i]);
		}
		return replacedStr;
	}

}
