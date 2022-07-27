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

	public Preview(BufferedImage img, int w, int h, boolean clickable)
	{
		if(img != null)
			this.setPreferredSize(PuzzlePan.getNewDimension(img, w, h));
		else
			this.setPreferredSize(new Dimension(w, h));
		
		if(clickable)
		{
			this.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
					new PreviewFrame(img);
				}

			});
		}

		this.img = img;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(img == null)
			return;
		Dimension dim = PuzzlePan.getNewDimension(img, this.getWidth(), this.getHeight());
		g.drawImage(img, 0, 0, (int)dim.getWidth(), (int)dim.getHeight(), null);
	}
}