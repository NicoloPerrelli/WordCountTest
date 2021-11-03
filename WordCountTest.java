
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.junit.*;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.*;
import java.awt.event.*;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


class WordsObj {
	String wordBank;
	int wordCount;

	public WordsObj(String wordBank, int wordCount) {
		this.wordBank = wordBank;
		this.wordCount = wordCount;
	}

	public String getWordBank() {return this.wordBank;}
	public void setWordBank(String word) {this.wordBank = word;}

	public int getWordCount() {return this.wordCount;}
	public void setWordCount(int number) {this.wordCount = number;}

	public String toString() {return this.wordBank + " " + this.wordCount;}
}

class SortCount implements Comparator<WordsObj> {
	public int compare(WordsObj a, WordsObj b){return b.wordCount - a.wordCount;}
}

//  <=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=->
//<=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=->
//  <=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=->

public class App {

	static void GUI() {

		JFrame frame = new JFrame("Word Count GUI App");
		JPanel panel = new JPanel();
		JTextArea label1 = new JTextArea ("");
		JButton button = new JButton("Start Count");
		GroupLayout layout = new GroupLayout(panel);

		button.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				try {
					label1.setText(wordCount(fileRead("\\C:\\Users\\Nicolo Perrelli\\Desktop\\poemText.txt")));
					//more stuff to make GUI look nice
				}
				catch (FileNotFoundException e1) { e1.printStackTrace(); }
			}
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,1000);
		frame.add(panel);
		panel.setLayout(layout);
		button.setBounds(10, 10, 100, 50);
		panel.add(button);
		label1.setBounds(10, 70, 970, 900);
		label1.setLineWrap(true);
		label1.setWrapStyleWord(true);
		label1.setFont(new Font("Serif", Font.ITALIC, 18));
		panel.add(label1);
		
		frame.setVisible(true);
	}

	@Test
	@DisplayName("GUI Tests")
	//TESTS ON GUI ELEMENTS
	public void testGUI() {
		//a robot or a @before and find way to test elements
		//i have not made the gui a class so it is harder to test this way
	}


	static String[] fileRead(String filePath) throws FileNotFoundException {
		//move data from .txt file
		Scanner sc = new Scanner(new File(filePath));
		List<String> lines = new ArrayList<String>();

		while (sc.hasNext()) {
			//for cleaning up edge casses
			String splitIn = sc.next().replaceAll("—", " ");

			//take edge case into an array for seperate inserts with checks to filter out nulls and " "'s
			if (splitIn.contains(" ")) {
				String[] splitWords = splitIn.split(" ", 2);
				if (!splitWords[0].isEmpty()) {lines.add(splitWords[0]);}
				if (!splitWords[1].isEmpty()) {lines.add(splitWords[1]);}}
			else if (!splitIn.isEmpty()) {lines.add(splitIn);}
			else{System.out.println("SOMETHING DIDN'T FIT");}
		}

		String[] str = lines.toArray(new String[0]);
		return str;
	}

	@Test(expected = FileNotFoundException.class)
	@DisplayName("File is not empty")
	public void testFileReadErr() throws FileNotFoundException {
		//test FNF Exception
		fileRead("\\C:\\Users\\Not A Real Name\\Desktop\\BadFile.txt");
	}
		
	@Test
	@DisplayName("File reads correctly")
	public void testFileRead() throws FileNotFoundException {
		String[] testString1 = {"Yes", "No", "Yes;"};//new line and "-"s
		String[] testString2 = {"No.", "Yes", "Spaces", "Lines"};//Iregular Lines and Spaces.
		assertArrayEquals(testString1, fileRead("\\C:\\Users\\Nicolo Perrelli\\Desktop\\TestFile1.txt"));
		assertArrayEquals(testString2, fileRead("\\C:\\Users\\Nicolo Perrelli\\Desktop\\TestFile2.txt"));
	}


	static String wordCount(String[] str) {

		Boolean newWordbBoolean;
		String word;
		List<WordsObj> wb = new ArrayList<WordsObj>();

		for (int i = 0; i < str.length; i++) {
			// Grab Word
			word = str[i];

			// Scrub Word of Non-alphanumeric
			word = word.replaceAll("[^a-zA-Z0-9]", "");

			//set bool to true till proven otherwise for this word.
			newWordbBoolean=true;
			//System.out.println(word+" word");
			for (WordsObj toss: wb) {

				if (toss.getWordBank().equals(word)) {
					newWordbBoolean=false;
					toss.setWordCount(toss.getWordCount()+1);
					break;
				}
			}

			//if  word is new add it and reset the key
			if (newWordbBoolean) {
				//System.out.print("New ");
				WordsObj newThing = new WordsObj(word,1);
				wb.add(newThing);
			}
		}
		//for (WordsObj asdf: wb) {System.out.println(asdf);}//unsorted
		Collections.sort(wb, new SortCount());
		return (wb.toString());
	}

	@Test
	@DisplayName("Test wordCount()")
	public void testWordCount() {
		String[] testString1 = {"yes", "no", "yes"};//quantity and placment1
		String[] testString2 = {"no", "yes"};//quantity and placment2
		String[] testString3 = {"yes", "yes", "yes"};//all the same
		String[] testString4 = {"", "", ""};//array of empty
		String[] testString5 = {};//empty array
		String[] testString6 = {"no.", "'yes'"};//Character Scrub

		assertEquals("[yes 2, no 1]", wordCount(testString1));
		assertEquals("[no 1, yes 1]", wordCount(testString2));
		assertEquals("[yes 3]", wordCount(testString3));
		assertEquals("[ 3]", wordCount(testString4));
		assertEquals("[]", wordCount(testString5));
		assertEquals("[no 1, yes 1]", wordCount(testString6));
	}


	public static void main(String[] args) {
		GUI();
	};
}


