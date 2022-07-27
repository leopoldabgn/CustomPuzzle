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
import javax.swing.border.EmptyBorder;

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
		String imgPath = "paysage.jpg";
		BufferedImage img = null;
		try {
			img = ImageIO.read(Window.getImageURL(imgPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(img == null)
			return;
		
        this.difficulty = new JSlider(0, 100, 15);
        difficulty.setOpaque(false);
		difficulty.setMinorTickSpacing(10);
        difficulty.setMajorTickSpacing(25);
        difficulty.setPaintLabels(true);
		
		final int c = difficulty.getValue(), l = difficulty.getValue();
		img = resize(img, 625, 550);
		this.preview = new Preview(img, 300, 400);
		this.puzzleContainer = new PuzzleContainer(img, c, l);

		resetBtn.addActionListener(e -> {
			int a = difficulty.getValue();
			a = a <= 1 ? 2 : a;
			int b = a;
			puzzleContainer.changeDimension(a, b);
		});

		scrambleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int c = difficulty.getValue();
				c = c <= 1 ? 2 : c;
				int l = c;
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

		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(getTempPan(10, pan));
		this.add(getTempPan(10, puzzleContainer));
	}
	
	public static JPanel getTempPan(int padding, Component ... comp)
	{
		JPanel pan = getTempPan(comp);
		pan.setBorder(new EmptyBorder(padding, padding, padding, padding));
		return pan;
	}

	public static JPanel getTempPan(Component ... comp)
	{
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
