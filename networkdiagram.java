/*
This project is going to be a little hard to understand because of all the nested functions but i'll try to explain the important things here.
1) as these are parallel arrays, it is very important to know what the main index of incrementing through the development is. It is "grandposition"
2) any dependency that has no existing activty to match it will be forgotten in the end

*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.Object;
import java.io.*;

public class networkdiagram {
	public static void main(String args[]) {
		
	    ArrayList<String> activityName = new ArrayList<String>();					//list of all activities in program
	    ArrayList<String> activityDependencies = new ArrayList<String>();			//list of all dependencies in program
	    ArrayList<String> activityPaths = new ArrayList<String>();					//lists of all paths to each activity in the program
	    							
	    ArrayList<String> pathmin = new ArrayList<String>();						//the list of all minimum paths to each node
	    ArrayList<Integer> activityDuration = new ArrayList<Integer>();				//the list of durations for every activity
	   
	    
	    ArrayList<String> pathsInOrder = new ArrayList<String>();					//order of paths in descending order (matched with durationInOrder)
	    ArrayList<Integer> durationInOrder= new ArrayList<Integer>();				//order of duration in descending oder(matched with pathsInOrder)
	    
	   
	    
	    int tot=0;																	//sum of each path when broken down around line 270
	    int grandposition=0;														//the overall position during most of the function
	    
	    																	
	    int[] durmin= new int[100];													//duration minimums to reach all functions
	    
	    
	    
	    String paths="";															//initializing so i can use the set function
	    int checker=0;																//used to toggle the path development portion but i honestly don't remember why i use it.
	    
	    
	    
	    String readIn;																//the variable used to read from system.in
	    
	    
	    String[] arrOfStr=new String[100];											//what the strings are split into once they're parced using the '|' (OR bar)
	     
	    
		boolean check=false;														// used to shut off the loop when the user no longer wishes to add more nodes
		char continuetest='y';														// continuation response default
	    
	///// First entry/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.print("Enter the first node activty name then hit ENTER (your input may not contain a period('.')\n");		//first node activity name
		Scanner scanIn = new Scanner(System.in);

		
		readIn=scanIn.nextLine();
		
		boolean bool = readIn.contains(".");
		
		while(bool==true){																										//error checking
			System.out.print("I told you no periods!\ndo it again...\nEnter the first node activty name then hit ENTER (your input may not contain a period('.')\n");
			readIn=scanIn.nextLine();
			bool = readIn.contains(".");
		}
		
		activityName.add(readIn);
		activityPaths.add(readIn);
	    		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	    
		System.out.print("Enter the first node activty duration then hit ENTER\n");												//first activity duration
		
		readIn=scanIn.nextLine();
		int def = parse(readIn);
		while(def==106576){																										// error checking
			System.out.print("What you doing entering a non-number????\nso disrespectful...\ndo it again....\nEnter the first node activty duration then hit ENTER\n");
			readIn=scanIn.nextLine();
			 def = parse(readIn);
		}
		
		activityDuration.add(def);
		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.print("would you like to continue to add nodes?(y/n)\n");													// continuation decision
		continuetest = scanIn.next().charAt(0);
		scanIn.nextLine();
		
		if(continuetest=='n'){
			check=true;
		}
		 
	  
		  ++grandposition;
//////// OTHER ENTRIES///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    while(check==false){
	    	
	    	System.out.print("Enter the additional node's activty name then hit ENTER (your input may not contain a period('.')\n");
	    	readIn=scanIn.nextLine();
			
			bool = readIn.contains(".");
			
			while(bool==true){
				System.out.print("I told you no periods!\ndo it again...\nEnter the node's activty name then hit ENTER (your input may not contain a period('.')\n");
				readIn=scanIn.nextLine();
				bool = readIn.contains(".");
			}	        
			
			activityName.add(readIn);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			System.out.print("Enter the additional node's activty duration then hit ENTER\n");
			readIn=scanIn.nextLine();
			def = parse(readIn);
			while(def==106576){
				
				System.out.print("What you doing entering a non-number????\nso disrespectful...\ndo it again....\nEnter the node's activty duration then hit ENTER\n");
				readIn=scanIn.nextLine();
				 def = parse(readIn);
			}
			
			activityDuration.add(def);
			
			
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	        
	        System.out.print("Enter the activity's list of dependencies separated by commas then hit ENTER(your input may not contain a period('.')(Ex. arc,bat,car)\n");		//dependencies input
	        readIn=scanIn.nextLine();
	        
	        
	        bool = readIn.contains(".");
			
			while(bool==true){																																					//error checking
				System.out.print("I told you no periods!\ndo it again...\nEnter the activity's dependencies then hit ENTER (your input may not contain a period('.')\n");
				readIn=scanIn.nextLine();
				bool = readIn.contains(".");
			}
			
	        activityDependencies.add(readIn);
	        
	        
		
//// EVERYTHING AFTER HERE is processing dependencies to develop the possible paths to reach each node (convoluted algorithm)

			String parts[] = activityDependencies.get(activityDependencies.size()-1).split(" | "); 										//split OR bar to modify strings before setting then to another input's path
			activityPaths.add("");
        	for(int it=0;it<parts.length;++it){
        		
			
			arrOfStr = parts[it].split(",", 100); 																		//split commas to get individual activity names before combining them with new input's path
			ArrayList<String> as = new ArrayList<String>(Arrays.asList(arrOfStr));
			if(as.get(it).equals(activityName.get(grandposition))){														//avoids inputing it's self as a dependency by deleting it.
				as.remove(it);
				System.out.println("why depend on yourself?");
	       }
			
	        for (String a : arrOfStr){																					//arrays of split string
	        		        	
	        	for(int k=0;k < activityName.size();++k){																//all of this is using the broken down pieces to rebuild them into paths
	        		//System.out.print("TEST\n");
	        		if(a.equals(activityName.get(k))){
	        			
	        			String orsplit[] = activityPaths.get(k).split(" | ");
	        			
	                	for(int n=0;n<orsplit.length;++n){
	                		
	                		//System.out.println("."+orsplit[n]+"."+"\n");
	        			if(checker>=1){
	        				if(orsplit[n].equals("|")){
	        					
	        					paths= paths+" "+orsplit[n]+" ";
			        			activityPaths.set(grandposition,paths);
	        					
		        			}
	        				
	        				else{
	        					//System.out.println(activityPaths.get(grandposition).charAt(activityPaths.get(grandposition).length()-2)+".");
	        					if(activityPaths.get(grandposition).charAt(activityPaths.get(grandposition).length()-2)=='|'){		// Combated a unique error where I had two bars appearing next to each other
	        						paths=paths+orsplit[n]+"." +activityName.get(grandposition);
	    	        				activityPaths.set(grandposition,paths);
	        					}
	        					else{
	        				paths=paths+" | "+orsplit[n]+"." +activityName.get(grandposition);
	        				activityPaths.set(grandposition,paths);
	        					}
	        				}
	        				
	        			}
	        	else{
	        			if(orsplit[n].equals("|")){
	        				
	        				
	        				paths= paths+" "+orsplit[n]+" ";											
		        			activityPaths.set(grandposition,paths);
		        			
	        			}
	        			else{
	        				
	        			paths= paths+orsplit[n]+"." +activityName.get(grandposition);
	        			activityPaths.set(grandposition,paths);
	        			//System.out.println("activity: " + activityPaths.get(grandposition));
	        				
	        			}
	        			}
	        			
	                	}
	        		}
	        		//else if(a==activityName.get(k) && linked[k]<1){
	        		//	validity=true;
	        		//}
	        	
	        	
	        }
	        	checker++;   
	        	
	   }
	   checker=0;
	   paths="";
}
//////////////////////////////////////////////////////////////////////////////////////////////////	       
			// System.out.print(numOfPaths);
		     //   System.out.print("\n");
			
	    	System.out.print("would you like to continue adding activities?(y/n)\n");									//checking desire of user
	    	continuetest = scanIn.next().charAt(0);
	    	scanIn.nextLine();
	    	
	    	if(continuetest=='n'){
	    		check=true;
	    	}
	    	
	    	
	    	
		    System.out.println("Contents of the activities: " + activityName);											//print activity list
		    System.out.println("Durations of the activities: " + activityDuration);										//print duration list
		   // System.out.println("Dependencies of the activities: " + activityDependencies);
		    
		    System.out.println("paths " + activityPaths);																//print paths list
		  
		    
		        
			  ++grandposition;
		  
	    }
	    
//////////////////////////reading paths to determine duration and path order /////////////////////////////////////////
	    for(grandposition=0;grandposition<activityPaths.size();grandposition++){			// OR bar level
	    	durmin[grandposition]=1000000000;
	    	pathmin.add("");
	    	
	    	String parts[]= activityPaths.get(grandposition).split(" | ");
	    	ArrayList<String> parts2 = new ArrayList<String>(Arrays.asList(parts));
	    	for(int smallposition=0;smallposition<parts2.size();smallposition++){			// . (period) level
	    		if(parts2.get(smallposition).equals("|")){									//combats unique error where it was splitting while including an OR bar
	    			parts2.remove(smallposition);
	    		}
	    		
	    		String partss=parts2.get(smallposition).replace('.',' ');					//also combatting an error
	    		
	    		String partsss[]= partss.split(" ");
		    	ArrayList<String> parts3 = new ArrayList<String>(Arrays.asList(partsss));	// converting to make the info more usable
		    	
		    for(int tinyposition=0;tinyposition<parts3.size();tinyposition++){				//identification level where we start adding it all up
		    	
		    	for(int miniposition=0;miniposition<activityName.size();++miniposition){
		    		if(activityName.get(miniposition).equals(parts3.get(tinyposition))){
		    			tot=tot+activityDuration.get(miniposition);							//total computation of string durations
		    			
		    		}
		    	}
		    }
		   
		    if(tot<durmin[grandposition]){													//get the minimums of each activity
		    	durmin[grandposition]=tot;
		    	pathmin.set(grandposition, parts2.get(smallposition));
		    	
		    
		    }
		    durationInOrder.add(tot);														// gather all path totals and duration totals to later be sorted
		    pathsInOrder.add(parts2.get(smallposition));
		    
		    tot=0;
	    	}
	    	
	    }
	    
	  ////SORT /////////
	    for(int set=0;set<durationInOrder.size();++set){									//bubble sort cause im a rebel
	    
	    	for(int set2=0;set2<durationInOrder.size();++set2){
		    	
	    		if(durationInOrder.get(set2)>=durationInOrder.get(set)){
	    			
	    			int temps=durationInOrder.get(set);
	    			durationInOrder.set(set,durationInOrder.get(set2));
	    			durationInOrder.set(set2,temps);
	    			
	    			String tempss=pathsInOrder.get(set);
	    			pathsInOrder.set(set,pathsInOrder.get(set2));
	    			pathsInOrder.set(set,tempss);
	    		}
	    		
		    }
	    }
	    
	    
	    
	    for(int test=0;test<activityName.size();++test){									//printing of minimum information
	    System.out.println("shortest duration and path for activty "+activityName.get(test)+": " + durmin[test]+ " "+pathmin.get(test));
	    
	    }
	    System.out.println("all Path's Info:");
		 for(int test2=durationInOrder.size()-1;test2>=0;test2--){							//printing of ALL path information;
			    System.out.println(pathsInOrder.get(test2)+" " +durationInOrder.get(test2));
			    
			    }
	    }
	


	
public static int parse(String s) {															//function to handle NumberFormatException credit to stackoverflow
	int def=106576;
	
	    try {
	        return Integer.parseInt(s);
	    }
	    catch (NumberFormatException e) {
	        // It's OK to ignore "e" here because returning a default value is the documented behaviour on invalid input.
	        return def;
	    }
	}
	}
