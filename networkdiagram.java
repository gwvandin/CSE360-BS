import java.util.ArrayList;
import java.util.Scanner;

public class networkdiagram {
	public static void main(String args[]) {
		
	    ArrayList<String> activityName = new ArrayList<String>();
	    ArrayList<String> activityDependencies = new ArrayList<String>();
	   // ArrayList activityPaths = new ArrayList();
	    ArrayList<Integer> activityDuration = new ArrayList<Integer>();
	    boolean validity;
	    int place=0;
	    int tot=0;
	    int stringsize=0;
	    int i=0;
	    int ii=0;
	    int k=0;
	    int[] Paths= new int[100];
	    int numOfPaths=1;
	    int commaCount=0;
	    String temp;
	    
	    String[] arrOfStr=new String[100];
	     

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
		activityDuration.add(scanIn.nextInt());

		System.out.print("would you like to continue to add nodes?(y/n)\n");
		continuetest = scanIn.next().charAt(0);
		scanIn.nextLine();
		
		if(continuetest=='n'){
			check=true;
		}
		 tot=tot+activityDuration.get(i);
	       
		  ++i;
	    while(check==false){

	        System.out.print("Enter the activty name of the additional node then hit ENTER\n");
			activityName.add(scanIn.nextLine());	        
	        
	        System.out.print("Enter the activty duration of the additional node then hit ENTER\n");
			activityDuration.add(scanIn.nextInt());
			scanIn.nextLine();
	        
	        System.out.print("Enter the activity's list of dependencies separated by commas then hit ENTER (Ex. a,b,c)\n");
			activityDependencies.add(scanIn.nextLine());
			
//Checks for commas
			/*
			for( stringsize = 0; stringsize < activityDependencies.get(activityDependencies.size()-1).length(); stringsize++ )
			{
			    temp = activityDependencies.get(activityDependencies.size()-1); 
			    if( temp.charAt(stringsize)==','){
			        numOfPaths++;
			       
			    }
			    
			}
			*/
			
			arrOfStr = activityDependencies.get(activityDependencies.size()-1).split(",", 15); 
			  
	        for (String a : arrOfStr){
	        	
	        	for(k=0;k < activityName.size();++k){
	        		if(a==activityName.get(k))
	        			validity=true;
	        	
	        	
	        }
	            
	   }  
	        
	        
//////////////////////////////////////////////////////////////////////////////////////////////////	       
			 System.out.print(numOfPaths);
		        System.out.print("\n");
			
	    	System.out.print("would you like to continue to add nodes?(y/n)\n");
	    	continuetest = scanIn.next().charAt(0);
	    	scanIn.nextLine();
	    	
	    	if(continuetest=='n'){
	    		check=true;
	    	}
	    	
		    System.out.println("Contents of the activities: " + activityName);
		    System.out.println("Durations of the activities: " + activityDuration);
		    System.out.println("Dependencies of the activities: " + activityDependencies);
		  
		    
		        tot=tot+activityDuration.get(i);
		        System.out.print(tot);
		        System.out.print("\n");
			  ++i;
		  
	    }
	    
	}
}
