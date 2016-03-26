package kmeans;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JPanel;

public class ClusteringPanel extends JPanel{
	
	private static final long serialVersionUID = 4824268500752553697L;
	private static final int pointThickness = 10;
	private static final int width =450;
	private static final int height = 450;
	
    private int clusterNumber;
   
    private List<Point> points;
    private List<Point> centroids;
    
    private Map<Integer, Color> colorMapper = new HashMap<Integer, Color>();
	
    private Random rand;
    
    public ClusteringPanel(){
    	this.centroids = new ArrayList<Point>();
    	this.points = new ArrayList<Point>();

    	this.rand = new Random(System.currentTimeMillis());
    	
    }
    
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		this.setSize(width, height);
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
	    for (Point p : points){
	    	int x = (int) Math.round(p.getX());
	    	int y = (int) Math.round(p.getY());
	    	
	    	g.setColor(colorMapper.get(p.getCluster()));
	    	g.fillOval(x, y, pointThickness, pointThickness);
	    }
	    
	    if (!this.centroids.isEmpty()){
	    	double[] linePoints = findEuclideanDistanceLinePoints();
		    g.setColor(Color.BLACK);
		    g.drawLine((int)Math.round(linePoints[0]),
		    		   (int)Math.round(linePoints[1]),
		    		   (int)Math.round(linePoints[2]),
		    		   (int)Math.round(linePoints[3]));
	    }
	    
	}
	
	public void setNumberOfCluster(int num){
		this.clusterNumber = num;
		
		for (int i = 0;i<clusterNumber;i++){
			
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			
			colorMapper.put(i, new Color(r,g,b));
		}
	}
	
	public void setCentroids(List<Point> centroids){
		this.centroids=centroids;
	}
	
	public void updateData(List<Point> points ){
		this.points = points;
		this.repaint();
	}

	private double[] findEuclideanDistanceLinePoints(){
		
			double x1 = this.centroids.get(0).getX();
			double x2 = this.centroids.get(1).getX();
			double y1 = this.centroids.get(0).getY();
			double y2 = this.centroids.get(1).getY();
			
			double m = -1/((y2-y1)/(x2-x1));
			double midx = (x1+x2)/2;
			double midy = (y1+y2)/2;
			
			double linex1=0;
			double linex2=width;
			double liney1=m*(linex1-midx)+midy;
			double liney2=m*(linex2-midx)+midy;
			return (new double[]{linex1,liney1,linex2,liney2});	
	}
	
}
