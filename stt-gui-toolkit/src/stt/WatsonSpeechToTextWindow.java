package stt;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator.OfDouble;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;

import org.omg.CORBA.REBIND;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class WatsonSpeechToTextWindow {

	private JFrame frmSpeechToText;
	private JComboBox comboEndPoint;
	private JComboBox comboModel;
	private JTextField textEndPoint;
	private JTextField textUsername;
	private JTextField textWavFilename;
	private JTextField textModel;
	private JPasswordField passwordField;
	private JTextArea textAreaRecognized;
	private JTextArea textAreaStatus;
	private JTextField txtFieldCstmId;
	private RecognizeOptions options = null;
	private String wavFilepath = PathTool.get_currentpath();
	private String outputText;
	private FileInputStream audioInputStream = null;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WatsonSpeechToTextWindow window = new WatsonSpeechToTextWindow();
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
	public WatsonSpeechToTextWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSpeechToText = new JFrame();
		frmSpeechToText.setTitle("Speech To Text");
		frmSpeechToText.setBounds(300, 100, 600, 450);
		frmSpeechToText.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblEndpoint = new JLabel("endPoint");
		JLabel lblUsername = new JLabel("username");
		JLabel lblPassword = new JLabel("password");
		JLabel lblWavfile = new JLabel("wavFile");
		JLabel lblModel = new JLabel("model");
		
		comboEndPoint = new JComboBox();
		comboEndPoint.setModel(new DefaultComboBoxModel(new String[] {"https://stream.watson-j.jp/speech-to-text/api", "https://stream.watsonplatform.net/speech-to-text/api"}));
		
		comboModel = new JComboBox();
		comboModel.setModel(new DefaultComboBoxModel(new String[] {"ja-JP_BroadbandModel", "en-US_BroadbandModel"}));

		textEndPoint = new JTextField();
		textEndPoint.setEditable(false);
		textEndPoint.setText("https://stream.watson-j.jp/speech-to-text/api");
		textEndPoint.setColumns(10);

		textUsername = new JTextField();
		textUsername.setEditable(false);
		textUsername.setText("307e4ab4-a72c-443d-b876-64ff52ccca7b");
		textUsername.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setEditable(false);
		passwordField.setText("q1SzIzepUogs");

		textWavFilename = new JTextField();
		textWavFilename.setColumns(10);
		
		txtFieldCstmId = new JTextField();
		txtFieldCstmId.setColumns(10);

		JButton btnFile = new JButton("File");
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser(wavFilepath);
				//wavファイルしか選べないようにこのメソッドをoverrideしている(が実際には選べる)
				filechooser.addChoosableFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
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
					textWavFilename.setText(file.getAbsolutePath());
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

		JLabel lblRecognizedtext = new JLabel("Text");
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JCheckBox chckbxUseCustomization = new JCheckBox("Use customization");
		JCheckBox chckbxOutputTextFile = new JCheckBox("output text file");
		
		JLabel lblStatus = new JLabel("Status");
		JScrollPane scrollPaneStatus = new JScrollPane();
		
		JButton btnUpload = new JButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUpload.setEnabled(false);
				/*
				 * [KIT instance]
				 * String endPoint = "https://stream.watson-j.jp/speech-to-text/api";
				 * String username = "307e4ab4-a72c-443d-b876-64ff52ccca7b";
				 * String password = "q1SzIzepUogs";
						[Author's instance]
						String endPoint = "https://stream.watsonplatform.net/speech-to-text/api";
						String username = "80c55ecb-9c6e-40d9-94a2-094d705fecc3";
						String password = "QbVVGDfTk3bW";
				*/
				String endPoint = (String) comboEndPoint.getSelectedItem();
				String username = textUsername.getText();
				String password = passwordField.getText();
				String model = (String) comboEndPoint.getSelectedItem();
				String customizationId = txtFieldCstmId.getText();
				String wavFileName = textWavFilename.getText();
				
				if ((new File(wavFileName)).isFile() == false || wavFileName.endsWith("wav") == false) {
					textAreaStatus.setText("ファイルが無効です。wavファイルを選択してください。\n");
					btnUpload.setEnabled(true);
					return;
				}
				
//				stt.RecognizeFile sttf = new RecognizeFile(username, password, endPoint);
				SpeechToText service = null;
				try {
					service = new SpeechToText();
					service.setUsernameAndPassword(username, password);
					service.setEndPoint(endPoint);
				} catch (Exception e1) {
					textAreaStatus.setText( e1.getLocalizedMessage() + "\n接続できませんでした。endpoint, username, passwordが正しいものか確認してください。\n" );
					return;
				}

				try {
					audioInputStream = new FileInputStream(new File(wavFileName));
					
					if(chckbxUseCustomization.isSelected() == true){
						options = new RecognizeOptions.Builder().contentType(HttpMediaType.AUDIO_WAV) //
								.continuous(true) //
								.interimResults(true) //
								.model(model) //
								.timestamps(true) //
								.customizationId(customizationId) //
								.build();
						System.out.println("using customization \n" + customizationId);
					} else{
						options= new RecognizeOptions.Builder().contentType(HttpMediaType.AUDIO_WAV) //
								.continuous(true) //
								.interimResults(true) //
								.model(model) //
								.timestamps(true) //
								.build();	
						System.out.println("NOT using customization" + customizationId);
					}

					ArrayList<String> ss = new ArrayList<String>();
					List<Double> st = new ArrayList<Double>();
					BaseRecognizeCallback callback = new BaseRecognizeCallback() {
						@Override
							public void onTranscription(SpeechResults speechResults) {
								outputText = new TranscriptToString().setTranscriptUsingWebSocket(speechResults, ss, st);
								textAreaRecognized.setText( outputText );
/*								btnStopUpload.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										System.out.println("stop upload");
//										textAreaStatus.setText("中断されました\n");
										try {
											audioInputStream.close();
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
								});
*/						}
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
					btnUpload.setEnabled(true);
				} catch (RuntimeException e2) {
					textAreaStatus.setText( e2.getLocalizedMessage() + "\n音声の認識に失敗しました。\n" );
					e2.printStackTrace();
					btnUpload.setEnabled(true);
				} 
				
			}
		});
		
		
		JButton btnMicStart = new JButton("Mic start");
		
		JButton btnMicStop = new JButton("Mic Stop");
		
		
		
	
		GroupLayout groupLayout = new GroupLayout(frmSpeechToText.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblRecognizedtext, GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
							.addGap(20)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 397, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUsername)
								.addComponent(lblEndpoint)
								.addComponent(lblWavfile)
								.addComponent(lblPassword))
							.addGap(64)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(textWavFilename, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnFile))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(comboEndPoint, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textEndPoint, GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE))
								.addComponent(textUsername, GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblModel)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(textModel, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(chckbxUseCustomization)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtFieldCstmId, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(chckbxOutputTextFile, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnUpload, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnMicStart)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnMicStop))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblStatus, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(scrollPaneStatus, GroupLayout.PREFERRED_SIZE, 399, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textEndPoint, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEndpoint)
						.addComponent(comboEndPoint, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUsername))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblPassword)
							.addComponent(lblModel)
							.addComponent(textModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(29)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnFile)
								.addComponent(textWavFilename, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblWavfile))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtFieldCstmId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chckbxUseCustomization))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxOutputTextFile)
						.addComponent(btnUpload)
						.addComponent(btnMicStart)
						.addComponent(btnMicStop))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRecognizedtext, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblStatus, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPaneStatus, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		textAreaStatus = new JTextArea();
		scrollPaneStatus.setViewportView(textAreaStatus);

		textAreaRecognized = new JTextArea();
		textAreaRecognized.setLineWrap(true);
		scrollPane.setViewportView(textAreaRecognized);
//		frmSpeechToText.getContentPane().setLayout(groupLayout);
	}
}
