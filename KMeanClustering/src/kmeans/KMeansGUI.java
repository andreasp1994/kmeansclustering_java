package kmeans;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class KMeansGUI {

	private JFrame frmKmeansClusteringJava;
	private JTextField txtPoints;
	private JTextField txtClusters;
	
	private boolean STOP_FLAG = false;
	
	private static KMeans kmeans;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KMeansGUI window = new KMeansGUI();
					window.frmKmeansClusteringJava.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public KMeansGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmKmeansClusteringJava = new JFrame();
		frmKmeansClusteringJava.setTitle("K-Means Clustering Java Implementation - Antreas Pogiatzis");
		frmKmeansClusteringJava.setBounds(100, 100, 634, 589);
		frmKmeansClusteringJava.setLocationRelativeTo(null);
		frmKmeansClusteringJava.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmKmeansClusteringJava.getContentPane().setLayout(null);
		
		ClusteringPanel panel = new ClusteringPanel();
		panel.setBounds(12, 13, 450, 450);
		frmKmeansClusteringJava.getContentPane().add(panel);
		
		JLabel lblNumberOfPoints = new JLabel("Number of Points:");
		lblNumberOfPoints.setBounds(484, 13, 117, 16);
		frmKmeansClusteringJava.getContentPane().add(lblNumberOfPoints);
		
		txtPoints = new JTextField();
		txtPoints.setText("100");
		txtPoints.setBounds(484, 34, 116, 22);
		frmKmeansClusteringJava.getContentPane().add(txtPoints);
		txtPoints.setColumns(10);
		
		JLabel lblNumberOfClusters = new JLabel("Number of Clusters");
		lblNumberOfClusters.setBounds(484, 69, 117, 16);
		frmKmeansClusteringJava.getContentPane().add(lblNumberOfClusters);
		
		txtClusters = new JTextField();
		txtClusters.setEnabled(false);
		txtClusters.setText("2");
		txtClusters.setColumns(10);
		txtClusters.setBounds(484, 90, 116, 22);
		frmKmeansClusteringJava.getContentPane().add(txtClusters);
		
		JLabel lblIteration = new JLabel("Iteration: 0");
		lblIteration.setBounds(12, 476, 460, 16);
		frmKmeansClusteringJava.getContentPane().add(lblIteration);
		
		JSlider slider = new JSlider();
		slider.setBounds(56, 503, 363, 26);
		slider.setMaximum(3000);
		slider.setMinimum(0);
		frmKmeansClusteringJava.getContentPane().add(slider);
		
		JLabel lblSpeed = new JLabel("Fast");
		lblSpeed.setBounds(12, 503, 32, 26);
		frmKmeansClusteringJava.getContentPane().add(lblSpeed);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				
				if (kmeans == null){
					JOptionPane.showMessageDialog(null, "Please press initialize first!", "Error!", JOptionPane.ERROR_MESSAGE);
				} else {
			        
						Runnable runUpdate = new Runnable(){
							@Override
							public void run() {
								kmeans.updateClusteringPanel(panel);
								lblIteration.setText("Iteration: " + kmeans.getCurrentIteration());
							}
						};
			        	
						Runnable runIter = new Runnable(){
							@Override
							public void run() {
		
								while(!STOP_FLAG && !kmeans.iteration()){
									SwingUtilities.invokeLater(runUpdate);
										
									//Sleep
									try {
										Thread.sleep(slider.getValue());
									} catch (InterruptedException e) {
										
										e.printStackTrace();
									}
										
								} // end-of-while	
								STOP_FLAG = false;
								
									
							}
						};
						
						//Using thread to avoid blocking GUI.
						Thread t = new Thread(runIter);
						t.start();
				}
				
			}
		});
		btnNewButton.setBounds(484, 163, 117, 25);
		frmKmeansClusteringJava.getContentPane().add(btnNewButton);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				STOP_FLAG = true;
			}
		});
		btnStop.setBounds(484, 199, 117, 25);
		frmKmeansClusteringJava.getContentPane().add(btnStop);
		
		JButton btnPause = new JButton("Next Step");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				kmeans.iteration();
				kmeans.updateClusteringPanel(panel);
				lblIteration.setText("Iteration: " + String.valueOf(kmeans.getCurrentIteration()));
			}
		});
		btnPause.setBounds(484, 237, 117, 25);
		frmKmeansClusteringJava.getContentPane().add(btnPause);
		
		JButton btnInitialize = new JButton("Initialize");
		btnInitialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int NUM_POINTS = Integer.valueOf(txtPoints.getText());
				int NUM_CLUSTERS = Integer.valueOf(txtClusters.getText());
				
				kmeans = new KMeans(NUM_POINTS, NUM_CLUSTERS);
				kmeans.init();
				kmeans.initPanel(panel);
				lblIteration.setText("Iteration: " + String.valueOf(kmeans.getCurrentIteration()));
				
			}
		});
		btnInitialize.setBounds(484, 125, 117, 25);
		frmKmeansClusteringJava.getContentPane().add(btnInitialize);
		
		JLabel lblSlow = new JLabel("Slow");
		lblSlow.setBounds(430, 503, 32, 26);
		frmKmeansClusteringJava.getContentPane().add(lblSlow);
	}
}
