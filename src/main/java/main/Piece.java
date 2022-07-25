package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Piece extends JPanel
{
	private static final long serialVersionUID = 4314924537067868767L;
	
	private ImageIcon imgIcon;
	private transient BufferedImage img;
	private transient boolean isSelected = false;
	private int[] indexes;
	
	public Piece(BufferedImage img, int x, int y, int[] indexes)
	{
		this.imgIcon = new ImageIcon(img);
		this.img = img;
		this.indexes = indexes;
		if(img == null)
			return;
		this.setBounds(x, y, img.getWidth(), img.getHeight());
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(imgIcon == null)
			return;
		if(img == null)
			img = PuzzleContainer.toBufferedImg(imgIcon);
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
		if(isSelected)
		{
			g.setColor(Color.BLUE);
			g.drawRoundRect(0, 0, this.getWidth(), this.getHeight(), 10, 10);
		}
	}
	
	public boolean isSelected()
	{
		return isSelected;
	}
	
	public void setSelected(boolean bool)
	{
		this.isSelected = bool;
	}
	
	public int[] getPosition()
	{
		return new int[] {this.getX(), this.getY()};
	}

	public int[] getIndexes() {
		return indexes;
	}

	public void setIndexes(int[] indexes) {
		this.indexes = indexes;
	}
	
}