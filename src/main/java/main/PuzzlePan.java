package main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

public class PuzzlePan extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private PuzzleContainer puzzleContainer;
	private Preview preview;
	private JButton scrambleBtn = new JButton("Scramble"),
					resetBtn = new JButton("Reset"),
					importImg = new JButton("Import image");
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
		
		int c = difficulty.getValue(), l = difficulty.getValue();
		this.preview = new Preview(img, 300, 200, true);
		this.puzzleContainer = new PuzzleContainer(new ImageIcon(img), c, l);

		importImg.addActionListener(e -> {
			changeImage(openFile());
		});

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
		JPanel pan2 = new JPanel();
		pan2.setOpaque(false);
		pan2.setLayout(new BoxLayout(pan2, BoxLayout.PAGE_AXIS));
		pan2.add(getTempPan(preview));
		pan2.add(getTempPan(new JLabel("(Click on me)")));
		pan.add(pan2);
		pan2 = new JPanel();
		pan2.setOpaque(false);
		pan2.setLayout(new BoxLayout(pan2, BoxLayout.PAGE_AXIS));
		pan2.add(getTempPan(difficulty));
		pan2.add(getTempPan(importImg, resetBtn, scrambleBtn));
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
    
	public static File openFileChooser(int fileSelectionMode)
	{
		JFileChooser choice = new JFileChooser();
		choice.setFileSelectionMode(fileSelectionMode);
		int var = choice.showOpenDialog(null);
		if(var == JFileChooser.APPROVE_OPTION)
			return choice.getSelectedFile();
		return null;
	}

	public static File openFile()
	{
		return openFileChooser(JFileChooser.FILES_ONLY);
	}

	public static File openFolder()
	{
		return openFileChooser(JFileChooser.DIRECTORIES_ONLY);
	}

	public static String getFileExtension(File f)
	{
		try {
			String ext = f.getName().substring(f.getName().lastIndexOf(".")+1).toUpperCase();
			return ext;
		} catch(Exception e) {
			return null;
		}
	}
	
	public static boolean isImg(File f)
	{
		if(f.isFile())
		{
			String ext = getFileExtension(f).toUpperCase();
			if(ext.equals("JPG") || ext.equals("JPEG") || ext.equals("PNG")) // || ext.equals("ICO"))
					return true;
		}
		return false;
	}

	public void saveLastPuzzle()
	{
		puzzleContainer.saveLastPuzzle();
	}

	public void loadLastPuzzle()
	{
		loadIn(PuzzleContainer.LAST_PUZZLE_NAME);
	}

	public void changeImage(File image) 
	{
		if(image == null || !isImg(image))
			return;
		ImageIcon imgIcon = new ImageIcon(image.getAbsolutePath());
		puzzleContainer.changeImage(imgIcon);
		preview.changeImage(resize(PuzzleContainer.toBufferedImg(imgIcon), 625, 550));
		revalidate();
		repaint();
	}

	public PuzzleContainer getPuzzleContainer()
	{
		return puzzleContainer;
	}
	
	public void save(String name)
	{
		puzzleContainer.save(name);
	}

	public void loadIn(String name)
	{
		try {
			this.puzzleContainer.loadIn(name);
			this.preview.changeImage(puzzleContainer.getImage());
			revalidate();
			repaint();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
