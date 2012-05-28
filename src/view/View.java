package view;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.Model;
import controller.WordEvent;


public class View {
	@SuppressWarnings("unused")
	private final BlockingQueue<WordEvent> eventQueue;
	@SuppressWarnings("unused")
	private final Model model;
	private MainFrame mainWindow;
	private LearnFrame learnWindow;
	private EditFrame editWindow;
		
	public View(final BlockingQueue<WordEvent> eventQueue, final Model model)
	{
		this.eventQueue = eventQueue;
		this.model = model;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				editWindow = new EditFrame(eventQueue, model);
				learnWindow = new LearnFrame(eventQueue, model);
				mainWindow = new MainFrame(eventQueue, model);
				mainWindow.setVisible(true);
			}
		});
	}
		
	public void prepareLearnMode()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainWindow.setVisible(false);
				editWindow.setVisible(false);
				learnWindow.setVisible(true);
			}
		});
	}
	
	public void prepareEditMode()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainWindow.setVisible(false);
				learnWindow.setVisible(false);
				editWindow.setVisible(true);
			}
		});
	}
	
	public LearnFrame getLearnFrame()
	{
		return learnWindow;
	}
	
	public EditFrame getEditFrame()
	{
		return editWindow;
	}
	
	public MainFrame getMainFrame()
	{
		return mainWindow;
	}
	public String displayChooseDialog(List<String> groupsList, String message)
	{
		String s = (String)JOptionPane.showInputDialog(mainWindow, message, "Choose group:", JOptionPane.PLAIN_MESSAGE, null, groupsList.toArray(), null);
		return s;
	}
}
