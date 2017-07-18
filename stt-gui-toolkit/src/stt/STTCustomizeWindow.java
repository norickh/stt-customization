package stt;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class STTCustomizeWindow {

	private JFrame frame;
	private JTextField inName;
	private JTextField inDescription;
	private JPasswordField inPassword;
//	private JTextField inEndpoint;
	private JTextField inUsername;
	private JTextArea outStatus;
	private JTextField outCustomizationId;
	private JTextArea outWordData;
	private String customizationId;
	private String csvFilepath = PathTool.get_currentpath();
	private String txtFilepath = PathTool.get_currentpath();
	private List<String> WordData;
	private JTextField inCsvFileName;
	private JTextField outTrainingStatus;
	private JTextField inTxtFileName;
	private JTextField inCorpusName;
	private ResourceBundle menu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					STTCustomizeWindow window = new STTCustomizeWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public STTCustomizeWindow() {
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
	        System.out.println(menu.getString("customLabel"));
	        System.out.println(menu.getString("name"));
	        System.out.println(menu.getString("corpusTxtFile"));
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Speech To Text Cutomize ver.8.1");
		frame.getContentPane().setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		frame.setBounds(100, 100, 740, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel label_3 = new JLabel(menu.getString("endpoint"));
		label_3.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		label_3.setBounds(10, 11, 88, 16);
		frame.getContentPane().add(label_3);

		JLabel label_2 = new JLabel(menu.getString("username"));
		label_2.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		label_2.setBounds(10, 35, 88, 16);
		frame.getContentPane().add(label_2);

		JLabel label_4 = new JLabel(menu.getString("password"));
		label_4.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		label_4.setBounds(400, 37, 60, 16);
		frame.getContentPane().add(label_4);

		/*inEndpoint = new JTextField();
		inEndpoint.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		inEndpoint.setText("https://stream.watsonplatform.net/speech-to-text/api");
		inEndpoint.setColumns(10);
		inEndpoint.setBounds(110, 5, 488, 26);
		frame.getContentPane().add(inEndpoint);*/

		JComboBox comboEndPoint = new JComboBox();
		comboEndPoint.setModel(new DefaultComboBoxModel(new String[] {"https://stream.watsonplatform.net/speech-to-text/api","https://stream.watson-j.jp/speech-to-text/api"}));
		comboEndPoint.setBounds(110, 5, 488, 26);
		frame.getContentPane().add(comboEndPoint);

		inUsername = new JTextField();
		inUsername.setText("0d8800cd-b182-4ecd-b586-836e97190452");
			//higashide instnce: 0d8800cd-b182-4ecd-b586-836e97190452
			//inahara instance: 02c7ce7c-6d76-495f-a185-35f997e7b65c
		inUsername.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		inUsername.setColumns(10);
		inUsername.setBounds(110, 34, 278, 26);
		frame.getContentPane().add(inUsername);
		


		inPassword = new JPasswordField();
		inPassword.setText("5xfSzJAxEmOW");
			//higashide instance: 5xfSzJAxEmOW
			//inahara instance: tjH7vaaxt34l
		inPassword.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		inPassword.setColumns(8);
		inPassword.setBounds(468, 34, 130, 26);
		frame.getContentPane().add(inPassword);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(128, 128, 128), 1, true));
		panel.setBounds(10, 65, 662, 205);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblName = new JLabel(menu.getString("name"));
		lblName.setBounds(10, 30, 90, 16);
		panel.add(lblName);

		inName = new JTextField();
		inName.setText("Test");
		inName.setBounds(126, 25, 117, 26);
		panel.add(inName);
		inName.setColumns(10);

		JLabel lblDescription = new JLabel(menu.getString("description"));
		lblDescription.setBounds(10, 55, 90, 16);
		panel.add(lblDescription);

		inDescription = new JTextField();
		inDescription.setText("This is test.");
		inDescription.setBounds(126, 53, 341, 26);
		panel.add(inDescription);
		inDescription.setColumns(10);

		JLabel lblNewLabel = new JLabel(menu.getString("status"));
		lblNewLabel.setBounds(167, 542, 140, 20);
		frame.getContentPane().add(lblNewLabel);

		JButton btnCreateClm = new JButton(menu.getString("create"));
		btnCreateClm.setBounds(516, 50, 108, 29);
		panel.add(btnCreateClm);

		outCustomizationId = new JTextField();
		outCustomizationId.setEditable(false);
		outCustomizationId.setBounds(133, 280, 335, 30);
		frame.getContentPane().add(outCustomizationId);
		outCustomizationId.setColumns(10);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(126, 85, 498, 110);
		panel.add(scrollPane_2);

		DefaultListModel<String> modelList = new DefaultListModel<String>();
		JList<String> outCLMList = new JList<String>(modelList);
		scrollPane_2.setViewportView(outCLMList);

		JButton btnList = new JButton(menu.getString("listModels"));
		btnList.setBounds(6, 85, 100, 30);
		panel.add(btnList);

		JButton btnDelete = new JButton(menu.getString("delete"));
		btnDelete.setBounds(6, 125, 100, 30);
		panel.add(btnDelete);

		JButton btnDeleteAll = new JButton(menu.getString("deleteAll"));
		btnDeleteAll.setBounds(6, 165, 100, 30);
		panel.add(btnDeleteAll);

		JLabel lblManageCustomLanguage = new JLabel(menu.getString("customLabel"));
		lblManageCustomLanguage.setBounds(10, 6, 266, 16);
		panel.add(lblManageCustomLanguage);
		
		JComboBox comboModel = new JComboBox();
		comboModel.setBounds(357, 26, 273, 27);
		panel.add(comboModel);
		comboModel.setModel(new DefaultComboBoxModel(new String[] {"ja-JP_BroadbandModel", "ja-JP_NarrowbandModel","en-US_BroadbandModel","en-US_NarrowbandModel"}));
		
		JLabel lblSpeechModel = new JLabel(menu.getString("model"));
		lblSpeechModel.setBounds(255, 30, 90, 16);
		panel.add(lblSpeechModel);

		btnDeleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outCustomizationId.setText("");
				stt.CLMList clmList = new CLMList(inUsername.getText(), inPassword.getText(), (String) comboEndPoint.getSelectedItem(), (String) comboModel.getSelectedItem());
				if (clmList.deleteAllCLMs()) {
					modelList.clear();
				}
				outStatus.setText(clmList.getStatus() + "\n");
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outCustomizationId.setText("");
				if (!outCLMList.isSelectionEmpty()) {
					int index = outCLMList.getSelectedIndex();
					String in = outCLMList.getSelectedValue();
					String[] str = in.split("ID: ");
					customizationId = str[1];
					stt.CLMList clmList = new CLMList(inUsername.getText(), inPassword.getText(), (String) comboEndPoint.getSelectedItem(),(String) comboModel.getSelectedItem());
					if (clmList.deleteCLMs(customizationId)) {
						modelList.remove(index);
					}
					outStatus.setText(clmList.getStatus() + "\n");
				} else {
					outStatus.setText("削除したい辞書を選択してください\n\n");
				}
			}
		});
		btnList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outCustomizationId.setText("");
				modelList.clear();
				stt.CLMList clmList = new CLMList(inUsername.getText(), inPassword.getText(), (String) comboEndPoint.getSelectedItem(), (String) comboModel.getSelectedItem());
				List<String> idNames = clmList.listCLMs();
				for (String id : idNames) {
					modelList.addElement(id);
				}
				outStatus.setText(clmList.getStatus() + "\n");
			}
		});

		btnCreateClm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outCustomizationId.setText("");
				System.out.println(inUsername.getText() + "\n" + inPassword.getText() + "\n" + (String) comboEndPoint.getSelectedItem()
						+"\n" + (String) comboModel.getSelectedItem() + "\n" + inName.getText() + "\n" + inDescription.getText());
				
				stt.CLMCreate clm = new CLMCreate(inUsername.getText(), inPassword.getText(), (String) comboEndPoint.getSelectedItem(), 
						inName.getText(), inDescription.getText(), (String) comboModel.getSelectedItem());
				customizationId = clm.getCustomizationId();
				outStatus.setText(clm.getStatus() + "\n");
			}
		});

		JButton btnSetClm = new JButton(menu.getString("selectModel"));
		btnSetClm.setBounds(10, 281, 115, 30);
		frame.getContentPane().add(btnSetClm);
		btnSetClm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!outCLMList.isSelectionEmpty()) {
					String in = outCLMList.getSelectedValue();
					String[] str = in.split("ID: ");
					customizationId = str[1];
					outCustomizationId.setText(customizationId);
					outStatus.setText("カスタマイズまたはトレーニングを行う辞書が設定されました。\n\n");
				} else {
					outStatus.setText("カスタマイズまたはトレーニングを行う辞書をリストから選択してください。\n\n");
				}

			}
		});


		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.GRAY, 1, true));
		panel_1.setBounds(10, 325, 328, 205);
		frame.getContentPane().add(panel_1);

		JButton btnFile = new JButton(menu.getString("csvFile"));
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser(csvFilepath); // ファイル選択用クラス
				int selected = filechooser.showOpenDialog(frame); // 「開く」ダイアログ表示
				if (selected == JFileChooser.APPROVE_OPTION) { // ファイルが選択されたら
					File file = filechooser.getSelectedFile();
					csvFilepath = file.getAbsolutePath();
					inCsvFileName.setText(csvFilepath); // ラベルの文字をファイル名に
				}

				if ((new File(csvFilepath)).isFile() == false || csvFilepath.endsWith("csv") == false) {
					outStatus.setText("ファイルが無効です。csvファイルを選択してください。\n\n");
				} else {
					outStatus.setText("有効なcsvファイルが選択されました。\n\n");
				}
			}
		});

		inCsvFileName = new JTextField();
		inCsvFileName.setColumns(10);
		
		JButton btnAddWords = new JButton(menu.getString("addWords"));
		btnAddWords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outWordData.setText("");
				stt.CLMWords clma = new CLMWords(inUsername.getText(), inPassword.getText(), (String) comboEndPoint.getSelectedItem());
				clma.setAddWords(outCustomizationId.getText(), inCsvFileName.getText());
				outStatus.setText(clma.getStatus()+"\n");
			}
		});

		
		JButton btnGetWords = new JButton(menu.getString("getWords"));
		btnGetWords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outWordData.setText("");
				stt.CLMWords clma = new CLMWords(inUsername.getText(), inPassword.getText(), (String) comboEndPoint.getSelectedItem());
				WordData = new ArrayList<String>();
				WordData = clma.getWords(outCustomizationId.getText());
				System.out.println(WordData);
				for (String str : WordData) {
					outWordData.append(str);
					outWordData.append("\n");
				}
				outStatus.setText(clma.getStatus()+"\n");
			}
		});

		
		JButton btnDeleteWords = new JButton(menu.getString("deleteWords"));
		btnDeleteWords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outWordData.setText("");
				stt.CLMWords clma = new CLMWords(inUsername.getText(), inPassword.getText(), (String) comboEndPoint.getSelectedItem());
				clma.deleteWords(outCustomizationId.getText());
				outStatus.setText(clma.getStatus()+"\n");
			}
		});

		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnFile, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnDeleteWords, 0, 0, Short.MAX_VALUE)
						.addComponent(btnGetWords, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnAddWords, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(inCsvFileName, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(9)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnFile)
						.addComponent(inCsvFileName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddWords))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(44)
							.addComponent(btnGetWords)
							.addGap(18)
							.addComponent(btnDeleteWords))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)))
					.addContainerGap())
		);
		outWordData = new JTextArea();
		scrollPane.setViewportView(outWordData);
		panel_1.setLayout(gl_panel_1);


		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.GRAY, 1, true));
		panel_2.setBounds(350, 326, 364, 204);
		frame.getContentPane().add(panel_2);

		
		
		JLabel lblCorpusName = new JLabel(menu.getString("corpusName"));
		lblCorpusName.setBounds(16, 40, 121, 16);
		panel_2.add(lblCorpusName);
		
		inCorpusName = new JTextField();
		inCorpusName.setBounds(133, 36, 225, 26);
		panel_2.add(inCorpusName);
		inCorpusName.setColumns(10);
		

		inTxtFileName = new JTextField();
		inTxtFileName.setBounds(133, 6, 225, 26);
		inTxtFileName.setColumns(10);
		
		DefaultListModel<String> modelList2 = new DefaultListModel<String>();
		JList<String> outCorporaList = new JList<String>(modelList2);
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(136, 71, 222, 127);
		scrollPane_3.setViewportView(outCorporaList);
		
		JButton btnCorpusTxtFile = new JButton(menu.getString("corpusTxtFile"));
		btnCorpusTxtFile.setBounds(6, 6, 121, 29);
		btnCorpusTxtFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser(txtFilepath); // ファイル選択用クラス
				int selected = filechooser.showOpenDialog(frame); // 「開く」ダイアログ表示
				if (selected == JFileChooser.APPROVE_OPTION) { // ファイルが選択されたら
					File file = filechooser.getSelectedFile();
					txtFilepath = file.getAbsolutePath();
					inTxtFileName.setText(txtFilepath); // ラベルの文字をファイル名に
				}

				if ((new File(txtFilepath)).isFile() == false || txtFilepath.endsWith("csv") == false) {
					outStatus.setText("ファイルが無効です。txtファイルを選択してください。\n\n");
				} else {
					outStatus.setText("有効なtxtファイルが選択されました。\n\n");
				}

			}
		});

		JButton btnAddCorpus = new JButton(menu.getString("addCorpus"));
		btnAddCorpus.setBounds(6, 68, 121, 29);
		btnAddCorpus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelList2.clear();
				stt.CLMCorpora clmc = new CLMCorpora(inUsername.getText(), inPassword.getText(), (String) comboEndPoint.getSelectedItem());
				try {
					clmc.setCorpas(outCustomizationId.getText(), inTxtFileName.getText(), inCorpusName.getText());
					outStatus.setText(clmc.getStatus()+"\n");
				} catch (IOException e1) {
					e1.printStackTrace();
					outStatus.setText("ファイルの読み込みに失敗しました。\n");
				}
			}
		});

		JButton btnListCorpora = new JButton(menu.getString("listCorpora"));
		btnListCorpora.setBounds(6, 109, 121, 29);
		btnListCorpora.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelList2.clear();
				stt.CLMCorpora clmc = new CLMCorpora(inUsername.getText(), inPassword.getText(), (String) comboEndPoint.getSelectedItem());
				List<String> crpr = clmc.getCorpora(outCustomizationId.getText());
				for (String crps : crpr) {
					modelList2.addElement(crps);
				}
				outStatus.setText(clmc.getStatus() + "\n");
			}
		});

		JButton btnDeleteCorpus = new JButton(menu.getString("deleteCorpus"));
		btnDeleteCorpus.setBounds(6, 150, 121, 29);
		btnDeleteCorpus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!outCorporaList.isSelectionEmpty()) {
					int index = outCorporaList.getSelectedIndex();
					String in = outCorporaList.getSelectedValue();
					String str[] = in.split(",");
					System.out.println(str[0] + str[1]);
					stt.CLMCorpora clmc = new CLMCorpora(inUsername.getText(), inPassword.getText(), (String) comboEndPoint.getSelectedItem());
					if ( clmc.deleteCorpus(outCustomizationId.getText(), str[0]) ) {
						modelList2.remove(index);
					}
					outStatus.setText(clmc.getStatus() + "\n");
				} else {
					outStatus.setText("削除したいコーパスを選択してください\n\n");
				}
				
			}
		});
		
		panel_2.setLayout(null);
		panel_2.add(btnListCorpora);
		panel_2.add(btnCorpusTxtFile);
		panel_2.add(inTxtFileName);
		panel_2.add(btnAddCorpus);
		panel_2.add(btnDeleteCorpus);
		panel_2.add(scrollPane_3);


		
		

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(167, 562, 505, 111);
		frame.getContentPane().add(scrollPane_1);

		outStatus = new JTextArea();
		outStatus.setLineWrap(true);
		scrollPane_1.setViewportView(outStatus);
		outStatus.setForeground(new Color(0, 0, 255));

		outTrainingStatus = new JTextField();
		outTrainingStatus.setEditable(false);
		outTrainingStatus.setBounds(585, 280, 130, 30);
		frame.getContentPane().add(outTrainingStatus);
		outTrainingStatus.setColumns(10);

		JButton btnGetStatus = new JButton(menu.getString("getStatus"));
		btnGetStatus.setBounds(478, 280, 100, 30);
		frame.getContentPane().add(btnGetStatus);
		btnGetStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stt.CLMTraining clmt = new CLMTraining(inUsername.getText(),inPassword.getText(), (String) comboEndPoint.getSelectedItem());
				String s = clmt.getTrainingStatus(outCustomizationId.getText());
				outTrainingStatus.setText(s);
				outStatus.setText(clmt.getStatus());
			}
		});

		JButton btnTraining = new JButton(menu.getString("training"));
		btnTraining.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		btnTraining.setBounds(15, 562, 130, 45);
		frame.getContentPane().add(btnTraining);

		btnTraining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				while (true) {
					try {
						stt.CLMTraining clmt = new CLMTraining(inUsername.getText(),inPassword.getText(), (String) comboEndPoint.getSelectedItem());
						clmt.train(outCustomizationId.getText());
						String e1msg = clmt.getError1();
						String e2msg = clmt.getError2();
						if (e1msg != null) {
							System.out.println("conflict error");
							Thread.sleep(5000);
						} else if (e2msg != null) {
							System.out.println("request error");
							outStatus.setText(e2msg);
							break;
						} else {
							outStatus.setText(clmt.getStatus() );
							System.out.println("break");
							break;
						}
					} catch (InterruptedException exception) {
						outStatus.setText("トレーニング中にエラーが発生しました。再度試してください。\n\n");
						exception.printStackTrace();
						break;
					}
				}
			}
		});

		/*
		 * else{ //Display status. try{ for (int x = 0; x < 30; x++) { String
		 * status = clmt.getTrainingStatus(inUsername.getText(),
		 * inPassword.getText(), outCustomizationId.getText());
		 * outTrainingStatus.setText(status);
		 * if(status.equals("AVAILABLE")){break;}
		 * 
		 * } }catch(InterruptedException e1){ e1.printStackTrace();
		 * outStatus.append(e1.getMessage()); }
		 */

	}
}
