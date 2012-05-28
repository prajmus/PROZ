package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.BlockingQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Model;

import controller.WordEvent;


/**
 * Class extending JFrame. It's made to display the main learning window of the application
 * @author Jakub Borowski
 *
 */
public class LearnFrame extends JFrame implements Frames 
{
	private final BlockingQueue<WordEvent> eventQueue;
	private final Model model;
	private JPanel learnPanel;
	private JButton knowButton;
	private JButton repeatButton;
	private JButton dontKnowButton;
	private JButton checkButton;
	private JTextField toTranslateWordField;
	private JTextField translationWordField;
	private JLabel toTranslateWordLabel;
	private JLabel translationWordLabel;
	private JLabel correctWordLabel;
	/**
	 * Constructor of the class, invoking prepareFrame method.
	 */
	public LearnFrame(BlockingQueue<WordEvent> eventQueue, Model model)
	{
		this.eventQueue = eventQueue;
		this.model = model;
		prepareMenu();
		prepareFrame();
	}
	
	/**
	 * Method that draws components on the screen.
	 */
	public void prepareFrame()
	{
		setTitle("Word Learner");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(370,130);
		setResizable(true);
		learnPanel = new JPanel();
		learnPanel.setLayout(new BorderLayout());
		
		JPanel westPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		toTranslateWordLabel = new JLabel("Word:");
		toTranslateWordLabel.setMaximumSize(new Dimension(150, 35));
		toTranslateWordField = new JTextField();
		toTranslateWordField.setEditable(false);
		toTranslateWordField.setMaximumSize(new Dimension(150,30));

		translationWordLabel = new JLabel("Translation:");
		translationWordField = new JTextField();
		translationWordField.setMaximumSize(new Dimension(150,30));
		translationWordField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent event)
			{
				if(translationWordField.getDocument().getLength() > 0)
					checkButton.setEnabled(true);
				else
					checkButton.setEnabled(false);
			}
		});
		westPanel.add(toTranslateWordLabel);
		westPanel.add(translationWordLabel);
		centerPanel.add(toTranslateWordField);
		centerPanel.add(translationWordField);
		
		learnPanel.add(westPanel, BorderLayout.WEST);
		learnPanel.add(centerPanel, BorderLayout.CENTER);

		
		correctWordLabel = new JLabel();
		learnPanel.add(correctWordLabel, BorderLayout.EAST);
		
		prepareButtons();
		
		getRootPane().setDefaultButton(checkButton);
		
		learnPanel.setVisible(true);
		add(learnPanel);
	}
	
	/**
	 * Method setting and displaying buttons
	 */
	private void prepareButtons()
	{
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
		checkButton = new JButton("Check");
		checkButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e)
			{
				eventQueue.add(new WordEvent(WordEvent.CHECK_WORD));
			}
		});
		checkButton.setVisible(true);
		checkButton.setEnabled(false);
		southPanel.add(checkButton);
	
		Box buttonsBox = new Box(BoxLayout.LINE_AXIS);
		knowButton = new JButton("I know");
		knowButton.setVisible(false);
		knowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				eventQueue.add(new WordEvent(WordEvent.DECISION_KNOW));
			}
		});
		repeatButton = new JButton("I want to repeat");
		repeatButton.setVisible(false);
		repeatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				eventQueue.add(new WordEvent(WordEvent.DECISION_REPEAT));
			}
		});
		dontKnowButton = new JButton("I don't know");
		dontKnowButton.setVisible(false);
		dontKnowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				eventQueue.add(new WordEvent(WordEvent.DECISION_NOT_KNOW));
			}
		});
		buttonsBox.add(knowButton);
		buttonsBox.add(repeatButton);
		buttonsBox.add(dontKnowButton);
		southPanel.add(buttonsBox);
		learnPanel.add(southPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Prepares menu to be displayed
	 */
	public void prepareMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add("Change to Edit Mode").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				eventQueue.add(new WordEvent(WordEvent.PREPARE_EDIT));
			}
		});
		fileMenu.add("Change Words Set").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventQueue.add(new WordEvent(WordEvent.PREPARE_LEARN));
			}
		});
		
		fileMenu.add("Close").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
	}
	/**
	 * Method changing buttons mode from guessing to deciding weather user knows previous word
	 */
	public void switchButtons()
	{
		final boolean decision = checkButton.isVisible();
		checkButton.setVisible(!decision);
		knowButton.setVisible(decision);
		repeatButton.setVisible(decision);
		dontKnowButton.setVisible(decision);
		if(decision)
			getRootPane().setDefaultButton(knowButton);
		else
			getRootPane().setDefaultButton(checkButton);
	}
	/**
	 * @return	value of what is inserted into Translation Field
	 */
	public String getTranslation()
	{
		return translationWordField.getText();
	}
	/**
	 * displays the correct answer for current word
	 * @param text correct answer
	 * @param isCorrect	determine whether the answer was good of bad to choose color
	 */
	public void setCorrectWordLabel(final String text, boolean isCorrect)
	{
		correctWordLabel.setText(text);
		if(isCorrect) 
			correctWordLabel.setForeground(Color.green);
		else
			correctWordLabel.setForeground(Color.red);
	}
	/**
	 * sets word in toTranslateWordField, taken from Model
	 */
	public void setWord()
	{
		toTranslateWordField.setText(model.getCurrentWord().getToTranslate());
		translationWordField.setText("");
		correctWordLabel.setText("");
		checkButton.setVisible(true);
		knowButton.setVisible(false);
		repeatButton.setVisible(false);
		dontKnowButton.setVisible(false);
		getRootPane().setDefaultButton(checkButton);
	}
}
