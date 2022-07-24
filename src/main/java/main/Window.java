package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
	private JMenuItem save = new JMenuItem("Save"),
			reset = new JMenuItem("Reset"),
			load =  new JMenuItem("Load");
	
	public Window(int w, int h)
	{
		super();
		this.setMinimumSize(new Dimension(w, h));
		this.setTitle("puzzleGame");
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(w, h));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setJMenuBar(menuBar);
		
		menuBar.add(tools);
		tools.add(reset);
		tools.add(save);
		tools.add(load);
		
		puzzlePan = new PuzzlePan(w, h);
		
		this.add(puzzlePan, BorderLayout.CENTER);
		
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzlePan = new PuzzlePan(w, h);
				setContentPane(puzzlePan);
				revalidate();
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzlePan.getPuzzleContainer().save("puzzle1.save");
			}
		});
		
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzlePan.getPuzzleContainer().load("puzzle1.save");
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				//preset.savePreset(self);
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
