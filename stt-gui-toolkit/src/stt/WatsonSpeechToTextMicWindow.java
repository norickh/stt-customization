package stt;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class WatsonSpeechToTextMicWindow {

	private JFrame frmSpeechToText;
	private JTextArea textAreaRecognize;
	private JTextArea textAreaStatus;
	private JCheckBox chckbxUseCustomizationId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WatsonSpeechToTextMicWindow window = new WatsonSpeechToTextMicWindow();
					window.frmSpeechToText.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WatsonSpeechToTextMicWindow() {
		initialize();
	}

	StringBuilder sbFinalText = new StringBuilder();
	StringBuilder sbTempText = new StringBuilder();
	private JTextField textField;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSpeechToText = new JFrame();
		frmSpeechToText.setTitle("Speech To Text");
		frmSpeechToText.setBounds(100, 100, 450, 450);
		frmSpeechToText.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton btnMic = new JButton("\u30DE\u30A4\u30AF\u5165\u529B");
		JButton btnNewButton = new JButton("マイク入力中止");

		btnMic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String endPoint = "https://stream.watson-j.jp/speech-to-text/api";
				String username = "307e4ab4-a72c-443d-b876-64ff52ccca7b";
				String password = "q1SzIzepUogs";
				String model = "ja-JP_BroadbandModel";
				String customizationId = textField.getText();
				// Author's Instance
				// String endPoint =
				// "https://stream.watsonplatform.net/speech-to-text/api/v1";
				// String username = "80c55ecb-9c6e-40d9-94a2-094d705fecc3";
				// String password = "QbVVGDfTk3bW";
				// String customeid = "9f1ddad0-c76f-11e6-8e67-ada083c78a12";

				SpeechToText service;
				try {
					service = new SpeechToText();
					service.setUsernameAndPassword(username, password);
					service.setEndPoint(endPoint);
				} catch (Exception e1) {
					textAreaStatus.setText(
					e1.getLocalizedMessage() + "\n接続できませんでした。endpoint, username, passwordが正しいものか確認してください。\n");
					return;
				}

				try {
					// Signed PCM AudioFormat with 16kHz, 16 bit sample size,
					// mono
					int sampleRate = 16000;
					javax.sound.sampled.AudioFormat format = new javax.sound.sampled.AudioFormat(sampleRate, 16, 1,
							true, false);
					DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
					if (!AudioSystem.isLineSupported(info)) {
						System.out.println("Line not supported");
						System.exit(0);
					}
					TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
					line.open(format);
					line.start();
					AudioInputStream audio = new AudioInputStream(line);

					// set recognize options
					RecognizeOptions options;
					if (chckbxUseCustomizationId.isSelected() == true) {
						options = new RecognizeOptions.Builder().continuous(true).interimResults(true)
								.contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate) //
								.model(model) //
								.timestamps(true) //
								.customizationId(customizationId) //
								.build();
					} else {
						options = new RecognizeOptions.Builder().continuous(true).interimResults(true)
								.contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate) //
								.model(model) //
								.timestamps(true) //
								.build();
					}

					// set callback function
					ArrayList<String> ss = new ArrayList<String>();
					List<Double> st = new ArrayList<Double>();
					BaseRecognizeCallback callback = new BaseRecognizeCallback() {
						@Override
						public void onTranscription(SpeechResults speechResults) {
							String tmp = new TranscriptToString().setTranscriptUsingWebSocket(speechResults, ss, st);
							textAreaRecognize.setText(tmp);
						}
					};

					service.recognizeUsingWebSocket(audio, options, callback);
					textAreaStatus.setText("マイク入力の音声認識を開始しました。\n");
					System.out.println("Listening to your voice...");

					btnNewButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								line.stop();
								line.close();
								textAreaStatus.setText("マイク入力の音声認識を終了しました。\n");
								return;
							} catch (Exception ex) {
								textAreaStatus.setText("マイク入力の音声認識を正常に終了できませんでした。\n");
								ex.printStackTrace();
								return;
							}
						}
					});

				} catch (Exception ex) {
					textAreaStatus.setText("マイク入力の音声認識に失敗しました。\n");
					ex.printStackTrace();
				}

			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane scrollPane_1 = new JScrollPane();
		JLabel lblNewLabel = new JLabel("Status");

		textField = new JTextField();
		textField.setColumns(10);

		chckbxUseCustomizationId = new JCheckBox("Use Customization ID");
		GroupLayout groupLayout = new GroupLayout(frmSpeechToText.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING).addComponent(lblNewLabel, Alignment.LEADING).addGroup(
								groupLayout
										.createSequentialGroup().addGroup(groupLayout
												.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
														groupLayout.createSequentialGroup()
																.addComponent(btnMic, GroupLayout.PREFERRED_SIZE, 207,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(18).addComponent(btnNewButton,
																		GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
												.addComponent(scrollPane_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
														432, Short.MAX_VALUE)
												.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
														432, Short.MAX_VALUE)
												.addGroup(groupLayout.createSequentialGroup()
														.addComponent(chckbxUseCustomizationId).addPreferredGap(
																ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(textField, GroupLayout.PREFERRED_SIZE, 257,
																GroupLayout.PREFERRED_SIZE)))
										.addGap(19)))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addGap(3)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnMic)
								.addComponent(btnNewButton))
						.addGap(6)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxUseCustomizationId))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 254, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblNewLabel)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		textAreaStatus = new JTextArea();
		scrollPane_1.setRowHeaderView(textAreaStatus);

		textAreaRecognize = new JTextArea();
		textAreaRecognize.setLineWrap(true);
		scrollPane.setViewportView(textAreaRecognize);
		frmSpeechToText.getContentPane().setLayout(groupLayout);
	}
}
