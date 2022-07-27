package main;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class PreviewFrame extends JFrame
{

	public PreviewFrame(BufferedImage img)
	{
		this.setMinimumSize(PuzzlePan.getNewDimension(img, 1025, 650));
        this.setMaximumSize(getMinimumSize());
        this.setSize(getMinimumSize());
		this.setTitle("Puzzle preview");
		//this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setContentPane(PuzzlePan.getTempPan(new Preview(img, 1025, 650, false)));

		this.setVisible(true);
	}



}
