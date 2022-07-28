package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Window extends JFrame
{
	private static final long serialVersionUID = 1L;

	private PuzzlePan puzzlePan;
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu tools = new JMenu("Tools");
	private JMenuItem saveAs = new JMenuItem("Save as..."),
			reset = new JMenuItem("Reset"),
			load =  new JMenuItem("Load save...");
	
	public Window(int w, int h)
	{
		this.setMinimumSize(new Dimension(w, h));
		this.setTitle("Custom Puzzle");
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(w, h));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setJMenuBar(menuBar);
		
		menuBar.add(tools);
		tools.add(load);
		tools.add(saveAs);
		tools.add(reset);
		
		puzzlePan = new PuzzlePan(w, h);
		puzzlePan.loadLastPuzzle();
		
		this.add(puzzlePan, BorderLayout.CENTER);
		
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzlePan = new PuzzlePan(w, h);
				setContentPane(puzzlePan);
				revalidate();
			}
		});
		
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File saveAs = PuzzlePan.openFile();
				if(saveAs != null) {
					String path = saveAs.getAbsolutePath();
					String ext = PuzzlePan.getFileExtension(saveAs);
					if(ext == null || !ext.equalsIgnoreCase("save"))
						path += ".save";
					puzzlePan.save(path);
				}
			}
		});
		
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File loadFile = PuzzlePan.openFile();
				if(loadFile != null) {
					String path = loadFile.getAbsolutePath();
					String ext = PuzzlePan.getFileExtension(loadFile);
					if(ext == null || !ext.equalsIgnoreCase("save"))
						return;
					puzzlePan.loadIn(path);
				}
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				puzzlePan.saveLastPuzzle();
				System.exit(0);
			}
		});

		this.setVisible(true);
	}
	
	public static URL getImageURL(final String path) {
		return Thread.currentThread().getContextClassLoader().getResource(path);
	}

	public static Image getImage(final String path)
	{
		return Toolkit.getDefaultToolkit().getImage(getImageURL(path));
	}

}
