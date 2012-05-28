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

public class EditFrame extends JFrame implements Frames 
{
	private final BlockingQueue<WordEvent> eventQueue;
	private final Model model;
	private JPanel mainPanel;
	private JButton saveButton, cancelButton, addRowButton, removeRowButton;
	DefaultTableModel tableModel;
	private JTable table;
	
	public EditFrame(final BlockingQueue<WordEvent> eventQueue, final Model model)
	{
		this.eventQueue = eventQueue;
		this.model = model;
		prepareMenu();
		prepareFrame();
	}
	
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		fileMenu.add("Close").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
	}
	
	public void prepareFrame()
	{
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
		northPanel.add(addRowButton);
		northPanel.add(removeRowButton);
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
		
		getRootPane().setDefaultButton(cancelButton);
		mainPanel.setVisible(true);
		add(mainPanel);
	}
	
	public void setGroup(final Vector<Word> words, boolean newSet)
	{
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		JLabel groupName = new JLabel(model.getCurrentGroup());
		centerPanel.add(groupName);
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
	}
	
	public void addRow(String word, String translation)
	{
		tableModel.insertRow(table.getRowCount(), new Object[]{word, translation});
	}
	
	public void removeRows()
	{
		int[] rows = table.getSelectedRows();
		for(int i=0;i<rows.length;i++)
			tableModel.removeRow(rows[i]-i);	
	}
}
