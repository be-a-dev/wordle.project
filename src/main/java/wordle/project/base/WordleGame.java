package wordle.project.base;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class WordleGame implements ActionListener {

	class WordPanel extends JPanel {

		JLabel[] wordColumns = new JLabel[5];

		public WordPanel() {
			this.setLayout(new GridLayout(1, 5));
			Border blackBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
			for (int i = 0; i < 5; i++) {
				wordColumns[i] = new JLabel();
				wordColumns[i].setHorizontalAlignment(JLabel.CENTER);
				wordColumns[i].setOpaque(true);
				wordColumns[i].setBorder(blackBorder);
				this.add(wordColumns[i]);
			}
		}

		public void clearWordPanel() {
			for (int i = 0; i < 5; i++) {
				wordColumns[i].setText("");
			}
		}

		public void setPanelText(String charValue, int position, Color color) {
			this.wordColumns[position].setText(charValue);
			this.wordColumns[position].setBackground(color);
		}
	}

	class UserPanel extends JPanel {

		private JTextField userInput;
		private JButton okButton;

		public UserPanel() {
			this.setLayout(new GridLayout(1, 2));
			userInput = new JTextField();
			this.add(userInput);
			okButton = new JButton("OK");
			this.add(okButton);
		}

		public JTextField getUserInput() {
			return userInput;
		}

		public JButton getOkButton() {
			return okButton;
		}

	}

	private JFrame gameFrame;
	private WordPanel[] wordPanelArray = new WordPanel[6];
	private UserPanel userPanel;
	private String wordleString;
	private int count = 0;

	public WordleGame() {
		gameFrame = new JFrame("Wordle Game");
		gameFrame.setSize(300, 300);
		gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameFrame.setLayout(new GridLayout(7, 1));
		gameFrame.setVisible(true);
		gameFrame.setLocationRelativeTo(null);

		for (int i = 0; i < 6; i++) {
			wordPanelArray[i] = new WordPanel();
			gameFrame.add(wordPanelArray[i]);
		}
		userPanel = new UserPanel();
		userPanel.getOkButton().addActionListener(this);
		gameFrame.add(userPanel);
		gameFrame.revalidate();

		wordleString = getWordleString();
		System.out.println("Word for the day : " + wordleString);
	}

	public static void main(String[] args) {
		new WordleGame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String userWord = this.userPanel.getUserInput().getText();

		if (userWord.length() > 4) {
			if (isWordleWordEqualTo(userWord.trim().toUpperCase())) {
				clearAllPanels();
				JOptionPane.showMessageDialog(null, "You Win!!!", "Congrats", JOptionPane.INFORMATION_MESSAGE);
				gameFrame.dispose();
				return;
			}
		}
		if (count > 5) {
			JOptionPane.showMessageDialog(null, "You Lost.Better luck next time.", "Oops",
					JOptionPane.INFORMATION_MESSAGE);
			gameFrame.dispose();
			return;
		}
		count++;
	}

	private void clearAllPanels() {
		for (int i = 0; i <= count; i++) {
			wordPanelArray[i].clearWordPanel();
		}
	}

	private boolean isWordleWordEqualTo(String userWord) {
		List<String> wordleWordsList = Arrays.asList(wordleString.split(""));
		String[] userWordsArray = userWord.split("");
		List<Boolean> wordMatchesList = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			if (wordleWordsList.contains(userWordsArray[i])) {
				if (wordleWordsList.get(i).equals(userWordsArray[i])) {
					getActivePanel().setPanelText(userWordsArray[i], i, Color.GREEN);
					wordMatchesList.add(true);
				} else {
					getActivePanel().setPanelText(userWordsArray[i], i, Color.YELLOW);
					wordMatchesList.add(false);
				}
			} else {
				getActivePanel().setPanelText(userWordsArray[i], i, Color.GRAY);
				wordMatchesList.add(false);
			}
		}
		return !wordMatchesList.contains(false);
	}

	public WordPanel getActivePanel() {
		return this.wordPanelArray[count];
	}

	public String getWordleString() {
		Path path = Paths.get("..\\\\wordle.project\\\\assets\\\\Words.txt");
		List<String> wordList = new ArrayList<>();
		try {
			wordList = Files.readAllLines(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Random random = new Random();
		int position = random.nextInt(wordList.size());
		return wordList.get(position).trim().toUpperCase();
	}

}
