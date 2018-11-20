package ActDiagram;



import com.sun.beans.editors.IntegerEditor;
import com.sun.javafx.css.converters.StringConverter;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.LinearGradient;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;




public class networkdiagram {

    static String aName="", aDur="",response="", aDep="";

    static ArrayList<String> activityName = new ArrayList<String>();					//list of all activities in program
    static ArrayList<String> activityDependencies = new ArrayList<String>();			//list of all dependencies in program
    static ArrayList<String> activityPaths = new ArrayList<String>();					//lists of all paths to each activity in the program

    static ArrayList<String> pathmin = new ArrayList<String>();						//the list of all minimum paths to each node
    static ArrayList<Integer> activityDuration = new ArrayList<Integer>();
    static ArrayList<Integer> activityVerification = new ArrayList<Integer>();
    static ArrayList<String> activityDur = new ArrayList<String>();


    static ArrayList<String> pathsInOrder = new ArrayList<String>();					//order of paths in descending order (matched with durationInOrder)
    static ArrayList<Integer> durationInOrder= new ArrayList<Integer>();				//order of duration in descending oder(matched with pathsInOrder)
    static ArrayList<String> criticalPaths = new ArrayList<String>();
    static ArrayList<Integer> criticalDurations= new ArrayList<Integer>();

    static int q=0;
    static int tot=0;																	//sum of each path when broken down around line 270
    static int grandposition=0;														//the overall position during most of the function
    static int cyclecheck=1;
    static int checkempty=0;
    static int finish;
    static char restart='n';
    static boolean start=true;


    static int[] durmin= new int[100];													//duration minimums to reach all functions
    static String paths="";															//initializing so i can use the set function
    static int checker=0;																//used to toggle the path development portion but i honestly don't remember why i use it.
    static String readIn;																//the variable used to read from system.in
    static String[] arrOfStr=new String[100];											//what the strings are split into once they're parced using the '|' (OR bar)

    static boolean check=false;														// used to shut off the loop when the user no longer wishes to add more nodes
    static char continuetest='y';														// continuation response default


    public static void Diagram() {

        while(start==true){

            //JOptionPane.showMessageDialog(null, "NETWORK DIAGRAM CONSTRUCTOR\nAbout:\nThis program intends to construct a network diagram based on input given from the user!\n In this program there is one starting node that we acknowledge which doesn't hace any dependencies.\nafter the first node you will be prompted whether or not you'd like to continue\n and each iteration after that you will be asked for the name, duration, and dependencies of each node.\n once the user decides to finish the algorithm will be run and will show you what the possible paths from start to finish are!");

//////// First activity ///////////////////////////////////////////////////////////////////////
            addFirstActivity();

////////response ///////////////////////////////////////////////////////////////////////
            response = JOptionPane.showInputDialog("would you like to continue to add nodes?(y/n)");													// continuation decision
            continuetest = response.charAt(0);

            if(continuetest =='n'){
                handleDependencyError();
                check=true;
            }

            ++grandposition;

//////// additional activity ///////////////////////////////////////////////////////////////////////
            while(check==false){

                addActivity();

                response = JOptionPane.showInputDialog("would you like to continue to add nodes?(y/n)");													// continuation decision
                continuetest = response.charAt(0);

                if(continuetest=='n'){
                    handleDependencyError();
                    System.out.println(activityName);
                    System.out.println(activityDependencies);
                    createPaths();

                    check=true;
                }
                if(continuetest=='y'){
                    ++grandposition;
                }
            }

            //JOptionPane.showMessageDialog(null, "the AName is "+activityName+": Results" );
            //JOptionPane.showMessageDialog(null, "the ADur is "+activityDuration+": Results");
            //JOptionPane.showMessageDialog(null, "the ADep is "+activityDependencies+": Results");
            //JOptionPane.showMessageDialog(null, "the APaths is "+activityPaths+": Results");


            createPathList();
            calcDuration();
            System.out.println(criticalPaths);
            System.out.println(pathsInOrder);
            System.out.println(criticalDurations);
            System.out.println(durationInOrder);
            orderLists();

            System.out.println(criticalPaths);
            System.out.println(pathsInOrder);
            System.out.println(criticalDurations);
            System.out.println(durationInOrder);


            String pathmax="All Paths and Durations in Decending Order:\n";
            for(int test2=durationInOrder.size()-1;test2>=0;test2--){							//printing of ALL path information;
                pathmax=pathmax+pathsInOrder.get(test2)+"    " +durationInOrder.get(test2)+"\n";

            }

            pathmax=pathmax+"Critical Paths and Durations in Decending Order:\n";
            for(int test2=criticalDurations.size()-1;test2>=0;test2--){							//printing of ALL path information;
                pathmax=pathmax+criticalPaths.get(test2)+"    " +criticalDurations.get(test2)+"\n";

            }

            String resultss="the result paths are:\n" + pathmax;

            resultss=resultss.replace('.', '>');

            JOptionPane.showMessageDialog(null, resultss);

            response = JOptionPane.showInputDialog("would you like run the application again?(y/n)");													// continuation decision
            restart = response.charAt(0);
            //frame.setVisible(false);

            if(restart=='y'){
                start=true;
            }
            else{
                start=false;
            }
        }
    }

