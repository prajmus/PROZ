package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import model.Model;
import model.Word;

import controller.WordEvent;


/**
 * Class extending JFrame and implementing Frames. It displays the window responsible for editing data.
 * @author Jakub Borowski
 *
 */
public class EditFrame extends JFrame implements Frames 
{
	private final BlockingQueue<WordEvent> eventQueue;
	private final Model model;
	private JPanel mainPanel;
	private JButton saveButton, cancelButton, addRowButton, removeRowButton, deleteButton;
	private DefaultTableModel tableModel;
	private JTable table;
	
	/**
	 * Class constructor.
	 * @param eventQueue	reference to BlockingQueue, to send actions to Controller
	 * @param model reference to model
	 */
	public EditFrame(final BlockingQueue<WordEvent> eventQueue, final Model model)
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
		fileMenu.add("Change to Learn Mode").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					eventQueue.put(new WordEvent(WordEvent.PREPARE_LEARN));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		fileMenu.add("Change Words Set").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					eventQueue.put(new WordEvent(WordEvent.PREPARE_EDIT));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
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
		setSize(400,400);
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		//  PREPARATION OF THE NORTH PANEL
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
		addRowButton = new JButton("+");
		addRowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				eventQueue.add(new WordEvent(WordEvent.ADD_ROW));
			}
		});
		removeRowButton = new JButton("-");
		removeRowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventQueue.add(new WordEvent(WordEvent.REMOVE_ROWS));
			}
		});
		deleteButton = new JButton("del");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				eventQueue.add(new WordEvent(WordEvent.DELETE_GROUP));
			}
		});
		
		northPanel.add(addRowButton);
		northPanel.add(removeRowButton);
		northPanel.add(deleteButton);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		// NOTRH PANEL END
		
		// PREPARATION OF THE SOUTH PANEL
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
		saveButton = new JButton("Save changes");
		saveButton.setEnabled(false);
		saveButton.setVisible(true);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread myThread = new Thread(new Runnable() {
					public void run() {
						model.editGroup(tableModel.getDataVector());
					}
				});
				myThread.start();
				saveButton.setEnabled(false);
			}
		});
		cancelButton = new JButton("Cancel changes");
		cancelButton.setEnabled(true);
		cancelButton.setVisible(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventQueue.add(new WordEvent(WordEvent.PREPARE_EDIT));
			}
		});
		
		southPanel.add(saveButton);
		southPanel.add(cancelButton);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		// SOUTH PANEL END
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		JLabel groupName = new JLabel(model.getCurrentGroup());
		centerPanel.add(groupName);
		Object[] columns = {"Word", "Translation"};
		tableModel = new DefaultTableModel(5, 2);
		tableModel.setColumnIdentifiers(columns);
		
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		tableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent arg0) {
				getRootPane().setDefaultButton(saveButton);
				saveButton.setEnabled(true);
			}
		});
		centerPanel.add(scrollPane);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		getRootPane().setDefaultButton(cancelButton);
		mainPanel.setVisible(true);
		add(mainPanel);
	}
	/**
	 * Fills JTable with selected group
	 * @param words	data to be displayed in table
	 * @param newSet	true if creating new set, false otherwise
	 */
	public void setGroup(final Vector<Word> words, boolean newSet)
	{
		Object[] columns = {"Word", "Translation"};
		if(newSet)
		{
			tableModel = new DefaultTableModel(5, 2);
			tableModel.setColumnIdentifiers(columns);
		}
		else
		{
			Object[][] rows = new Object[words.size()][2];
			int i=0;
			for (Word word : words)
			{
				rows[i][0] = word.getToTranslate();
				rows[i][1] = word.getTranslation();
				i++;
			}
			tableModel = new DefaultTableModel(rows, columns);
		}
		//table = new JTable(tableModel);
		table.setModel(tableModel);
		tableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent arg0) {
				getRootPane().setDefaultButton(saveButton);
				saveButton.setEnabled(true);
			}
		});
	}
	/**
	 * Adds row to table.
	 * @param word	toTranslate part of the word
	 * @param translation	translation of the word
	 */
	public void addRow(String word, String translation)
	{
		tableModel.insertRow(table.getRowCount(), new Object[]{word, translation});
	}
	/**
	 * Removes all selected rows from table.
	 */
	public void removeRows()
	{
		int[] rows = table.getSelectedRows();
		for(int i=0;i<rows.length;i++)
			tableModel.removeRow(rows[i]-i);	
	}
}
