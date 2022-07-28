package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PuzzleContainer extends JPanel
{
	private static final long serialVersionUID = 1L;
	public static final String LAST_PUZZLE_NAME = "last_puzzle.save";
	public static final Dimension DEFAULT_PUZZLE_SIZE = new Dimension(675, 550);

	private ImageIcon imgIcon;
	private transient BufferedImage img;
	private Piece[][] pieces;
	private transient Piece actualPiece;
	private transient int[] startIndex;
	private Dimension pieceDim;
	
	public PuzzleContainer(ImageIcon imgIcon, int c, int l)
	{
		this.setLayout(null);
		this.imgIcon = imgIcon;
		this.img = getResizedImg();
		this.setupPieces(img, c, l);
		this.addPieces(c, l);
		pieceDim = pieces[0][0].getSize();
		this.setPreferredSize(new Dimension((int)pieceDim.getWidth()*c, (int)pieceDim.getHeight()*l));
		
		this.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e)
			{
				actualPiece = getPieceByMousePos(e.getX(), e.getY());
				if(actualPiece == null)
					return;
				startIndex = getIndexTab(actualPiece.getX(), actualPiece.getY());
				setComponentZOrder(actualPiece, 0); // Met la piece selectionnee au premier plan
				actualPiece.setSelected(true);
				actualPiece.repaint();
			}
			
			public void mouseReleased(MouseEvent e)
			{
				if(actualPiece == null)
					return;
				int width = getWidth()-1, height = getHeight()-1;
				int x = e.getX() < 0 ? 0 : e.getX() > width ? 
						(width+1)-(int)pieceDim.getWidth() : e.getX(); 
				int y = e.getY() < 0 ? 0 : e.getY() > height ? 
						(height+1)-(int)pieceDim.getHeight() : e.getY(); 
				
				int[] endIndex = getIndexTab(x, y);
				int[] startPos = new int[] {startIndex[0]*(int)pieceDim.getWidth(), 
											startIndex[1]*(int)pieceDim.getHeight()};
				int[] endPos = new int[] {endIndex[0]*(int)pieceDim.getWidth(), 
										  endIndex[1]*(int)pieceDim.getHeight()};
				pieces[startIndex[0]][startIndex[1]] = getPieceByMousePos(x, y);
				pieces[startIndex[0]][startIndex[1]].setLocation(startPos[0], startPos[1]);
				pieces[endIndex[0]][endIndex[1]] = actualPiece;
				actualPiece.setLocation(endPos[0], endPos[1]);
				actualPiece.setSelected(false);
				pieces[startIndex[0]][startIndex[1]].repaint();
				actualPiece.repaint();
			}
		});
		
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if(actualPiece == null)
					return;
				actualPiece.setLocation(e.getX(), e.getY());
				actualPiece.repaint();
			}
		});
	}

	public void setupPieces(BufferedImage img, int c, int l)
	{
		BufferedImage[] images = cutImg(img, c, l);
		pieces = new Piece[c][l];
		int x = 0, y = 0, w = images[0].getWidth(), h = images[0].getHeight();
		for(int j=0;j<c;j++)
		{
			for(int i=0;i<l;i++)
			{
				pieces[j][i] = new Piece(images[(j*l)+i], x, y, new int[] {j, i});
				y += h;
			}
			y = 0;
			x += w;
		}
	}
	
	public void addPieces(int c, int l) {
		this.removeAll();
		for(int j=0;j<c;j++)
			for(int i=0;i<l;i++)
				this.add(pieces[j][i]);
	}

	public int[][] getMatrixOfImage(BufferedImage img)
	{
		if(img == null)
			return null;
		
		int w = img.getWidth();
		int h = img.getHeight();
		int[][] matrix = new int[w][h];
		for(int j=0;j<w;j++)
			for(int i=0;i<h;i++)
				matrix[j][i] = img.getRGB(j, i);
		return matrix;
	}
	
	public BufferedImage getImageByMatrix(int[][] matrix, int c, int l, int w, int h)
	{
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		
		for(int j=0;j<w;j++)
			for(int i=0;i<h;i++)
				img.setRGB(j, i, matrix[(c*(w-1))+j][(l*(h-1))+i]);
		
		return img;
	}
	
	public BufferedImage[] cutImg(BufferedImage img, int c, int l)
	{
		int[][] matrix = getMatrixOfImage(img);
		if(matrix == null || (c <= 0 || l <= 0) || img == null)
			return null;
		BufferedImage[] images = new BufferedImage[c*l];
		int w = (int) Math.floor((float)img.getWidth(null)/c);
		int h = (int) Math.floor((float)img.getHeight(null)/l);
		
		for(int j=0;j<c;j++)
		{
			for(int i=0;i<l;i++)
			{
				images[(j*l)+i] = getImageByMatrix(matrix, j, i, w, h);
			}
		}		
		
		return images;
	}
	
	public int[] getIndexTab(int x, int y)
	{
		int w = pieces[0][0].getWidth();
		int h = pieces[0][0].getHeight();
		return new int[] {(int)Math.floor((float)x/w), (int)Math.floor((float)y/h)};
	}
	
	public Piece getPieceByMousePos(int x, int y)
	{
		int[] indexTab = getIndexTab(x, y);
		return pieces[indexTab[0]][indexTab[1]];
	}

	public int getRandNb(int min, int max)
	{
		return (int)(Math.random()*max) + min;
	}
	
	public void invertPiecePos(Piece p1, Piece p2)
	{
		if(p1 == null || p2 == null)
			return;
		int[] temp = new int[] {p1.getX(), p1.getY()};
		p1.setLocation(p2.getX(), p2.getY());
		p2.setLocation(temp[0], temp[1]);
	}
	
	public void invertPieceIndexes(int[] p1, int[] p2)
	{
		if(p1 == null || p2 == null)
			return;
		Piece temp = pieces[p1[0]][p1[1]];
		pieces[p1[0]][p1[1]] = pieces[p2[0]][p2[1]];
		pieces[p2[0]][p2[1]] = temp;
		invertPiecePos(pieces[p1[0]][p1[1]], pieces[p2[0]][p2[1]]);
	}
	
	public static BufferedImage toBufferedImg(ImageIcon imgIcon)
	{
		Image img = imgIcon.getImage();
		BufferedImage im = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = im.getGraphics();
		g.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
		g.dispose();
		
		return im;
	}

	public void scramble(int essais)
	{
		int c = pieces.length, l = pieces[0].length;
		for(int i=0;i<essais;i++)
			invertPieceIndexes(new int[] {getRandNb(0, c), getRandNb(0, l)},
						   new int[] {getRandNb(0, c), getRandNb(0, l)});
	}	
	
	public void save(String name)
	{
		ObjectOutputStream oos = null;
		File file = new File(name);
		try {
			oos =  new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	public static PuzzleContainer load(String name)
	{
		PuzzleContainer pC = null;
		ObjectInputStream ois = null;
		File file = new File(name);
		if(file.exists())
		{
			try {
				ois =  new ObjectInputStream(new FileInputStream(file)) ;
				pC = (PuzzleContainer)ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}	
		}

		return pC;
	}

	public void saveLastPuzzle() {
		save(LAST_PUZZLE_NAME);
	}

	public void loadLastPuzzle() {
		loadIn(LAST_PUZZLE_NAME);
	}

	public void loadIn(String name)
	{
		PuzzleContainer pC = load(name);
		if(pC == null)
			return;
		this.imgIcon = pC.imgIcon;
		this.pieces = pC.pieces;
		this.pieceDim = pC.pieceDim;
		int c = nbColumns(), l = nbLines();
		this.addPieces(c, l);
		this.setPreferredSize(new Dimension((int)pieceDim.getWidth()*c, (int)pieceDim.getHeight()*l));
	}

	public void changeDimension(int c, int l)
	{
		img = getResizedImg();
		this.setupPieces(img, c, l);
		this.addPieces(c, l);
		pieceDim = pieces[0][0].getSize();
		this.setPreferredSize(new Dimension((int)pieceDim.getWidth()*c, (int)pieceDim.getHeight()*l));
		this.revalidate();
		this.repaint();
	}

	public void changeImage(ImageIcon imgIcon)
	{
		this.imgIcon = imgIcon;
		this.img = null;
		changeDimension(nbColumns(), nbLines());
	}

	public void reset()
	{
		changeDimension(pieces.length, pieces[0].length);
	}

	public int nbColumns()
	{
		return pieces == null ? 0 : pieces.length;
	}

	public int nbLines()
	{
		return pieces == null ? -1 : (pieces[0] == null ? -1 : pieces[0].length);
	}

	public BufferedImage getResizedImg()
	{
		if(imgIcon == null)
			return null;
		BufferedImage buffImg = toBufferedImg(imgIcon);
		return PuzzlePan.resize(buffImg, (int)DEFAULT_PUZZLE_SIZE.getWidth(), (int)DEFAULT_PUZZLE_SIZE.getHeight());
	}

	public BufferedImage getImage()
	{
		return toBufferedImg(imgIcon);
	}

}
