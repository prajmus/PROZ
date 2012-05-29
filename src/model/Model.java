package model;

import java.sql.SQLException;
import java.util.Vector;

import model.Database;
import model.Word;

/**
 * Model class of the MVC design pattern.
 * @author Jakub Borowski
 *
 */
public class Model
{
	private Vector<Word> words = null;
	private Vector<Word> repeats = null;
	private Word currentWord;
	private String currentGroup;
	private int currentIndex;
	private int goodWords;
	private int guessedWords;
	private Database db = null;
	/**
	 * Class constructor. Creates database connection.
	 */
	public Model()
	{
		words = new Vector<Word>();
		repeats = new Vector<Word>();
		try
		{
			db = new Database();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Gets list of words from certain group from database.
	 * @param group name of desired words group
	 * @return vector of words
	 */
	public Vector<Word> getListOfWords(final String group)
	{
		if(words != null)
			words.clear();
		try
		{
			words = db.selectQuery(group);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		currentGroup = group;
		repeats.clear();
		currentWord = words.get(0);
		currentIndex = goodWords = guessedWords = 0;
		return words;
	}
	/**
	 * gets list of groups
	 * @return vector of group names
	 */
	public Vector<String> getListOfGroups()
	{
		Vector<String> groupsList = new Vector<String>();
		try
		{
			groupsList = db.selectGroupsQuery();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return groupsList;
	}
	/**
	 * gets currently displayed word
	 * @return current word
	 */
	public Word getCurrentWord()
	{
		return currentWord;
	}
	/**
	 * gets currently displayed group
	 * @return current group
	 */
	public String getCurrentGroup()
	{
		return currentGroup;
	}
	/**
	 * sets the current group to new value
	 * @param currentGroup new current group
	 */
	public void setCurrentGroup(final String currentGroup)
	{
		this.currentGroup = currentGroup;
	}
	/**
	 * increments number of guesses and number of good guesses
	 */
	public void goodGuess()
	{
		guessedWords++;
		goodWords++;
	}
	/**
	 * increments number of guesses only
	 */
	public void badGuess()
	{
		guessedWords++;
	}
	/**
	 * get number of guessed words in current group
	 * @return number of guessed words
	 */
	public int getGuessedWords()
	{
		return guessedWords;
	}
	/**
	 * gets number of correct guesses in current group
	 * @return number of correct guesses
	 */
	public int getGoodWords()
	{
		return goodWords;
	}
	/**
	 * removes current word from words list
	 */
	public void removeCurrent()
	{
		words.remove(currentIndex);
		currentIndex--;
	}
	/**
	 * adds word to repetition list
	 */
	public void addToRepeat()
	{
		repeats.add(currentWord);
	}
	/**
	 * prepares the next word to be learned
	 * @return true if next word is set, false if no words left to learn
	 */
	public boolean getNext()
	{
		currentIndex++;
		if(words.size() > 0)
		{
			if(currentIndex < words.size())
			{
				currentWord = words.get(currentIndex);
				return true;
			}
			else
			{
				currentIndex = 0;
				currentWord = words.get(currentIndex);
				return true;
			}
		}
		else
		{
			if(repeats.size() > 0)
			{
				words = repeats;
				currentIndex = 0;
				currentWord = words.get(currentIndex);
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	/**
	 * deletes current group from database
	 */
	public void deleteGroup()
	{
		try
		{
			db.deleteGroup(currentGroup);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * saves changes to edited group in database.
	 * @param words vector of edited words
	 */
	public void editGroup(Vector<Vector> words)
	{
		deleteGroup();
		for(int i = 0;i<words.size();i++)
		{
			try 
			{
				db.insertQuery(new Word( (String)words.elementAt(i).elementAt(0), (String)words.elementAt(i).elementAt(1), currentGroup));
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
}
