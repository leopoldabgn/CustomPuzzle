package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Preview extends JPanel
{
	private static final long serialVersionUID = 1L;

	private BufferedImage img;
	private int w, h;

	public Preview(BufferedImage img, int w, int h, boolean clickable)
	{
		this.w = w;
		this.h = h;
		changeImage(img);
		if(clickable)
		{
			this.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
					new PreviewFrame(getImage());
				}

			});
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(img == null)
			return;
		g.drawImage(img, 0, 0, (int)getPreferredSize().getWidth(), (int)getPreferredSize().getHeight(), null);
	}

	public void changeImage(BufferedImage img)
	{
		this.img = img;
		if(img != null)
		{
			Dimension dim = PuzzlePan.getNewDimension(img, w, h);
			dim.setSize(dim.getWidth(), dim.getHeight());
			this.setPreferredSize(dim);
		}
		else
			this.setPreferredSize(new Dimension(w, h));
	}

	public BufferedImage getImage()
	{
		return img;
	}

}