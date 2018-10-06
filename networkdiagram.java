import java.util.ArrayList;
import java.util.Scanner;

public class networkdiagram {
	public static void main(String args[]) {
		
		// create array lists
	    ArrayList activityName = new ArrayList();
	    ArrayList activityDependencies = new ArrayList();
	    ArrayList activityDuration = new ArrayList();

		boolean check=false;
		char continuetest='y';
	    
		System.out.print("Enter the first node activty name then hit ENTER\n");
		Scanner scanIn = new Scanner(System.in);

	    // add elements to the array list
		activityName.add(scanIn.nextLine());
	    
		
	    // display the array list
	    //System.out.println("Contents of al: " + activityName);

	    // Remove elements from the array list
	    //al.remove("F");
	    //al.remove(2);
	    //System.out.println("Size of al after deletions: " + al.size());
	    //System.out.println("Contents of al: " + al);
	    
		System.out.print("Enter the first node activty duration then hit ENTER\n");
		activityDuration.add(scanIn.nextLine());

		System.out.print("would you like to continue to add nodes?(y/n)\n");
		continuetest = scanIn.next().charAt(0);
		scanIn.nextLine();
		
		if(continuetest=='n'){
			check=true;
		}
	    while(check==false){

	        System.out.print("Enter the activty name of the additional node then hit ENTER\n");
			activityName.add(scanIn.nextLine());	        
	        
	        System.out.print("Enter the activty duration of the additional node then hit ENTER\n");
			activityDuration.add(scanIn.nextLine());
	        
	        System.out.print("Enter the activity's list of dependencies separated by commas then hit ENTER (Ex. a,b,c)\n");
			activityDependencies.add(scanIn.nextLine());
			
			
	    	System.out.print("would you like to continue to add nodes?(y/n)\n");
	    	continuetest = scanIn.next().charAt(0);
	    	scanIn.nextLine();
	    	
	    	if(continuetest=='n'){
	    		check=true;
	    	}
	    	
		    System.out.println("Contents of the activity: " + activityName);
		    System.out.println("Durations of the activity: " + activityDuration);
		    System.out.println("Dependencies of the activity: " + activityDependencies);
	    }
	}
}
