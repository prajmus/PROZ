package model;

public class Word {
	private String toTranslate;
	private String translation;
	private String group;
	
	Word()
	{
		toTranslate = new String();
		translation = new String();
		group = new String();
	}
	public Word(final String word, final String translation, final String group)
	{
		this.toTranslate = new String(word);
		this.translation = new String(translation);
		this.group = new String(group);
	}
	
	
	public final String getToTranslate() {
		return toTranslate;
	}
	public void setWord(final String toTranslate) {
		this.toTranslate = toTranslate;
	}
	public final String getTranslation() {
		return translation;
	}
	public void setTranslation(final String translation) {
		this.translation = translation;
	}
	public final String getGroup() {
		return group;
	}
	public void setGroup(final String group) {
		this.group = group;
	}
}
