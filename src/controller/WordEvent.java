package controller;

public class WordEvent {
	public static final int CHECK_WORD = 1;
	public static final int PREPARE_LEARN = 2;
	public static final int PREPARE_EDIT = 3;
	public static final int DECISION_KNOW = 4;
	public static final int DECISION_NOT_KNOW = 5;
	public static final int DECISION_REPEAT = 6;
	public static final int GROUP_END = 7;
	public static final int ADD_ROW = 8;
	public static final int REMOVE_ROWS = 9;
	private int type;
	
	public WordEvent()
	{
		type = 0;
	}
	public WordEvent(final int type)
	{
		this.type = type;
	}
	public void setType(final int type)
	{
		this.type = type;
	}
	
	public final int getType()
	{
		return this.type;
	}
}
