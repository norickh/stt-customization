package stt;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
import javax.swing.filechooser.FileFilter;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;

public class STTRecognizeWindow {
	StringBuilder sbFinalText = new StringBuilder();
	StringBuilder sbTempText = new StringBuilder();
	private JTextField inCustomizationId;
	private JFrame frmSpeechToText;
	private JTextArea textAreaRecognize;
	private JTextArea textAreaStatus;
	private JCheckBox chckbxUseCustomization;
	private JPasswordField inPassword;
	private JTextField inUsername;
	private JTextField textWavFileName;
	private String wavFilepath = PathTool.get_currentpath();
	private String outputText;
	private FileInputStream audioInputStream = null;
	private RecognizeOptions options = null;
	private JScrollPane scrollPane;
	private ResourceBundle menu;
	private JCheckBox chckbxtimeStamp;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					STTRecognizeWindow window = new STTRecognizeWindow();
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
	public STTRecognizeWindow() {
		setResourceBundle();
		initialize();
	}
	
	private void setResourceBundle(){
		String language = "en";
        String country = "US";
		Locale loc = new Locale(language, country);
		loc = Locale.getDefault();      // 現在のロケール
	        System.out.println("Locale           = "  + loc.toString());
	        System.out.println("Country          = "  + loc.getCountry());
	        System.out.println("Display Country  = "  + loc.getDisplayCountry());
	        System.out.println("Language         = " + loc.getLanguage());
	        System.out.println("Display Language = " + loc.getDisplayLanguage());
        
        menu =ResourceBundle.getBundle("MenuBundle", loc);
	        System.out.println(menu.getString("useCustomization"));
	        System.out.println(menu.getString("startMic"));
	        System.out.println(menu.getString("password"));
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSpeechToText = new JFrame();
		frmSpeechToText.setTitle("STT Recognize ver.4.1 smartformat-en-mic");
		frmSpeechToText.setBounds(100, 100, 620, 580);
		frmSpeechToText.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblEndpoint = new JLabel(menu.getString("endpoint"));
		lblEndpoint.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblEndpoint.setBounds(10, 11, 93, 16);
		frmSpeechToText.getContentPane().add(lblEndpoint);

		JLabel label_2 = new JLabel(menu.getString("username"));
		label_2.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		label_2.setBounds(10, 43, 61, 12);
		frmSpeechToText.getContentPane().add(label_2);

		JLabel label_3 = new JLabel(menu.getString("model"));
		label_3.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		label_3.setBounds(10, 75, 61, 16);
		frmSpeechToText.getContentPane().add(label_3);
		
		JLabel label_4 = new JLabel(menu.getString("password"));
		label_4.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		label_4.setBounds(400, 43, 60, 16);
		frmSpeechToText.getContentPane().add(label_4);

		inUsername = new JTextField();
		inUsername.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		inUsername.setColumns(10);
		inUsername.setBounds(115, 40, 273, 26);
		frmSpeechToText.getContentPane().add(inUsername);

		inPassword = new JPasswordField();
		inPassword.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		inPassword.setColumns(8);
		inPassword.setBounds(464, 40, 116, 26);
		frmSpeechToText.getContentPane().add(inPassword);

		JComboBox comboEndPoint = new JComboBox();
		comboEndPoint.setModel(new DefaultComboBoxModel(new String[] {"https://stream.watsonplatform.net/speech-to-text/api","https://stream.watson-j.jp/speech-to-text/api"}));
		comboEndPoint.setBounds(115, 7, 465, 27);
		frmSpeechToText.getContentPane().add(comboEndPoint);
		
		JComboBox comboModel = new JComboBox();
		comboModel.setModel(new DefaultComboBoxModel(new String[] {"ja-JP_BroadbandModel", "ja-JP_NarrowbandModel","en-US_BroadbandModel","en-US_NarrowbandModel"}));
		comboModel.setBounds(115, 73, 273, 27);
		frmSpeechToText.getContentPane().add(comboModel);
		
		textWavFileName = new JTextField();
		textWavFileName.setBounds(151, 146, 207, 26);
		frmSpeechToText.getContentPane().add(textWavFileName);
		textWavFileName.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 216, 574, 240);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JScrollPane scrollPaneStatus = new JScrollPane();
		scrollPaneStatus.setBounds(6, 485, 574, 55);
		JLabel lblNewLabel = new JLabel(menu.getString("status"));
		lblNewLabel.setBounds(6, 468, 97, 16);

		inCustomizationId = new JTextField();
		inCustomizationId.setBounds(168, 108, 220, 26);
		inCustomizationId.setColumns(10);
		frmSpeechToText.getContentPane().add(inCustomizationId);

		chckbxUseCustomization = new JCheckBox(menu.getString("useCustomization"));
		chckbxUseCustomization.setBounds(7, 109, 153, 23);

		textAreaStatus = new JTextArea();
		textAreaStatus.setForeground(new Color(0, 0, 255));
		scrollPaneStatus.setViewportView(textAreaStatus);

