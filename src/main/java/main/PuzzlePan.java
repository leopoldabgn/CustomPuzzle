package main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class PuzzlePan extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private PuzzleContainer puzzleContainer;
	private Preview preview;
	private JButton scrambleBtn = new JButton("Scramble"),
					resetBtn = new JButton("Reset");
	private JSlider difficulty;

	public PuzzlePan(int w, int h)
	{
		final int c = 25, l=25;
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
		this.preview = new Preview(img, w/3, h);
		this.puzzleContainer = new PuzzleContainer(img, c, l);
		
        this.difficulty = new JSlider(0, 100, 25);
        difficulty.setOpaque(false);
		difficulty.setMinorTickSpacing(10);
        difficulty.setMajorTickSpacing(25);
        difficulty.setPaintLabels(true);

		resetBtn.addActionListener(e -> {
			puzzleContainer.changeDimension(difficulty.getValue(),
											difficulty.getValue());
		});

		scrambleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int c = difficulty.getValue(), l = difficulty.getValue();
				puzzleContainer.changeDimension(c, l);
				puzzleContainer.scramble(c*l*2);
			}
		});
		
		JPanel pan = new JPanel();
		pan.setLayout(new GridLayout(2, 1));
		pan.add(getTempPan(preview));
		JPanel pan2 = new JPanel();
		pan2.setOpaque(false);
		pan2.setLayout(new BoxLayout(pan2, BoxLayout.PAGE_AXIS));
		pan2.add(getTempPan(difficulty));
		pan2.add(getTempPan(resetBtn, scrambleBtn));
		pan.add(pan2);

		this.setLayout(new GridLayout(1, 2));
		this.add(pan);
		this.add(puzzleContainer);
	}
	
	public static JPanel getTempPan(Component ... comp) {
		JPanel tmp = new JPanel();
		tmp.setOpaque(false);
		for(Component c : comp)
			tmp.add(c);

		return tmp;
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
    
	public void saveLastPuzzle()
	{
		puzzleContainer.saveLastPuzzle();
	}

	public void loadLastPuzzle()
	{
		
		puzzleContainer.loadLastPuzzle();
		revalidate();
		repaint();
	}

	public PuzzleContainer getPuzzleContainer()
	{
		return puzzleContainer;
	}
	
}
