package view;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.Model;
import controller.WordEvent;

/**
 * View class of the MVC design pattern. Responsible for displaying specific frame
 * @author Jakub Borowski
 *
 */
public class View 
{
	private final BlockingQueue<WordEvent> eventQueue;
	private final Model model;
	private MainFrame mainWindow;
	private LearnFrame learnWindow;
	private EditFrame editWindow;
	/**
	 * Class constructor.
	 * @param eventQueue	reference to BlockingQueue, to send actions to Controller
	 * @param model	reference to model
	 */
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
	/**
	 * Displays choose mode frame
	 */
	public void prepareMainMode()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainWindow.setVisible(true);
				editWindow.setVisible(false);
				learnWindow.setVisible(false);
			}
		});
	}
	/**
	 * Displays learning mode frame
	 */
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
	/**
	 * Displays editing mode frame
	 */
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
	/**
	 * @return learn mode frame
	 */
	public LearnFrame getLearnFrame()
	{
		return learnWindow;
	}
	/**
	 * @return edit mode frame
	 */
	public EditFrame getEditFrame()
	{
		return editWindow;
	}
	/**
	 * @return choose mode frame
	 */
	public MainFrame getMainFrame()
	{
		return mainWindow;
	}
	/**
	 * Displays dialog asking user to choose from existing word groups
	 * @param groupsList	list of groups to choose from
	 * @param message	message to be displayed in dialog
	 * @return users decision
	 */
	public String displayChooseDialog(List<String> groupsList, String message)
	{
		String s = (String)JOptionPane.showInputDialog(mainWindow, message, "Choose group:", JOptionPane.PLAIN_MESSAGE, null, groupsList.toArray(), null);
		return s;
	}
}
