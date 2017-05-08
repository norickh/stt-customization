package stt;

import java.util.ArrayList;
import java.util.List;

import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
/*
 * add Time Stamp to output string
 * */
public class TranscriptToString_ts {
	StringBuilder sb;
	
	public TranscriptToString_ts() {
		sb = new StringBuilder();
	}
			
	public String setTranscriptUsingWebSocket(SpeechResults transcript, ArrayList<String> ss, List<Double> st){
		
		int idx = transcript.getResultIndex();
		if (idx >= ss.size()) {
			ss.add("");
			st.add(0.1);
		}

		System.out.println(transcript);

		if (transcript.isFinal() == true) {
//			btnUpload.setEnabled(true);
			// textAreaRecognized.append("\n");
		}

		for (Transcript t : transcript.getResults()) {
			for (SpeechAlternative s : t.getAlternatives()) {
				ss.set(idx, s.getTranscript());
				st.set(idx, s.getTimestamps().get(0).getStartTime());
			}
		}

		sb = new StringBuilder();
		for (int n = 0; n < ss.size(); n++) {
			sb.append("[" + st.get(n).toString() + "]");
			sb.append(ss.get(n));
			sb.append("\n");
		}

//		String str = stagnationRemove(sb.toString());
		String str = TranscriptStagnation.stagnationDetect(sb.toString());
		return str;
	}
	
	public String setTranscript(SpeechResults transcript){
		int idx = 0;
		
		ArrayList<String> ss = new ArrayList<String>();
		ArrayList<Double> st = new ArrayList<Double>();

		for (Transcript t : transcript.getResults()) {
			for (SpeechAlternative s : t.getAlternatives()) {
				ss.add(idx, s.getTranscript());
				st.add(idx, s.getTimestamps().get(0).getStartTime());
				idx ++;
			}
		}
		
		sb = new StringBuilder();
		for (int n = 0; n < ss.size(); n++) {
			sb.append(ss.get(n));
		}

		String str = TranscriptStagnation.stagnationRemove(sb.toString()+"\n");
		return str;
	}

}
