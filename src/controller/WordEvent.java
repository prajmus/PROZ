package controller;


/**
 * Class representing events that can occur in view.
 * @author Jakub Borowski
 *
 */
public class WordEvent 
{
	/**
	 * constant sent from view to controller to check if the inserted word is correct
	 */
	public static final int CHECK_WORD = 1;
	/**
	 * constant sent from view to controller to prepare learning mode
	 */
	public static final int PREPARE_LEARN = 2;
	/**
	 * constant sent form view to controller to prepare editing mode
	 */
	public static final int PREPARE_EDIT = 3;
	/**
	 * constant sent from view to controller to perform action connected to 'I Know' button
	 */
	public static final int DECISION_KNOW = 4;
	/**
	 * constant sent from view to controller to perform action connected to 'I dont know' button
	 */
	public static final int DECISION_NOT_KNOW = 5;
	/**
	 * constant sent from view to controller to perform action connected to 'I want to repeat' button
	 */
	public static final int DECISION_REPEAT = 6;
	/**
	 * constant sent from view to controller to add rows to table
	 */
	public static final int ADD_ROW = 7;
	/**
	 * constant sent from view to controller to remove selected rows from table
	 */
	public static final int REMOVE_ROWS = 8;
	/**
	 * constatnt sent from view to controller to delete the edited group
	 */
	public static final int DELETE_GROUP = 9;
	private int type;
	/**
	 * default constructor
	 */
	public WordEvent()
	{
		type = 0;
	}
	/**
	 * Constructor creating new event
	 * @param type	event type
	 */
	public WordEvent(final int type)
	{
		this.type = type;
	}
	/**
	 * type setter
	 * @param type	event type
	 */
	public void setType(final int type)
	{
		this.type = type;
	}
	/**
	 * type getter
	 * @return	event type
	 */
	public final int getType()
	{
		return this.type;
	}
}
