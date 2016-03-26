package kmeans;


import java.util.ArrayList;
import java.util.List;

public class KMeans{

	//Number of Clusters. This metric should be related to the number of points
    private int NUM_CLUSTERS;    
    //Number of Points
    private int NUM_POINTS;
    //Min and Max X and Y
    private static final int MIN_COORDINATE = 0;
    private static final int MAX_COORDINATE = 450;
    
    private int iteration;
    private List<Point> points;
    private List<Cluster> clusters;
    private List<Point> lastCentroids;
    
    public KMeans(int numpoints, int numclusters) {
    	this.NUM_POINTS = numpoints;
    	this.NUM_CLUSTERS = numclusters;
    	this.iteration=0;
    	this.points = new ArrayList<Point>();
    	this.clusters = new ArrayList<Cluster>();    	
    }
    
//    public static void main(String[] args) {
//    	
//    	KMeans kmeans = new KMeans();
//    	kmeans.init();
//    	kmeans.calculate();
//    	
//    }
    

    //Initializes the process
    public void init() {
    	//Create Points
    	points = Point.createRandomPoints(MIN_COORDINATE,MAX_COORDINATE,NUM_POINTS);
    	
    	//Create Clusters
    	//Set Random Centroids
    	for (int i = 0; i < NUM_CLUSTERS; i++) {
    		Cluster cluster = new Cluster(i);
    		Point centroid = Point.createRandomPoint(MIN_COORDINATE,MAX_COORDINATE);
    		cluster.setCentroid(centroid);
    		clusters.add(cluster);
    	}
    	
    	//Print Initial state
    	plotClusters();
    	
    }
    
    public void initPanel(ClusteringPanel panel){
    	panel.setNumberOfCluster(NUM_CLUSTERS);
    	panel.updateData(points);
    	panel.repaint();
    }
    
    public void updateClusteringPanel(ClusteringPanel panel){
    	panel.setCentroids(lastCentroids);
    	panel.updateData(points);
    	panel.repaint();
    }

	private void plotClusters() {
    	for (int i = 0; i < NUM_CLUSTERS; i++) {
    		Cluster c = (Cluster) clusters.get(i);
    		c.plotCluster();
    	}
    }
    
	//The process to calculate the K Means, with iterating method.
    public void calculate() {
        boolean finish = false;
        iteration = 0;
        
        // Add in new data, one at a time, recalculating centroids with each new one. 
        while(!finish) {
        	//Clear cluster state
        	clearClusters();
        	
        	lastCentroids = getCentroids();
        	
        	//Assign points to the closer cluster
        	assignCluster();
            
            //Calculate new centroids.
        	calculateCentroids();
        	
        	iteration++;
        	
        	List<Point> currentCentroids = getCentroids();
        	
        	//Calculates total distance between new and old Centroids
        	double distance = 0;
        	for(int i = 0; i < lastCentroids.size(); i++) {
        		distance += Point.distance(lastCentroids.get(i),currentCentroids.get(i));
        	}
        	System.out.println("#################");
        	System.out.println("Iteration: " + iteration);
        	System.out.println("Centroid distances: " + distance);
        	plotClusters();
        	        	
        	if(distance == 0) {
        		finish = true;
        	}

        }
    }
    
    public boolean iteration(){
    	//Clear cluster state
    	clearClusters();
    	
    	lastCentroids = getCentroids();
    	
    	//Assign points to the closer cluster
    	assignCluster();
        
        //Calculate new centroids.
    	calculateCentroids();
    	
    	iteration++;
    	
    	List<Point> currentCentroids = getCentroids();
    	
    	//Calculates total distance between new and old Centroids
    	double distance = 0;
    	for(int i = 0; i < lastCentroids.size(); i++) {
    		distance += Point.distance(lastCentroids.get(i),currentCentroids.get(i));
    	}
//    	System.out.println("#################");
//    	System.out.println("Iteration: " + iteration);
//    	System.out.println("Centroid distances: " + distance);
    	//plotClusters();
    	        	
    	if(distance == 0) {
    		return true;
    	}
    	return false;
    }
    
    public int getCurrentIteration(){
    	return iteration;
    }
    
    private void clearClusters() {
    	for(Cluster cluster : clusters) {
    		cluster.clear();
    	}
    }
    
    private List<Point> getCentroids() {
    	List<Point> centroids = new ArrayList<Point>(NUM_CLUSTERS);
    	for(Cluster cluster : clusters) {
    		Point aux = cluster.getCentroid();
    		Point point = new Point(aux.getX(),aux.getY());
    		centroids.add(point);
    	}
    	return centroids;
    }
    
    private void assignCluster() {
        double max = Double.MAX_VALUE;
        double min = max; 
        int cluster = 0;                 
        double distance = 0.0; 
        
        for(Point point : points) {
        	min = max;
            for(int i = 0; i < NUM_CLUSTERS; i++) {
            	Cluster c = clusters.get(i);
                distance = Point.distance(point, c.getCentroid());
                if(distance < min){
                    min = distance;
                    cluster = i;
                }
            }
            point.setCluster(cluster);
            clusters.get(cluster).addPoint(point);
        }
    }
    
    private void calculateCentroids() {
        for(Cluster cluster : clusters) {
            double sumX = 0;
            double sumY = 0;
            List<Point> list = cluster.getPoints();
            int n_points = list.size();
            
            for(Point point : list) {
            	sumX += point.getX();
                sumY += point.getY();
            }
            
            Point centroid = cluster.getCentroid();
            if(n_points > 0) {
            	double newX = sumX / n_points;
            	double newY = sumY / n_points;
                centroid.setX(newX);
                centroid.setY(newY);
            }
        }
    }
}