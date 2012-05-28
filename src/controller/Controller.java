package controller;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;

import src.model.Model;
import src.model.Word;
import src.view.View;
import src.controller.WordEvent;


/**
 * Controller class of the MVC design pattern
 * @author Jakub Borowski
 *
 */
public class Controller
{
	private final Model model;	/** Model reference */
	private final View view;		/** View reference */
	private final BlockingQueue<WordEvent> eventQueue;	/** BlokingQueue reference */
	Vector<String> groupsList = null;	/** List of groups to be send to View */
	
	/**
	 * Constructor assigns the reference to BlockingQueue, Model and View
	 * @param eventQueue BlockingQueue reference
	 * @param model	Model reference
	 * @param view		View reference
	 */
	public Controller(final BlockingQueue<WordEvent> eventQueue, final Model model, final View view)
	{
		this.eventQueue = eventQueue;
		this.model = model;
		this.view = view;
	}
	/**
	 * Gets events from BlockingQueue and does adequate actions
	 */
	public void start() {
		while(true)
		{
			try
			{
				WordEvent wordEvent = eventQueue.take();
				switch (wordEvent.getType())
				{
					case WordEvent.CHECK_WORD:
						checkWord();
						break;
					case WordEvent.PREPARE_LEARN:
						prepareLearnMode();
						break;
					case WordEvent.PREPARE_EDIT:
						prepareEditMode();
						break;
					case WordEvent.DECISION_KNOW:
						removeWord();
						break;
					case WordEvent.DECISION_REPEAT:
						addToRepeat();
						removeWord();
						break;
					case WordEvent.DECISION_NOT_KNOW:
						dontKnowButtonAction();
						break;
					case WordEvent.ADD_ROW:
						addRow();
						break;
					case WordEvent.REMOVE_ROWS:
						removeRows();
						break;
					default:
						System.exit(0);
			}
			} catch(Exception e)
			{ 
				e.printStackTrace();
			}
		}
	}
	/**
	 * Checks if the inserted translation is correct
	 */
	private void checkWord()
	{
		String toTranslate = model.getCurrentWord().getTranslation();
		String translation = view.getLearnFrame().getTranslation();
		
		if(toTranslate.toLowerCase().equals(translation.toLowerCase()))
		{
			view.getLearnFrame().setCorrectWordLabel(toTranslate, true);
			model.goodGuess();
		}
		else
		{
			view.getLearnFrame().setCorrectWordLabel(toTranslate, false);
			model.badGuess();
		}
		
		view.getLearnFrame().switchButtons();
	}
	/**
	 * Prepares learning mode: loads selected group, and first word, sets the View
	 */
	private void prepareLearnMode()
	{
		groupsList = model.getListOfGroups();
		model.getListOfWords(view.displayChooseDialog(groupsList, "Which group do you want to study?"));
		view.getLearnFrame().setWord();
		view.prepareLearnMode();
	}
	/**
	 * Prepares editing mode: loads selected group or creates new, sets the View
	 */
	private void prepareEditMode()
	{
		groupsList = model.getListOfGroups();
		groupsList.add("New...");
		String choice = view.displayChooseDialog(groupsList, "Which group do you want to edit?");
		if(choice.equals("New..."))
		{
			String name = (String)JOptionPane.showInputDialog(view.getMainFrame(), "Enter new set name: ", null, JOptionPane.PLAIN_MESSAGE, null, null, null);
			model.setCurrentGroup(name);
			view.getEditFrame().setGroup(new Vector<Word>(), true);
		}
		else
		{
			Vector<Word> words = model.getListOfWords(choice);
			view.getEditFrame().setGroup(words, false);
		}
		view.prepareEditMode();
	}
	/**
	 * Remove word from list of current words to be learned ex. when user stated that he knows it. Loads new word, or ends learning if non.
	 */
	private void removeWord()
	{
		model.removeCurrent();
		if(model.getNext())
		{
			view.getLearnFrame().setWord();
		}
		else
		{
			groupsList = model.getListOfGroups();
			model.getListOfWords(view.displayChooseDialog(groupsList, "You got "+ model.getGoodWords() + " out of " + model.getGuessedWords() + 
					" right. <br /> Which group do you want to study next?"));
			view.getLearnFrame().setWord();
			view.prepareLearnMode();
		}
	}
	/**
	 * Selects new word, keeping previous one on the list to learn. Ends learning if no word to set.
	 */
	private void dontKnowButtonAction()
	{
		addToRepeat();
		if(model.getNext())
		{
			view.getLearnFrame().setWord();
		}
		else
		{
			groupsList = model.getListOfGroups();
			model.getListOfWords(view.displayChooseDialog(groupsList, "You got "+ model.getGoodWords() + " out of " + model.getGuessedWords() + 
					" right. <br /> Which group do you want to study next?"));
			view.getLearnFrame().setWord();
			view.prepareLearnMode();
		}
	}
	/**
	 * Ads word to repetition
	 */
	private void addToRepeat()
	{
		model.addToRepeat();
	}
	/**
	 * Ads new row in edit mode
	 */
	private void addRow()
	{
		String word = (String)JOptionPane.showInputDialog(view.getEditFrame(), "Enter word: ", null, JOptionPane.PLAIN_MESSAGE, null, null, null);
		String translation = (String)JOptionPane.showInputDialog(view.getEditFrame(), "Enter translation: ", null, JOptionPane.PLAIN_MESSAGE, null, null, null);
		view.getEditFrame().addRow(word, translation);
	}
	/**
	 * Removes selected words in edit mode
	 */
	private void removeRows()
	{
		view.getEditFrame().removeRows();
	}
}
