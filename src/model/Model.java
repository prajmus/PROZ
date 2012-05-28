package model;

import java.sql.SQLException;
import java.util.Vector;

import model.Database;
import model.Word;

public class Model {
	private Vector<Word> words = null;
	private Vector<Word> repeats = null;
	private Word currentWord;
	private String currentGroup;
	private int currentIndex;
	private int goodWords;
	private int guessedWords;
	private Database db = null;
	
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
			//TODO handle exception
		}
	}
	
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
			//TODO handle the exception
		}
		currentGroup = group;
		repeats.clear();
		currentWord = words.get(0);
		currentIndex = goodWords = guessedWords = 0;
		return words;
	}
	
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
	
	public final boolean checkCorrect(final String toCheck)
	{
		return currentWord.getTranslation().equals(toCheck);
	}
	
	public Word getCurrentWord()
	{
		return currentWord;
	}
	
	public String getCurrentGroup()
	{
		return currentGroup;
	}
	public void setCurrentGroup(final String currentGroup)
	{
		this.currentGroup = currentGroup;
	}
	
	public void goodGuess()
	{
		guessedWords++;
		goodWords++;
	}
	
	public void badGuess()
	{
		guessedWords++;
	}
	
	public int getGuessedWords()
	{
		return guessedWords;
	}
	
	public int getGoodWords()
	{
		return goodWords;
	}
	
	public void removeCurrent()
	{
		words.remove(currentIndex);
		currentIndex--;
	}
	public void addToRepeat()
	{
		repeats.add(currentWord);
	}
	public boolean getNext()
	{
		//TODO figure out how to randomize the words taken from database
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
	
	public void editGroup(Vector<Vector> words)
	{
		try {
			db.deleteGroup(currentGroup);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0;i<words.size();i++)
		{
			try {
				db.insertQuery(new Word( (String)words.elementAt(i).elementAt(0), (String)words.elementAt(i).elementAt(1), currentGroup));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
