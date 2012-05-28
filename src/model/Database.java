package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Vector;

import model.Word;

/**
 * Class responsible for database connection
 * @author Jakub Borowski
 *
 */
public class Database 
{
	private static final String dbURL = "jdbc:sqlite:words.db";
	private static final int timeout = 30;
	private static Connection conn;
	private static Statement stmt;
	private static int dbSize = 0;
	
	/**
	 * Database constructor creates connection to SQLite database located in 'words.db' file and prepares statement
	 * @throws SQLException
	 */
	public Database() throws SQLException
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
		} 
		catch(ClassNotFoundException ignored) { System.out.println("Couldn't create class");}
		
		
		conn = DriverManager.getConnection(dbURL);
		stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM words");
		try 
		{
			while(rs.next())
			{
				int sResult = rs.getInt("COUNT(*)");
				dbSize = sResult;
			}
		} 
		finally 
		{
			try { rs.close(); } catch (Exception ignore) {}
		}
		try { stmt.close(); } catch (Exception ignore) {}
	}
	
	/**
	 * Method closing DB connection
	 */
	public void closeConnection()
	{
		try
		{
			conn.close();
		}
		catch(Exception ignored)	{}
	}
	/**
	 * This method is used to execute an insert query on database
	 * @param w	Word object to be inserted
	 * @return		true if insert successful, false otherwise
	 * @throws SQLException
	 */
	public boolean insertQuery(final Word w) throws SQLException
	{
		int result;
		String query = "INSERT INTO words VALUES (" + ++dbSize + ",'" + w.getToTranslate() + "','" + w.getTranslation() + "','" + w.getGroup() + "')"; 
		stmt = conn.createStatement();
		
		try
		{
			stmt.setQueryTimeout(timeout);
			result = stmt.executeUpdate(query);
		}
		finally
		{
			try { stmt.close(); } catch(Exception ignored) {}
		}
		return result==0 ? false : true;
	}
	/**
	 * Method executing select query, to get a whole words group list
	 * @param group	Name of the words group to be selected
	 * @return	list of words in certain group
	 * @throws SQLException
	 */
	// baza ma pola |	id	|	word1	|	word2	|	zestaw	|
	public Vector<Word> selectQuery(final String group) throws SQLException
	{
		Vector<Word> wordsList = new Vector<Word>();
		String sMakeSelect = "SELECT * from words WHERE zestaw = '" + group + "'";
		try 
		{
			stmt.setQueryTimeout(timeout);
			ResultSet rs = stmt.executeQuery(sMakeSelect);
			try 
			{
				while(rs.next())
				{
					wordsList.add(new Word(rs.getString("word1"), rs.getString("word2"), rs.getString("zestaw")));
				}
			} 
			finally 
			{
				try { rs.close(); } catch (Exception ignore) {}
			}
		} 
		finally 
		{
			try { stmt.close(); } catch (Exception ignore) {}
		}
		return wordsList;
	}
	/**
	 * Method used to generate groups list for user to select one
	 * @return List of group names
	 * @throws SQLException
	 */
	public Vector<String> selectGroupsQuery() throws SQLException
	{
		Vector<String> groupsList = new Vector<String>();
		String sMakeSelect = "SELECT DISTINCT zestaw from words";
		try 
		{
			stmt.setQueryTimeout(timeout);
			ResultSet rs = stmt.executeQuery(sMakeSelect);
			try 
			{
				while(rs.next())
				{
					groupsList.add(new String(rs.getString("zestaw")));
				}
			} 
			finally 
			{
				try { rs.close(); } catch (Exception ignore) {}
			}
		} 
		finally 
		{
			try { stmt.close(); } catch (Exception ignore) {}
		}
		
		return groupsList;
	}
	/**
	 * Deletes the whole group from database
	 * @param currentGroup	name of group to delete
	 * @throws SQLException
	 */
	public void deleteGroup(final String currentGroup) throws SQLException
	{
		String deleteQuery = "DELETE FROM words WHERE zestaw = '" + currentGroup + "'";
		try 
		{
			stmt.setQueryTimeout(timeout);
			int rs = stmt.executeUpdate(deleteQuery);
		} 
		finally 
		{
			try { stmt.close(); } catch (Exception ignore) {}
		}
	}
	
	/**
	 * Overwritten method finalize() to close connection on deletion of database object
	 * @throws Throwable
	 */
	protected void finalize() throws Throwable
	{
		this.closeConnection();
	}
}