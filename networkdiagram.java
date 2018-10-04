import java.util.*;


public class networkdiagram {
	
public static void main(String args[]){
	
	boolean check=false;
	char continuetest='y';
	int size=1;
	
	String[]activityname= new String[100];
	String[]activitydependencies= new String[100];
	int[]activityduration= new int[100];
	
	
	System.out.print("Enter the first node activty name then hit ENTER\n");
	
	Scanner scanIn = new Scanner(System.in);
    activityname[1] = scanIn.nextLine();
    System.out.print("Enter the first node activty duration then hit ENTER\n");
    activityduration[1]=scanIn.nextInt();
    activitydependencies[1]="\0";
    size++;
    System.out.print("would you like to continue to add nodes?(y/n)\n");
	continuetest= scanIn.next().charAt(0);
	if(continuetest=='n'){
		check=true;
	}
    
    while(check==false){
    	
    	System.out.print("Enter the activty name of the additional node then hit ENTER\n");
        activityname[size] = scanIn.nextLine();
        System.out.print("Enter the activty duration of the additional nodethen hit ENTER\n");
        activityduration[size]=scanIn.nextInt();
        System.out.print("Enter the activity's list of dependencies separated by commas then hit ENTER (Ex. a,b,c\n");
        activitydependencies[size] = scanIn.nextLine();
    	
    	size++;
    	
    	System.out.print("would you like to continue to add nodes?(y/n)\n");
    	continuetest= scanIn.next().charAt(0);
    	if(continuetest=='n'){
    		check=true;
    	}
    }
    scanIn.close();
}
}
