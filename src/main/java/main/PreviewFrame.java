package main;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class PreviewFrame extends JFrame
{

	public PreviewFrame(BufferedImage img)
	{
        this.setSize(PuzzlePan.getNewDimension(img, 1025, 650));
		this.setSize(getWidth(), getHeight() + 75);
		this.setTitle("Puzzle preview");
		//this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setContentPane(PuzzlePan.getTempPan(new Preview(img, 950, 600, false)));

		this.setVisible(true);
	}



}
