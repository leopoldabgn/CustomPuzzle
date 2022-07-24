package main;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PuzzlePan extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private PuzzleContainer puzzleContainer;
	private Preview preview;
	private JButton scrambleBtn = new JButton("Scramble !");
	
	public PuzzlePan(int w, int h)
	{
		int rand = 1, c = 25, l=25; //(int)(Math.random()*2);
		String imgPath = "paysage.jpg";
		BufferedImage img = null;
		try {
			img = ImageIO.read(Window.getImageURL(imgPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(img == null)
			return;
		img = resize(img, w-w/4, h-h/6);
		this.preview = new Preview(img, w/4, h);
		this.puzzleContainer = new PuzzleContainer(img, c, l);
		
		scrambleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzleContainer.scramble(c*l*2);
			}
		});
		
		GridLayout gL = new GridLayout(2, 1);
		JPanel pan = new JPanel();
		pan.setLayout(gL);
		pan.add(preview);
		pan.add(scrambleBtn);
		this.add(pan);
		this.add(puzzleContainer);
	}
	
    public static BufferedImage resize(BufferedImage inputImage, int scaledWidth, int scaledHeight)
    {
    	Dimension dim = getNewDimension(inputImage, scaledWidth, scaledHeight);
    	scaledWidth = (int)dim.getWidth();
   		scaledHeight = (int)dim.getHeight();
        BufferedImage outputImage = new BufferedImage(scaledWidth,
	    scaledHeight, inputImage.getType());
	 
	    Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
	
	    return outputImage;
    }
	
	public static Dimension getNewDimension(BufferedImage pic, int w_max, int h_max)
	{
		int w, h;
		if(pic.getWidth(null) > w_max)
			w = w_max;
		else
			w = pic.getWidth(null);
		
		h = pic.getHeight(null);
		float coeff = (float)w / (float)pic.getWidth(null);
		h *= coeff;
		
		if(h > h_max)
		{
			coeff = (float)h_max / (float)h;
			h = h_max;
			w *= coeff;
		}	
		return new Dimension(w, h);
	}
    
	public PuzzleContainer getPuzzleContainer()
	{
		return puzzleContainer;
	}
	
}
