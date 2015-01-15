/** Initial version of Solitaire.java
 */

package Solitaire;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Solitaire extends JApplet {
	public static final int suitSize = 13;
	public static final int baseX = 40, baseY = 10, shiftX = 80, shiftY = 15;
	public static final int TableWidth = 640, TableHeight = 500; 
		// default table window size
	static protected JFrame outerFrame; // the whole window with its frame
	static protected Solitaire innerFrame; // the inside of the frame
    static protected JPanel buttonPanel;
    static protected CardTable table;

    public static void main(String[] args) {
        
        outerFrame = new JFrame( "Solitaire");  
        outerFrame.setResizable(false);
		Solitaire innerFrame = new Solitaire(); 
		buttonPanel = new JPanel(); 
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,30,0));
		innerFrame.setLayout(new BorderLayout());
		
		//create a new game button
		JButton ng = new JButton("New Game");
		ng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.restart();
				table. requestFocus();
			}
		});
		buttonPanel.add(ng);
		
		// Create a quit button
		JButton bq = new JButton("Quit");
		bq.addActionListener( new ActionListener() {
		    public void actionPerformed( ActionEvent e) {
		    	System.exit(0);
		    }
		});
		buttonPanel.add(bq);	

		table = new CardTable();		  
		table.setFocusable(true);
		table.setPreferredSize(new Dimension( TableWidth, TableHeight));
		innerFrame.add(table,BorderLayout.CENTER);
		innerFrame.add( buttonPanel,BorderLayout.NORTH);

		outerFrame.add(innerFrame);
		
		
		outerFrame.pack();
        outerFrame.setVisible(true);
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(table), "Press Space or Click for Talon");
    	
    }
}