    public static int parse(String s) {															//function to handle NumberFormatException credit to stackoverflow
        int def=106576;

        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            // It's OK to ignore "e" here because returning a default value is the documented behavior on invalid input.
            return def;
        }
    }

    public static void addFirstActivity(){
        aName = JOptionPane.showInputDialog("Enter the first node activty name then hit ENTER (your input may not contain a period('.')");

        activityName.add(aName);
//////////////// First Duration /////////////////////////////
        aDur = JOptionPane.showInputDialog("Enter the first node activty duration then hit ENTER");

        int def = parse(aDur);
        activityDur.add(aDur);
        activityDuration.add(def);
    }

    public static void addActivity(){
        aName = JOptionPane.showInputDialog("Enter the additional node's activty name then hit ENTER (your input may not contain a period('.')");

        activityName.add(aName);

        System.out.print("Enter the additional node's activty duration then hit ENTER\n");
        aDur = JOptionPane.showInputDialog("Enter the additional node's activty duration then hit ENTER");

        int def = parse(aDur);
        activityDur.add(aDur);
        activityDuration.add(def);

        aDep = JOptionPane.showInputDialog("Enter the activity's list of dependencies separated by commas then hit ENTER(your input may not contain a period('.')(Ex. arc,bat,car)\n");		//dependencies input

        activityDependencies.add(aDep);

    }

    public static void orderLists(){

        for(int set=0;set<durationInOrder.size();++set){									//bubble sort cause i'm a rebel

            for(int set2=0;set2<durationInOrder.size();++set2){

                if(durationInOrder.get(set2)>=durationInOrder.get(set)){

                    int temps=durationInOrder.get(set);
                    durationInOrder.set(set,durationInOrder.get(set2));
                    durationInOrder.set(set2,temps);

                    String tempss= "" + pathsInOrder.get(set);
                    pathsInOrder.set(set,pathsInOrder.get(set2));
                    pathsInOrder.set(set2,tempss);
                }

            }
        }


        for(int set=0;set<criticalDurations.size();++set){									//bubble sort cause i'm a rebel

            for(int set2=0;set2<criticalDurations.size();++set2){

                if(criticalDurations.get(set2)>=criticalDurations.get(set)){

                    int temps=criticalDurations.get(set);
                    criticalDurations.set(set,criticalDurations.get(set2));
                    criticalDurations.set(set2,temps);

                    String tempss= "" + criticalPaths.get(set);
                    criticalPaths.set(set,criticalPaths.get(set2));
                    criticalPaths.set(set2,tempss);
                }

            }
        }
    }

    public static void calcDuration(){
        System.out.println("Enter");

        for(int u=0;u<pathsInOrder.size();++u){

            int tot=0;
            //System.out.print(pathsInOrder.get(u));

            //String temp = "." +pathsInOrder.get(u);

            String dotparts[] = pathsInOrder.get(u).split(">");

            System.out.println(dotparts.length);
            for(int i=0;i<dotparts.length;++i){
                //System.out.println(dotparts.length);
                for(int e=0;e<activityName.size();e++){
                    if(activityName.get(e).equals(dotparts[i])){
                        tot=tot+activityDuration.get(e);
                    }
                }
            }
            durationInOrder.set(u, tot);
        }

        for(int s=0;s<criticalPaths.size();++s){
            int tot=0;

            String doparts[] = criticalPaths.get(s).split(">");
            for(int t=0;t<doparts.length;++t){
                System.out.println(doparts[t]);
                for(int d=0;d<activityName.size();d++){
                    if(activityName.get(d).equals(doparts[t])){
                        tot=tot+activityDuration.get(d);
                    }
                }
            }
            criticalDurations.set(s, tot);
        }
    }

    public static void createPathList(){
        ///pathsInOrder

        for(int h=0;h<activityPaths.size();++h){
            if(activityPaths.get(h).contains("|")){
                String orparts[] = activityPaths.get(h).split(" | ");
                for(int x=0;x<orparts.length;++x){
                    if(!orparts[x].equals("|")){

                        if(h==finish){
                            criticalPaths.add(orparts[x]);
                            criticalDurations.add(0);
                        }
                        pathsInOrder.add(orparts[x]);
                        durationInOrder.add(0);
                    }
                }
            }
            else{
                if(h==finish){
                    criticalPaths.add(activityPaths.get(h));
                    criticalDurations.add(0);
                }
                pathsInOrder.add(activityPaths.get(h));
                durationInOrder.add(0);
            }
        }

    }



    public static void createPaths(){
        int start=0;
        boolean check;
/// finds out which node is the last node
        for(int k=0;k<activityName.size();++k){
            check=false;
            for(int i=0;i<activityDependencies.size();i++){
                String parts[] = activityDependencies.get(i).split(",");
                for(int j=0;j<parts.length;++j){
                    if(activityName.get(k).equals(parts[j])){
                        check=true;
                    }
                }
            }
            if(check==false){
                finish=k;
                System.out.print(k);
            }
        }

/////////////////////////////////////////////

///// sets up the verify arrayList that lets us see the nodes that have paths created for them and creates the paths
        activityVerification.add(1);
        activityPaths.add(activityName.get(0));
        for(int n=0;n<activityDependencies.size();++n){
            activityVerification.add(0);
            activityPaths.add("");
        }
        System.out.println(activityVerification);
        System.out.println(activityPaths);

        boolean ready;

///// works on getting all the paths generated
        while(activityVerification.contains(0)){
            for(int x=0;x<activityPaths.size();++x){
                System.out.println(x);
                if(activityVerification.get(x).equals(0)){
                    String parts[] = activityDependencies.get(x-1).split(",");
                    ready=true;
                    for(int v=0;v<parts.length;++v){
                        for(int d=0; d< activityName.size();++d){
                            if(activityName.get(d).equals(parts[v])){
                                if(activityVerification.get(d).equals(0)){
                                    ready=false;
                                }
                            }
                        }
                    }
                    if(ready==true){
                        String path="";
                        for(int m=0;m<parts.length;++m){
                            for(int c=0; c< activityName.size();++c){
                                if(activityName.get(c).equals(parts[m])){
                                    if(m==0){
                                        if(activityPaths.get(c).contains("|")){
                                            String orparts[] = activityPaths.get(c).split(" | ");
                                            for(int r=0;r<orparts.length;++r){
                                                //System.out.println(orparts[r]);
                                                if(!orparts[r].equals("|")){
                                                    if(r==0){
                                                        path=path +orparts[r] + ">" + activityName.get(x);
                                                    }
                                                    else{
                                                        path=path +" | "+orparts[r] + ">" + activityName.get(x);
                                                    }
                                                }
                                            }
                                        }
                                        else{
                                            path= path + activityPaths.get(c) + ">" + activityName.get(x);
                                        }
                                    }
                                    else{
                                        if(activityPaths.get(c).contains("|")){
                                            String orparts[] = activityPaths.get(c).split(" | ");
                                            for(int r=0;r<orparts.length;++r){
                                                System.out.println(orparts[r]);
                                                if(!orparts[r].equals("|")){
                                                    path=path +" | "+orparts[r] + ">" + activityName.get(x);
                                                }
                                            }
                                        }
                                        else{
                                            path=path + " | " + activityPaths.get(c) + ">" + activityName.get(x);
                                        }
                                    }
                                    System.out.println(path);
                                }
                            }
                        }
                        activityPaths.set(x, path);
                        activityVerification.set(x, 1);
                    }
                }
            }

        }

    }

    public static void handleDependencyError(){
        String parts[];
        for(int i=0; i<grandposition;i++){
            int x=0;
            //System.out.println(activityDependencies.get(i));
            System.out.println(activityName.size());
            System.out.println(activityDependencies.size());
            System.out.println(activityDependencies.get(i));
            parts = activityDependencies.get(i).split(",");
            System.out.println(parts[0]);
            String newString="";
            for(int j=0;j<parts.length;j++){
                System.out.println(parts.length);
                System.out.println(parts[j]);
                for(int k=0;k<=grandposition;k++){
                    if(activityName.get(i+1).equals(parts[j])){

                    }
                    else{
                        if(activityName.get(k).equals(parts[j])){
                            if(x==0){
                                x++;
                                newString=newString + activityName.get(k);
                            }
                            else{
                                newString=newString +","+ activityName.get(k);
                            }
                            System.out.println(newString);
                        }
                    }
                    System.out.print("name ");
                    System.out.println(activityName.get(k));
                }
            }
            activityDependencies.set(i, newString);
            if(activityDependencies.get(i).equals("")){
                activityDependencies.remove(i);
                activityName.remove(i+1);
                activityDuration.remove(i+1);
            }
        }
    }

}
