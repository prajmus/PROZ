package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import controller.WordEvent;

import model.Model;

/**
 * Class extending JFrame and implementing Frames. Displays welcome window, allowing to chose mode.
 * @author Jakub Borowski
 *
 */
public class MainFrame extends JFrame implements Frames 
{
	private final BlockingQueue<WordEvent> eventQueue;
	private final Model model;
	
	private JButton editModeButton;
	private JButton learnModeButton;
	private JLabel chooseModeLabel;
	
	/**
	 * Class constructor.
	 * @param eventQueue reference to BlockingQueue, to send actions to Controller
	 * @param model reference to model
	 */
	public MainFrame(BlockingQueue<WordEvent> eventQueue, Model model)
	{
		this.eventQueue = eventQueue;
		this.model = model;
		prepareMenu();
		prepareFrame();
	}
	/**
	 * prepares top menu to be displayed
	 */
	public void prepareMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add("Close").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
	}
	/**
	 * prepares the whole frame to be displayed
	 */
	public void prepareFrame()
	{
		setLocation(200, 100);
		setTitle("Words learner");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,200);
		setResizable(false);
		setVisible(true);
		JPanel mainPanel = new JPanel(new GridLayout(3, 3));
		
		learnModeButton = new JButton("Learn mode");
		learnModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e)
			{
				eventQueue.add(new WordEvent(WordEvent.PREPARE_LEARN));
			}
		});
		editModeButton = new JButton("Edit mode");
		editModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventQueue.add(new WordEvent(WordEvent.PREPARE_EDIT));
			}
		});
		chooseModeLabel = new JLabel("Choose mode: ");
		mainPanel.add(chooseModeLabel);
		mainPanel.add(learnModeButton);
		mainPanel.add(editModeButton);
		mainPanel.setVisible(true);
		add(mainPanel);
	}
	
}