		textAreaRecognize = new JTextArea();
		textAreaRecognize.setLineWrap(true);
		scrollPane.setViewportView(textAreaRecognize);

		frmSpeechToText.getContentPane().setLayout(null);
		frmSpeechToText.getContentPane().add(lblNewLabel);
		frmSpeechToText.getContentPane().add(scrollPaneStatus);
		frmSpeechToText.getContentPane().add(scrollPane);
		frmSpeechToText.getContentPane().add(chckbxUseCustomization);
		
		
		JCheckBox chckbxOutputTextFile = new JCheckBox();
		chckbxOutputTextFile.setText(menu.getString("outputTextFile"));
		chckbxOutputTextFile.setBounds(410, 94, 161, 22);
		frmSpeechToText.getContentPane().add(chckbxOutputTextFile);
		
		chckbxtimeStamp = new JCheckBox(menu.getString("timeStamp"));
		chckbxtimeStamp.setSelected(true);
		chckbxtimeStamp.setBounds(410, 71, 128, 23);
		frmSpeechToText.getContentPane().add(chckbxtimeStamp);
		
		JButton btnFile = new JButton(menu.getString("wavFile"));
		btnFile.setBounds(7, 146, 132, 29);
		frmSpeechToText.getContentPane().add(btnFile);		
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser(textWavFileName.getText());
				//wavファイルしか選べないようにこのメソッドをoverrideしている(が実際には選べる)
				filechooser.addChoosableFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return null;
					}

					@Override
					public boolean accept(File f) {
						if (f.isDirectory() == true) {
							return false;
						}

						if (f.getName().endsWith(".wav")) {
							return true;
						}

						return false;
					}
				});

				int selected = filechooser.showOpenDialog(btnFile);
				if (selected == JFileChooser.APPROVE_OPTION) {
					File file = filechooser.getSelectedFile();
					System.err.println(file.getAbsolutePath());
					wavFilepath = file.getAbsolutePath();
					textWavFileName.setText(file.getAbsolutePath());
					textAreaStatus.setText("有効なwavファイルが選択されました。\n");
				} else if (selected == JFileChooser.CANCEL_OPTION) {
					textAreaStatus.setText("wavファイルが選択されませんでした。\n");
				} else if (selected == JFileChooser.ERROR_OPTION) {
					textAreaStatus.setText("wavファイルが選択されませんでした。\n");
				}
				
				if ((new File(wavFilepath)).isFile() == false || wavFilepath.endsWith("wav") == false) {
					textAreaStatus.setText("ファイルが無効です。wavファイルを選択してください。\n");
					return;
				}else {
					textAreaStatus.setText("有効なwavファイルが選択されました。\n");
				}

				
			}
		});
		
		
		JButton btnUpload = new JButton(menu.getString("upload"));
		btnUpload.setBounds(10, 180, 348, 29);
		frmSpeechToText.getContentPane().add(btnUpload);
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String endPoint = (String) comboEndPoint.getSelectedItem();
				String username = inUsername.getText();
				String password = inPassword.getText();
				String model = (String) comboModel.getSelectedItem();
				String customizationId = inCustomizationId.getText();
				String wavFileName = textWavFileName.getText();
				System.out.println(endPoint + "\n"+ username + "\n"+password + "\n"+model + "\n"+customizationId + "\n"+wavFileName);
				
				if ((new File(wavFileName)).isFile() == false || wavFileName.endsWith("wav") == false) {
					textAreaStatus.setText("ファイルが無効です。wavファイルを選択してください。\n");
					btnUpload.setEnabled(true);
					return;
				}
				
				SpeechToText service = null;
				try {
					service = new SpeechToText();
					service.setUsernameAndPassword(username, password);
					service.setEndPoint(endPoint);
				} catch (Exception e1) {
					textAreaStatus.setText( e1.getLocalizedMessage() + "\n接続できませんでした。endpoint, username, passwordが正しいものか確認してください。\n" );
					btnUpload.setEnabled(true);
					return;
				}

				try {
					btnUpload.setEnabled(false);
					chckbxtimeStamp.setEnabled(false);
					chckbxUseCustomization.setEnabled(false);
					audioInputStream = new FileInputStream(new File(wavFileName));
					
					if(chckbxUseCustomization.isSelected() == true){
						options = new RecognizeOptions.Builder().contentType(HttpMediaType.AUDIO_WAV) //
								.continuous(true) //
								.interimResults(true) //
								.model(model) //
								.timestamps(true) //
								.inactivityTimeout(-1) //
								.customizationId(customizationId) //
								.build();
						System.out.println("using customization \n" + customizationId);
					} else{
						options= new RecognizeOptions.Builder().contentType(HttpMediaType.AUDIO_WAV) //
								.continuous(true) //
								.interimResults(true) //
								.model(model) //
								.timestamps(true) //
								.inactivityTimeout(-1) //
								.build();	
						System.out.println("NOT using customization" + customizationId);
					}

					ArrayList<String> ss = new ArrayList<String>();
					List<Double> st = new ArrayList<Double>();
					BaseRecognizeCallback callback = new BaseRecognizeCallback() {
						@Override
							public void onTranscription(SpeechResults speechResults) {
							if (chckbxtimeStamp.isSelected() == true) {
								outputText = new TranscriptToString_ts().setTranscriptUsingWebSocket(speechResults, ss, st);
							} else {
								outputText = new TranscriptToString().setTranscriptUsingWebSocket(speechResults, ss, st);
							}
								textAreaRecognize.setText( outputText );
								JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
								scrollBar.setValue(scrollBar.getMaximum());
						}
						@Override
							public void onDisconnected(){
							btnUpload.setEnabled(true);
							textAreaStatus.setText( "音声認識が完了しました。\n" );
							if(chckbxOutputTextFile.isSelected() == true){
								String outTxt = WavSplitFixedTime.getPreffix(wavFileName) + ".txt";
								PrintWriter pw = null;
								try {
									pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outTxt)));
									pw.print(outputText);
									textAreaStatus.append("テキストを保存しました。");
								} catch (IOException exx) {
									textAreaStatus.append("テキストの保存に失敗しました。");
									exx.printStackTrace();
								} finally {
									if(pw != null){
										pw.close();
									}
								}
							}
						}
					};

				service.recognizeUsingWebSocket(audioInputStream, options, callback);
				//TODO: customizationIDが正しくない時の例外をキャッチできていないのでキャッチする
					
				} catch (IOException e1) {
					textAreaStatus.setText( e1.getLocalizedMessage() + "\nファイルの読み込みに失敗しました。\n" );
					e1.printStackTrace();
					chckbxtimeStamp.setEnabled(true);
				} catch (Exception e2) {
					textAreaStatus.setText( e2.getLocalizedMessage() + "\n音声の認識に失敗しました。\n" );
					e2.printStackTrace();
					chckbxtimeStamp.setEnabled(true);
				} finally {
					btnUpload.setEnabled(true);
					chckbxUseCustomization.setEnabled(true);
					chckbxtimeStamp.setEnabled(true);
				}
				
			}
		});
		
	
		
		JButton btnMic = new JButton(menu.getString("startMic"));
		btnMic.setBounds(385, 146, 195, 29);
		JButton btnMicStop = new JButton(menu.getString("stopMic"));
		btnMicStop.setBounds(385, 180, 195, 29);

		btnMic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chckbxtimeStamp.setEnabled(false);
				String endPoint = (String) comboEndPoint.getSelectedItem();
				String username = inUsername.getText();
				String password = inPassword.getText();
				String model = (String) comboModel.getSelectedItem();
				String customizationId = inCustomizationId.getText();
				System.out.println(endPoint + "\n"+ username + "\n"+password + "\n"+model + "\n"+customizationId);
				

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
					if (chckbxUseCustomization.isSelected() == true) {
						options = new RecognizeOptions.Builder().continuous(true).interimResults(true)
								.contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate) //
								.model(model) //
								.timestamps(true) //
								.customizationId(customizationId) //
								.smartFormatting(true) //
								.build();
					} else {
						options = new RecognizeOptions.Builder().continuous(true).interimResults(true)
								.contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate) //
								.model(model) //
								.timestamps(true) //
								.smartFormatting(true) //
								.build();
					}

					// set callback function
					ArrayList<String> ss = new ArrayList<String>();
					List<Double> st = new ArrayList<Double>();
					BaseRecognizeCallback callback = new BaseRecognizeCallback() {
						@Override
						public void onTranscription(SpeechResults speechResults) {
							String tmp;
							if (chckbxtimeStamp.isSelected() == true) {
								tmp = new TranscriptToString_ts().setTranscriptUsingWebSocket(speechResults, ss, st);
							} else {
								tmp = new TranscriptToString().setTranscriptUsingWebSocket(speechResults, ss, st);
							}
							textAreaRecognize.setText(tmp);
							JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
							scrollBar.setValue(scrollBar.getMaximum());
						}
					};

					service.recognizeUsingWebSocket(audio, options, callback);
					textAreaStatus.setText("マイク入力の音声認識を開始しました。\n");
					System.out.println("Listening to your voice...");

					btnMicStop.addActionListener(new ActionListener() {
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
				} finally {
					chckbxtimeStamp.setEnabled(true);
				}
			}
		});

		frmSpeechToText.getContentPane().add(btnMic);
		frmSpeechToText.getContentPane().add(btnMicStop);
		
		JLabel lblNewLabel_1 = new JLabel(menu.getString("onlyWavFile"));
		lblNewLabel_1.setBounds(431, 118, 132, 16);
		frmSpeechToText.getContentPane().add(lblNewLabel_1);
		
		
		

	}
}
