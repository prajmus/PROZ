package model;

/**
 * Class representing every single word to be learned: its original form, translation, and name of the group it belongs to.
 * @author Jakub Borowski
 *
 */
public class Word 
{
	private String toTranslate;
	private String translation;
	private String group;
	/**
	 * Default class constructor
	 */
	public Word()
	{
		toTranslate = new String();
		translation = new String();
		group = new String();
	}
	/**
	 * Class constructor
	 * @param word	word's original form
	 * @param translation word's translation
	 * @param group	word's group (where it belongs)
	 */
	public Word(final String word, final String translation, final String group)
	{
		this.toTranslate = new String(word);
		this.translation = new String(translation);
		this.group = new String(group);
	}
	
	/**
	 * Gets word original form
	 * @return original form
	 */
	public final String getToTranslate() 
	{
		return toTranslate;
	}
	/**
	 * sets word's original form
	 * @param toTranslate new word's original form
	 */
	public void setToTranslate(final String toTranslate) 
	{
		this.toTranslate = toTranslate;
	}
	/**
	 * gets word's translation
	 * @return translation
	 */
	public final String getTranslation() 
	{
		return translation;
	}
	/**
	 * sets word's translation
	 * @param translation word's new translation
	 */
	public void setTranslation(final String translation) 
	{
		this.translation = translation;
	}
	/**
	 * gets word's group
	 * @return word's group
	 */
	public final String getGroup()
	{
		return group;
	}
	/**
	 * sets word's group
	 * @param group name of the new group
	 */
	public void setGroup(final String group) 
	{
		this.group = group;
	}
}
