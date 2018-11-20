package ActDiagram;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main extends Application {
    static int count = 0;
    static int finish;
    public String allPaths;
    public String critPaths;
    public String nameStrOrder;
    public String notConnected;

    public ArrayList<Integer> activityVerification = new ArrayList<Integer>();
    public ArrayList<String> activityPaths = new ArrayList<String>();					//lists of all paths to each activity in the program
    public ArrayList<String> namesOrdered = new ArrayList<String>();					//order of paths in descending order (matched with durationInOrder)
    public ArrayList<Integer> durationOrdered = new ArrayList<Integer>();


    public ArrayList<String> pathsInOrder = new ArrayList<String>();					//order of paths in descending order (matched with durationInOrder)
    public ArrayList<Integer> durationInOrder= new ArrayList<Integer>();				//order of duration in descending oder(matched with pathsInOrder)
    public ArrayList<String> criticalPaths = new ArrayList<String>();
    public ArrayList<Integer> criticalDurations= new ArrayList<Integer>();

    Stage window;
    TableView<Nodes> table;
    TextField nameInput, durInput, depInput;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Activity Diagram - Inputs");
        //window.setMinWidth(1000);
        window.setMinHeight(500);
        window.setResizable(false);

        //TextArea to display the output and critical paths
        TextArea pathArea = new TextArea();
        pathArea.setMaxWidth(290);
        pathArea.setEditable(false);

        table = new TableView<>();
        table.setEditable(true);

        /**
         * Menu Bar Items
         */
        //File Menu
        Menu fileMenu = new Menu("File");

        MenuItem createItem = new MenuItem("Create");
        createItem.setOnAction(e -> createClicked());
        fileMenu.getItems().add(createItem);
        fileMenu.getItems().add(new SeparatorMenuItem());

        MenuItem overOpt = new MenuItem("Overview");
        overOpt.setOnAction(e -> overviewClicked());
        fileMenu.getItems().add(overOpt);
        MenuItem help = new MenuItem("Help");
        help.setOnAction(e -> helpClicked());
        fileMenu.getItems().add(help);
        fileMenu.getItems().add(new SeparatorMenuItem());

        MenuItem restartOpt = new MenuItem("Restart...");
        restartOpt.setOnAction(e -> restart());
        fileMenu.getItems().add(restartOpt);
        MenuItem exitOpt = new MenuItem("Exit...");
        exitOpt.setOnAction(e -> exitProg());
        fileMenu.getItems().add(exitOpt);

        //Options Menu
        Menu optionsMenu = new Menu("Options");
        CheckMenuItem criticalItem = new CheckMenuItem("Show Critical Paths");
        criticalItem.setOnAction(e -> {
            if(criticalItem.isSelected()) {
                pathArea.setText(critPaths);
            }else{
                pathArea.setText(allPaths);
            }
        });
        optionsMenu.getItems().add(criticalItem);
        optionsMenu.getItems().add(new SeparatorMenuItem());

        MenuItem deleteItem = new MenuItem("Delete Row");
        deleteItem.setOnAction(e -> deleteButtonClicked());
        optionsMenu.getItems().add(deleteItem);

        //Main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, optionsMenu);

        /**
         * Column Items
         */
        //Name column
        TableColumn nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setSortable(false);
        nameColumn.setResizable(false);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Nodes, String>("name"));

        //Price column
        TableColumn depColumn = new TableColumn<>("Dependencies");
        depColumn.setMinWidth(200);
        depColumn.setSortable(false);
        depColumn.setResizable(false);
        depColumn.setCellValueFactory(new PropertyValueFactory<Nodes, String>("depend"));

        //Quantity column
        TableColumn<Nodes, Integer> durColumn = new TableColumn<>("Duration");
        durColumn.setMinWidth(200);
        durColumn.setSortable(false);
        durColumn.setResizable(false);
        durColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        // Mekes the duration editable
        durColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
            @Override
            public Integer fromString(String value){
                try{
                    return super.fromString(value);
                }catch (NumberFormatException e){
                    error(1);
                    return 0;
                }
            }
        }));
        durColumn.setEditable(true);

        durColumn.setOnEditCommit((CellEditEvent<Nodes, Integer> event) -> {
            TablePosition<Nodes, Integer> pos = event.getTablePosition();
            Integer newDuration = event.getNewValue();
            int row = pos.getRow();
            Nodes nodes = event.getTableView().getItems().get(row);
            nodes.setDuration(newDuration);
            finishButtonClicked();
        });

        table.getColumns().addAll(nameColumn, depColumn, durColumn);

        /**
         * Text fields for the user to enter values
         */
        //Name input
        nameInput = new TextField();
        nameInput.setPromptText("Name");
        nameInput.setTooltip(
                new Tooltip("Type the name of your activity here")
        );
        nameInput.setMinWidth(100);

        //Quantity input
        depInput = new TextField();
        depInput.setPromptText("Dependencies");
        depInput.setTooltip(
                new Tooltip("Type the dependencies of the activity here")
        );
        depInput.setMinWidth(100);
        depInput.setEditable(false);

        //Price input
        durInput = new TextField();
        durInput.setPromptText("Duration");
        durInput.setTooltip(
                new Tooltip("Type the duration of the activity here (Input must be an integer)")
        );
        durInput.setMinWidth(100);

        /**
         * Buttons Displayed on the main window
         */
        //Buttons
        Button addButton = new Button("Add");
        addButton.setMinWidth(100);
        addButton.setStyle(
                "-fx-border-color: white; -fx-background-color: darkgreen; " +
                "-fx-text-fill: white; -fx-font-weight: bolder");
        addButton.setOnAction(e -> addButtonClicked());
        addButton.setTooltip(
                new Tooltip("Click here when you want to add a new node")
        );

        Button finishButton = new Button("Finish");
        finishButton.setMinWidth(100);
        finishButton.setStyle(
                "-fx-border-color: white; -fx-background-color: darkblue; " +
                        "-fx-text-fill: white; -fx-font-weight: bolder");
        finishButton.setOnAction(e -> {
            finishButtonClicked();
            pathArea.setText(""+allPaths);
        });
        finishButton.setTooltip(
                new Tooltip("Click here when you are finished entering nodes")
        );

        //Sets Layout for the text fields
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,20,10,20));
        hBox.setSpacing(20);
        hBox.setStyle("-fx-background-color: silver");
        hBox.setMinHeight(60);
        hBox.setAlignment(Pos.CENTER_LEFT);

        //Sets Layout for the buttons
        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(10,20,10,50));
        hBox2.setSpacing(20);
        hBox2.setStyle("-fx-background-color: silver");
        hBox2.setMinHeight(60);
        hBox2.setAlignment(Pos.CENTER_RIGHT);
        hBox2.getChildren().addAll( addButton, finishButton);

        //Sets the layout for the buttons and text fields displayed at the bottom
        hBox.getChildren().addAll(nameInput, depInput, durInput, hBox2);

        //Sets the layout for the main window
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setLeft(table);
        borderPane.setBottom(hBox);
        borderPane.setRight(pathArea);

        Scene scene = new Scene(borderPane);
        //scene.setFill(Color.BLACK);
        window.setOnCloseRequest(e -> {
            e.consume();
            exitProg();
        });

        window.setScene(scene);
        window.show();
    }

    //Add button clicked
    public void addButtonClicked(){
        try {
            Nodes nodes = new Nodes();
            if(nameInput.getText().isEmpty()) {
                error(2);
            }else if (depInput.getText().isEmpty() && count != 0) {
                error(3);
            }else if(durInput.getText().isEmpty()) {
                error(4);
            }else if (depInput.getText().contains(".") || nameInput.getText().contains(".")){
                error(7);
                depInput.clear();
                nameInput.clear();
            }else {
                nodes.setName(nameInput.getText());
                nodes.setDepend(depInput.getText());
                int durCheck = Integer.parseInt(durInput.getText());
                nodes.setDuration(durCheck);
                table.getItems().add(nodes);
                nameInput.clear();
                depInput.clear();
                durInput.clear();
                depInput.setEditable(true);
                count++;
            }

        } catch (NumberFormatException e){
            error(1);
            durInput.clear();
        }
    }

    //Delete menu item clicked
    public void deleteButtonClicked(){
        ObservableList<Nodes> nodeSelected, allNodes;
        allNodes = table.getItems();
        nodeSelected = table.getSelectionModel().getSelectedItems();

        nodeSelected.forEach(allNodes::remove);
        count--;
    }

    public void finishButtonClicked(){
        if(count==0){
            error(8);
        }else {

            Nodes nodes = new Nodes();

            ArrayList<String> arrName = new ArrayList<>();
            ArrayList<String> arrDep = new ArrayList<>();
            ArrayList<Integer> arrDur = new ArrayList<>();


            //Gets the values in the Name Column
            for (int i = 0; i < table.getItems().size(); i++) {
                nodes = table.getItems().get(i);
                arrName.add(nodes.getName());
            }
            //Gets the values in the Dependencies Column
            for (int i = 1; i < table.getItems().size(); i++) {
                nodes = table.getItems().get(i);
                arrDep.add(nodes.getDepend());
            }
            //Gets the values in the Duration Column
            for (int i = 0; i < table.getItems().size(); i++) {
                nodes = table.getItems().get(i);
                arrDur.add(nodes.getDuration());
            }

            for (int i = 0; i < arrName.size(); i++) {
                System.out.println(arrName.get(i));
                //System.out.println(arrDep.get(i));
                System.out.println(arrDur.get(i));
            }
            handleDependencyError(arrName, arrDep, arrDur);
            createPaths(arrName, arrDep, arrDur);
            createPathList();
            calcDuration(arrName, arrDur);
            orderLists();

            allPaths = "All Paths and Durations \nin Descending Order:\n";
            for (int test2 = durationInOrder.size() - 1; test2 >= 0; test2--) {                            //printing of ALL path information;
                allPaths = allPaths + pathsInOrder.get(test2) + "    " + durationInOrder.get(test2) + "\n";

            }

            critPaths = "Critical Paths and Durations \nin Descending Order:\n";
            for (int test2 = criticalDurations.size() - 1; test2 >= 0; test2--) {                            //printing of ALL path information;
                critPaths = critPaths + criticalPaths.get(test2) + "    " + criticalDurations.get(test2) + "\n";

            }

            alphaNumeric(arrName, arrDur);

            namesOrdered.clear();
            durationOrdered.clear();
            activityVerification.clear();
            activityPaths.clear();
            pathsInOrder.clear();
            durationInOrder.clear();
            criticalPaths.clear();
            criticalDurations.clear();
        }
    }

    //Create menu item clicked
    public void createClicked(){
        Stage cWindow = new Stage();
        cWindow.initModality(Modality.APPLICATION_MODAL);
        cWindow.setTitle("Create Report");
        cWindow.setMinWidth(500);
        cWindow.setMinHeight(250);

        //Label for the title
        Label tlabel = new Label();
        tlabel.setText("Report Title: ");

        //TextField for the title
        TextField titleInput = new TextField();
        titleInput.setPromptText("Title");
        titleInput.setTooltip(
                new Tooltip("Type the title of the document you want to create here")
        );
        titleInput.setMinWidth(100);

        //Title label and text field added to HBox
        HBox hBox1 = new HBox(5);
        hBox1.setPadding(new Insets(0,20,0,20));
        hBox1.setAlignment(Pos.CENTER);
        hBox1.getChildren().addAll(tlabel, titleInput);

        //Creating the date
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String formattedDate = dateFormat.format(date);

        //Label for the date
        Label dlabel = new Label();
        dlabel.setText("Current Date & Time: " + formattedDate);
        dlabel.setTooltip(
                new Tooltip("The current date and time")
        );

        //Create two buttons
        Button backButton = new Button("Back");
        backButton.setMinWidth(100);

        Button createButton = new Button("Create");
        createButton.setMinWidth(100);

        //Stage and scene for when the user successfully creates a text file
        Stage sWindow = new Stage();
        sWindow.initModality(Modality.APPLICATION_MODAL);
        sWindow.setTitle("Create Report");
        sWindow.setMinWidth(250);
        sWindow.setMinHeight(200);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.setSpacing(20);

        createButton.setOnAction(e -> {
            //Converts the title to a String
            String titleConvert = String.valueOf(titleInput.getText());
            if(titleConvert.isEmpty()){
                error(5);
            }else {
                writeToFile(titleConvert, formattedDate);
                cWindow.close();

                Label slabel = new Label();
                slabel.setText("File " + titleConvert + " created successfully!");
                slabel.setStyle("-fx-text-fill: black; -fx-font-size: 16px;");

                vBox.getChildren().addAll(slabel, backButton);
                vBox.setAlignment(Pos.CENTER);

                Scene sceneS = new Scene(vBox);
                sWindow.setScene(sceneS);
                sWindow.showAndWait();
            }
        });

        HBox hBox2 = new HBox(10);
        hBox2.setPadding(new Insets(10,20,10,20));
        hBox2.setAlignment(Pos.CENTER);
        hBox2.getChildren().addAll(backButton, createButton);
        VBox vBox2 = new VBox(10);
        vBox2.setPadding(new Insets(10,20,10,20));

        //Clicking will set answer and close window
        backButton.setOnAction(e -> {
            cWindow.close();
            sWindow.close();
        });

        //Add buttons
        vBox2.getChildren().addAll(hBox1, dlabel, hBox2);
        vBox2.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox2);
        cWindow.setScene(scene);
        cWindow.showAndWait();
    }

    public void writeToFile(String title, String date) {
        try {
            String filename = title + ".txt";
            String str = date;
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write("Created at: " + date + "\n\n"+ nameStrOrder+"\n\n"+ allPaths);
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
            error(6);
        }
    }

    public void overviewClicked(){
        Stage oWindow = new Stage();
        oWindow.initModality(Modality.APPLICATION_MODAL);
        oWindow.setTitle("Activity Diagram Overview");
        oWindow.setMinWidth(500);
        oWindow.setMinHeight(250);

        Label olabel = new Label();
        olabel.setText("Created By: David Diner, Tyler Lang, John Oper, Gavin Vandine");

        TextArea textArea = new TextArea();
        textArea.setText("This program manages an array list for nodes based off of the user input into a GUI.\n" +
                "The user tells the program the node name, its duration, and the predecessors \n" +
                "of the nodes. Our program uses an array list to organize nodes with their durations \n" +
                "and predecessors. With this information the program then creates a graph to show \n" +
                "the nodes with a visual within the GUI. The program allows the user to change and \n" +
                "edit nodes after inputting. The program allows you to view the graph for a given \n" +
                "input, as long as the user inputs the nodes into the GUI. The program also allows \n" +
                "the user to create .txt reports that list the paths and activities.\n"
        );
        textArea.setMinHeight(200);
        textArea.setMinWidth(450);
        textArea.setEditable(false);

        //Create two buttons
        Button backButton = new Button("Back");
        backButton.setMinWidth(100);

        //Clicking will set answer and close window
        backButton.setOnAction(e -> {
            oWindow.close();
        });

        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(10,20,10,20));
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(backButton);
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10,20,10,20));

        //Add buttons
        vBox.getChildren().addAll(olabel, textArea, hBox);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        oWindow.setScene(scene);
        oWindow.showAndWait();
    }

    public void helpClicked(){
        Stage hWindow = new Stage();
        hWindow.initModality(Modality.APPLICATION_MODAL);
        hWindow.setTitle("Help");
        hWindow.setMinWidth(500);
        hWindow.setMinHeight(250);

        Label hlabel = new Label();
        hlabel.setText("Program Help");

        TextArea textArea = new TextArea();
        textArea.setText(
                "***** Starting the Program *****\n" +
                "   - To start the program click Run.\n" +
                "***** Adding Nodes *****\n" +
                "   - For the first node type in an activity name and activity duration, then click the\n" +
                "     add button. You then should see the name and duration displayed on the table.\n" +
                "   - For the rest of the nodes, enter the activity name, duration, and dependencies,\n" +
                "     then click the 'Add' button. You shoudl see the activity name, duration, and \n" +
                "     dependencies once this is done.\n" +
                "   - Note, the duration must be entered as an integer\n"+
                "***** Finished Adding Nodes *****\n"+
                "   - When you are finished adding nodes, click the 'finish' button. This will display\n"+
                "     all the activities paths and durations in descending order of duration to the\n"+
                "     right of the table.\n" +
                "***** Showing the Critical Paths *****\n"+
                "   - To show the critical paths, you must have first clicked the 'finish' button.\n"+
                "   - Click the options button in the top menu and then click 'Show Critical Paths'.\n"+
                "     The 'Show Critical Paths' option will have a check mark when it is selected.\n"+
                "   - The critical path will be shown to the right of the table.\n" +
                "   - Click the 'Show Critical Paths' option again to show all the paths.\n" +
                "***** Changing the Duration *****\n"+
                "   - To change the duration, double click on the duration column and row that \n"+
                "     you would like to edit. Then type in a new integer value into the row and\n"+
                "     click enter. If an integer isn't entered, then the row will be set to 0\n"+
                "   - You can make changes to more than one duration if you wish. \n"+
                "   - Once you are finished editing the duration, click finish to show the new\n"+
                "     paths and the new critical paths."+
                "***** Deleting Rows *****\n"+
                "   - Click on the selected row you would like to delete and then click 'Options',\n" +
                "     located on the top menu bar, after that you click delete and the row will\n" +
                "     be deleted.\n"+
                "***** Creating Text Files *****\n" +
                "   - The finish button must be clicked before creating a text file, or else the\n"+
                "     text file will not contain any values.\n"+
                "   - To create a text file click the 'File' button, located on the top left\n"+
                "     of the screen, then click 'Create'.\n" +
                "   - Type in the name of the file that you would like and then click 'Create'.\n" +
                "   - Your file has then been created and you will be able to view it once you\n" +
                "     exit the program.\n"+
                "   - Click the 'Back' button if you do not wish to create a text file and would\n" +
                "     like to return to the main page.\n"+
                "***** Overview *****\n" +
                "   - To view the program overview, click 'File' and then click 'Overview'.\n" +
                "***** Restarting the Program *****\n" +
                "   - To restart the program, click 'File' and then click 'Restart'.\n"+
                "   - Once 'Restart' is clicked, a box will pop up asking if you are sure\n"+
                "     you want to restart. Click 'Yes' if you would like to restart.\n"+
                "     Click 'No' if you do not wish to restart and you would like to return\n"+
                "     to the previous page.\n"+
                "***** Exiting/Quitting the Program *****\n"+
                "   - To exit/quit the program click the exit button at the top of the window,\n"+
                "     or click 'File' then 'Exit'.\n"+
                "   - Once 'Exit' is clicked, you will be asked if you would like to exit.\n"+
                "     click 'Yes' if you would like to exit. Otherwise click 'No', where you\n"+
                "     will be returned to the previous page.\n"

        );

        textArea.setMinHeight(200);
        textArea.setMinWidth(450);
        textArea.setEditable(false);

        //Create two buttons
        Button backButton = new Button("Back");
        backButton.setMinWidth(100);

        //Clicking will set answer and close window
        backButton.setOnAction(e -> {
            hWindow.close();
        });

        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(10,20,10,20));
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(backButton);
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10,20,10,20));

        //Add buttons
        vBox.getChildren().addAll(hlabel, textArea, hBox);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        hWindow.setScene(scene);
        hWindow.showAndWait();
    }

    public void error(int errNum){
        Stage eWindow = new Stage();

        eWindow.initModality(Modality.APPLICATION_MODAL);
        eWindow.setTitle("ERROR MESSAGE");
        eWindow.setMinWidth(500);
        eWindow.setMinHeight(250);
        VBox vBox = new VBox(20);

        Button backButton = new Button("Back");
        backButton.setMinWidth(100);
        backButton.setOnAction(e1 -> eWindow.close());

        if(errNum == 1) {
            Label elabel = new Label();
            elabel.setText("ERROR: You must enter an integer into the duration.");
            elabel.setStyle("-fx-text-fill: firebrick; -fx-font-size: 16px;");
            vBox.getChildren().addAll(elabel, backButton);
        }
        if(errNum == 2) {
            Label elabel = new Label();
            elabel.setText("ERROR: You must enter a value into the activity name.");
            elabel.setStyle("-fx-text-fill: firebrick; -fx-font-size: 16px;");
            vBox.getChildren().addAll(elabel, backButton);
        }
        if(errNum == 3) {
            Label elabel = new Label();
            elabel.setText("ERROR: You must enter a value into the activity dependency.");
            elabel.setStyle("-fx-text-fill: firebrick; -fx-font-size: 16px;");
            vBox.getChildren().addAll(elabel, backButton);
        }
        if(errNum == 4) {
            Label elabel = new Label();
            elabel.setText("ERROR: You must enter a value into the activity duration.");
            elabel.setStyle("-fx-text-fill: firebrick; -fx-font-size: 16px;");
            vBox.getChildren().addAll(elabel, backButton);
        }
        if(errNum == 5) {
            Label elabel = new Label();
            elabel.setText("ERROR: You must enter a title before creating a new file");
            elabel.setStyle("-fx-text-fill: firebrick; -fx-font-size: 16px;");
            vBox.getChildren().addAll(elabel, backButton);
        }
        if(errNum == 6) {
            Label elabel = new Label();
            elabel.setText("ERROR: IOException");
            elabel.setStyle("-fx-text-fill: firebrick; -fx-font-size: 16px;");
            vBox.getChildren().addAll(elabel, backButton);
        }
        if(errNum == 7) {
            Label elabel = new Label();
            elabel.setText("ERROR: You cannot enter periods");
            elabel.setStyle("-fx-text-fill: firebrick; -fx-font-size: 16px;");
            vBox.getChildren().addAll(elabel, backButton);
        }
        if(errNum == 8) {
            Label elabel = new Label();
            elabel.setText("ERROR: You must enter an activity before clicking finish");
            elabel.setStyle("-fx-text-fill: firebrick; -fx-font-size: 16px;");
            vBox.getChildren().addAll(elabel, backButton);
        }
        if(errNum == 9) {
            Label elabel = new Label();
            elabel.setText("ERROR: Node " + notConnected + " is not connect");
            elabel.setStyle("-fx-text-fill: firebrick; -fx-font-size: 16px;");
            vBox.getChildren().addAll(elabel, backButton);
        }
        if(errNum == 10) {
            Label elabel = new Label();
            elabel.setText("ERROR: Node infinite cycle, restarting program...");
            elabel.setStyle("-fx-text-fill: firebrick; -fx-font-size: 16px;");
            vBox.getChildren().addAll(elabel, backButton);
        }
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.setAlignment(Pos.CENTER);

        Scene eScene = new Scene(vBox);
        eWindow.setScene(eScene);
        eWindow.showAndWait();
    }

    public void restart() {
        Stage rWindow = new Stage();
        rWindow.initModality(Modality.APPLICATION_MODAL);
        rWindow.setTitle("Restart");
        rWindow.setMinWidth(250);
        rWindow.setMinHeight(150);
        Label rlabel = new Label();
        rlabel.setText("Are you sure you want to restart?");
        rlabel.setStyle("-fx-text-fill: black; -fx-font-size: 16px;");

        //Create two buttons
        Button yesButton = new Button("Yes");
        yesButton.setMinWidth(100);

        Button noButton = new Button("No");
        noButton.setMinWidth(100);

        //Clicking will set answer and close window
        yesButton.setOnAction(e -> {
            ObservableList<Nodes> rNodes;
            table.getItems().clear();
            rWindow.close();
            count = 0;
            depInput.setEditable(false);

            namesOrdered.clear();
            durationOrdered.clear();
            activityVerification.clear();
            activityPaths.clear();
            pathsInOrder.clear();
            durationInOrder.clear();
            criticalPaths.clear();
            criticalDurations.clear();
        });
        noButton.setOnAction(e -> {
            rWindow.close();
        });

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,20,10,20));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(yesButton, noButton);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.getChildren().addAll(rlabel, hBox);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        rWindow.setScene(scene);
        rWindow.showAndWait();
    }

    public void exitProg() {
        Stage eWindow = new Stage();
        eWindow.initModality(Modality.APPLICATION_MODAL);
        eWindow.setTitle("Exit Program");
        eWindow.setMinWidth(250);
        eWindow.setMinHeight(150);
        Label elabel = new Label();
        elabel.setText("Are you sure you want to exit?");
        elabel.setStyle("-fx-text-fill: black; -fx-font-size: 16px;");

        //Create two buttons
        Button yesButton2 = new Button("Yes");
        yesButton2.setMinWidth(100);

        Button noButton2 = new Button("No");
        noButton2.setMinWidth(100);

        //Clicking will set answer and close window
        yesButton2.setOnAction(e -> {
            eWindow.close();
            window.close();
        });
        noButton2.setOnAction(e -> {
            eWindow.close();
        });

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,20,10,20));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(yesButton2, noButton2);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.getChildren().addAll(elabel, hBox);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        eWindow.setScene(scene);
        eWindow.showAndWait();
    }

    public void handleDependencyError(ArrayList<String> activityName, ArrayList<String> activityDependencies, ArrayList<Integer> activityDuration){
        String parts[];
        boolean check;
        for(int i=0; i< activityDependencies.size();i++){
            int x=0;
            //System.out.println(activityDependencies.get(i));
            parts = activityDependencies.get(i).split(",");
            String newString="";
            check=false;
            for(int j=0;j<parts.length;j++){

                for(int k=0;k<=activityDependencies.size();k++){

                    if(activityName.get(i+1).equals(parts[j])){

                    }
                    else{
                        if(activityName.get(k).equals(parts[j])){
                            check=true;
                            if(x==0){
                                x++;
                                newString=newString + activityName.get(k);
                            }
                            else{
                                newString=newString +","+ activityName.get(k);
                            }
                        }
                    }
                }
            }
            if(check==false){
                notConnected = activityName.get(i+1);
                error(9);
            }
            activityDependencies.set(i, newString);
            if(activityDependencies.get(i).equals("")){
                activityDependencies.remove(i);
                activityName.remove(i+1);
                activityDuration.remove(i+1);
            }
        }
    }

    public void createPaths(ArrayList<String> activityName, ArrayList<String> activityDependencies, ArrayList<Integer> activityDuration){
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
        int count = 0;
///// works on getting all the paths generated
        while(activityVerification.contains(0) && count<1000){
            for(int x=0;x<activityPaths.size();++x){
                count++;
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
        if (count >= 1000){
            error(10);
            restart();
        }
    }

    public void createPathList(){

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

    public void calcDuration(ArrayList<String> activityName, ArrayList<Integer> activityDuration){
        System.out.println("Enter");

        for(int u=0;u<pathsInOrder.size();++u){

            int tot=0;

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

    public void orderLists(){

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

    public void alphaNumeric(ArrayList<String> activityName, ArrayList<Integer> activityDuration){
        namesOrdered.addAll(activityName);
        durationOrdered.addAll(activityDuration);
        String temp;
        int temp2;
        for(int i=0;i<namesOrdered.size();i++){
            for(int j=i+1;j<namesOrdered.size();j++){
                if(namesOrdered.get(i).compareTo(activityName.get(j))>=0){
                    temp = namesOrdered.get(i);
                    namesOrdered.set(i,namesOrdered.get(j));
                    namesOrdered.set(j, temp);

                    temp2 = durationOrdered.get(i);
                    durationOrdered.set(i,durationOrdered.get(j));
                    durationOrdered.set(j, temp2);
                }
            }
        }
        nameStrOrder = ("Alphanumeric Order of Names and Duration: \n"+namesOrdered+"\n"+durationOrdered);
    }

}