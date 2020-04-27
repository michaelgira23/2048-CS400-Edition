package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * ListGameLeaderBoard class
 * 
 * @author Quan Nguyen 
 *
 */
public class ListGameLeaderBoard  implements Serializable {

    public static void main(String[] args) {
        // Creating an empty PriorityQueue 
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(); 
  
        // Use add() method to add elements into the Queue 
        queue.add(10); 
        queue.add(15); 
        queue.add(30); 
        queue.add(20); 
        queue.add(5); 
        queue.add(25); 
        // Neu co it nhat 1 diem thi lay ra
 		Iterator<Integer> itr2 = queue.iterator();
 		// Diem thap nhat
 		Integer minScore = itr2.next();
         while (itr2.hasNext()) {
         	// Diem dang duyet
        	 Integer currentScore = itr2.next();
        	 minScore = currentScore;
         	// So sanh diem dang duyet voi diem thap nhat
         	if (currentScore < minScore) {
         		// Gan diem thap nhat bang diem dang duyet
         		minScore = currentScore;
         	}
         }
          
        // Displaying the PriorityQueue 
        System.out.println("The PriorityQueue: " + queue); 
  
        // Creating the array and using toArray() 
        Object[] arr = queue.toArray(); 
  
        System.out.println("The array is:"); 
        for (int j = 0; j < arr.length; j++) 
            System.out.println(arr[j]); 
        
        
        PriorityQueue<String> pQueue = new PriorityQueue<>();
 
        // Adding items to the pQueue using add()
        pQueue.add("70");
        pQueue.add("20");
        pQueue.add("50");
        pQueue.add("40");
        pQueue.add("80");
        pQueue.add("20");
 
        System.out.println("Phan tu dau tien:" + pQueue.peek());
        // Sap xep priority queue
        Object[] pQArray = pQueue.toArray();
        Arrays.sort(pQArray);
        for (Object string : pQArray) {
			System.out.println(string.toString());
		}
 
        System.out.println("Queue: ");
        Iterator itr = pQueue.iterator();
        while (itr.hasNext())
            System.out.println(itr.next());
 
        pQueue.remove("G");
        System.out.println("Sau khi xoa phan tu G");
        Iterator<String> itr3 = pQueue.iterator();
        while (itr3.hasNext())
            System.out.println(itr3.next());
 
    }
	// Priority game leader board
	private PriorityQueue<GameLeaderBoard> topScores = new PriorityQueue<GameLeaderBoard>();
	// limit the amounts of top score
	public static final int NUM_LEADER_BOARD = 5;
	
	// Check if the current score is in top score
	public boolean isTopScore (GameLeaderBoard currentScore) {
		Iterator<GameLeaderBoard> topScoresIterator = topScores.iterator();
		while (topScoresIterator.hasNext()) {
			GameLeaderBoard gameLeaderBoard = topScoresIterator.next();
			if (currentScore.getScore() >= gameLeaderBoard.getScore()) {
				
				return true;
			}
		}
		
		return false;
	}
	
	// take out the lowest score
	public void pollMinScore () {
		if (topScores.isEmpty() == true) {
			
			return;
		}
		
		Iterator<GameLeaderBoard> itr = topScores.iterator();
		
		GameLeaderBoard minScore = itr.next();
        while (itr.hasNext()) {
        	
        	GameLeaderBoard currentScore = itr.next();
        
        	if (currentScore.getScore() < minScore.getScore()) {
        	
        		minScore = currentScore;
        	}
        }
        //If list has 5 elements, take out 1 lowest element
        if (topScores.size() >= 5) {
        	topScores.remove(minScore);
		}
	}
	
	// Add score to the list
	public void addScoreToList (GameLeaderBoard currentScore) {
		topScores.add(currentScore);
	}
	
	//Sorting top score list
	public Object[] sortedTopScores() {
        Object[] pQArray = topScores.toArray();
        Arrays.sort(pQArray);
        return pQArray;
	}
	
	public PriorityQueue<GameLeaderBoard> getTopScores() {
		return topScores;
	}
	public void setTopScores(PriorityQueue<GameLeaderBoard> topScores) {
		this.topScores = topScores;
	}
}
