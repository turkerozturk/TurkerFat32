package com.turkerozturk.desktop;

/***********************************************************************
 * Copyright 2023 Turker Ozturk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***********************************************************************/



import java.io.File;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.turkerozturk.library.fat32.Fat32Partition;
import com.turkerozturk.library.fat32.FileSystemInformationNode;
import com.turkerozturk.library.fat32.FileSystemMetaDataNode;
import com.turkerozturk.library.fat32.ForensicDiskImageNode;
import com.turkerozturk.library.fat32.FsNode;
import com.turkerozturk.library.fat32.UnallocatedSpaceNode;
import com.turkerozturk.library.fat32.enums.EntryType;
import com.turkerozturk.library.fat32.enums.FsNodeFileSystemType;
import com.turkerozturk.library.fat32.helpers.OtherHelper;
import com.turkerozturk.library.fat32.structures.UnallocatedSpaceContainerNode;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



/*
 TO RUN THIS APP, DOWNLOAD JAVAFX LIB AND ADD THIS VM OPTIONS BELOW CONTAINING ITS PATH:
  --module-path "C:\Users\\u\Downloads\APPP\openjfx-19.0.2.1_windows-x64_bin-sdk\javafx-sdk-19.0.2.1\lib" --add-modules javafx.controls,javafx.fxml
*/

/**
 *
 * @author Turker Ozturk
 * @since 2018-03-02
 */
public class Fat32Gui extends Application {

    private Object lastSelectedObject = new FsNode();

    Fat32Partition partition;

    private TreeView<Object> treeView;
    // private TableView table;
    //https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
    private TableView<Object> table;// = new TableView<>();
    TableColumn nameTableColumn;
    TableColumn sizeTableColumn;
    TableColumn sizeAsByteTableColumn;
    TableColumn typeTableColumn;
    TableColumn dateModifiedTableColumn;
    TableColumn deletedTableColumn;
    TableColumn absolutePathTableColumn;
    TableColumn firstDataClusterIdOfFileTableColumn;

    Button buttonExtractSelected;
    TextArea textArea;
    GridPane gridPane;
    int gridMiddleX = 8; //TODO
    int gridMiddleY = 9; //TODO

    int usttekilerMinHeight = 450;

    Button button;

    //@FXML
//    private WebView webview;

    //@FXML
//    private WebEngine webengine;

   // private String logicalDiskImageUri = "C:\\tmp\\fat32logical.img";
  //  private String logicalDiskImageUri = "C:\\tmp\\fat32images\\1gblogicalfat32.dig"; // gercek 1 gb bellek vardi sonra kayboldu, o imaj. bu konsola pathleri yazar ama sonra Directory.java:143 kodumda arraycopy: length -32 is negative hatası verir.
  //TODO BU CALISIYOR
    private String logicalDiskImageUri;// = "C:\\tmp\\fat32images\\USBLogicalFAT321GBFSF.dig"; // bu calisiyor problemsiz. benim olusturdugum imaj.


    TabPane tabPane;
    Tab tab0;
    Tab tab;
    Tab tab2;
    Tab tab3;
    Tab tab4;

    TextArea textAreaPartitionInformation;
    TextArea textAreaFsNodeHelp;
    TextArea textAreaTodo;

    //Image nodeImage = new Image(getClass().getResourceAsStream("duke_16x16.png"));
    @Override
    public void start(Stage primaryStage) throws IOException {



        // </editor-fold>
        // <editor-fold desc="Tab Panosu" defaultstate="collapsed">
        tabPane = new TabPane();

        tab0 = new Tab();
        tab0.setText("Open Image");
        //tab.setContent(new Rectangle(200,200, Color.LIGHTSTEELBLUE));
        tabPane.getTabs().add(tab0);

        tab = new Tab();
        tab.setText("Browser");
        //tab.setContent(new Rectangle(200,200, Color.LIGHTSTEELBLUE));
        tabPane.getTabs().add(tab);


        // <editor-fold desc="Dosya Secici" defaultstate="collapsed">
        FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Click Here to browse your logical FAT32 partition iamage file..\r\n" +
                 "You will see its contents on \"Browser\" tab.)");
        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        fileChooser.setTitle("Open FAT32 Partition(Logical) Image");
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            logicalDiskImageUri = file.getAbsolutePath();
                            try {
                                baslaParsellemeye(primaryStage);

                                //https://stackoverflow.com/questions/19025268/javafx-tabpane-switch-tabs-only-when-focused
                                //TabPaneSkin skin = (TabPaneSkin) tabPane.getSkin(); //https://stackoverflow.com/questions/19048127/java-8-javafx-getbehavior-alternative
                                //or BehaviorSkinBase skin = (BehaviorSkinBase) tabPane.getSkin();
                                //TabPaneBehavior tabPaneBehavior = (TabPaneBehavior) tabPane.getSkin()..getBehavior();
                                //tabPaneBehavior.selectNextTab();
                                tabPane.getSelectionModel().select(1);



                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                    }
                }
        );
        tab0.setContent(openButton);




        /*
        Tab tab5 = new Tab();
        tab5.setText("Methods Help");
        //tab5.setContent(new Rectangle(200,200, Color.LIGHTSTEELBLUE));
        tabPane.getTabs().add(tab5);
        //WebView - to display, browse web pages.
        this.webview = new WebView();
        this.webview.setMaxWidth(800);
        this.webview.setMaxHeight(500);
        this.webengine = this.webview.getEngine();
        //this.webengine.load("http://www.oracle.com/us/products/index.html");
        //this.webengine.load("/fat32help.html");
        //URL url = this.getClass().getResource("fat32help.html");
        //webengine.load(url.toString());
        //Or you can load the actual String path into a File object and use it to get the String URL.
        File f = new File("D:\\netbeansprojem\\JavaFXApplication1\\src\\resources\\fat32help.html");
        webengine.load(f.toURI().toString());
        tab5.setContent(this.webview);
         */

        // </editor-fold>

        // <editor-fold desc="Dosya Klasor Tablosu" defaultstate="collapsed">
        //NOT: tum sutunlari eklemek zorunda degiliz viewa. Yani burada tanimlariz fakat istemiyorsak eklemeyiz.
        table = new TableView();

        nameTableColumn = new TableColumn("Name");
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameTableColumn.setMinWidth(240);

        sizeTableColumn = new TableColumn("Size");
        sizeTableColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeTableColumn.setMinWidth(50);
        sizeTableColumn.setMaxWidth(50);

        //size.setCellValueFactory(new PropertyValueFactory<FsNode, Long>("sizeTableColumn")); Buna gerek yokmuş. zaten hangi classı koysamiçinde o isim varsa problemsiz çalışmasından anlamalıydım.

        sizeAsByteTableColumn = new TableColumn("Size as Byte");
        sizeAsByteTableColumn.setMinWidth(50);
        sizeAsByteTableColumn.setCellValueFactory(new PropertyValueFactory<>("sizeAsBytes"));

        typeTableColumn = new TableColumn("Type");
        typeTableColumn.setMinWidth(120);
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("entryType"));

        dateModifiedTableColumn = new TableColumn("Date Modified");
        dateModifiedTableColumn.setMinWidth(200);
        dateModifiedTableColumn.setCellValueFactory(new PropertyValueFactory<>("writtenDate"));

        deletedTableColumn = new TableColumn("Deleted");
        deletedTableColumn.setCellValueFactory(new PropertyValueFactory<>("deleted"));

        absolutePathTableColumn = new TableColumn("Absolute Path");
        absolutePathTableColumn.setCellValueFactory(new PropertyValueFactory<>("absolutePath"));

        firstDataClusterIdOfFileTableColumn = new TableColumn("Cluster Id");
        firstDataClusterIdOfFileTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstDataClusterIdOfFile"));

        typeTableColumn.setSortType(TableColumn.SortType.ASCENDING);
        table.getSortOrder().add(typeTableColumn);
        nameTableColumn.setSortType(TableColumn.SortType.ASCENDING);
        table.getSortOrder().add(nameTableColumn);

        // </editor-fold>
        // <editor-fold desc="Grid Panosu" defaultstate="collapsed">
        //  https://www.tutorialspoint.com/javafx/javafx_css.htm
        //Creating a Grid Pane
        gridPane = new GridPane();
        //Setting sizeTableColumn for the pane
        // gridPane.setMinSize(1200, 800);
        //Setting the padding
        gridPane.setPadding(new Insets(0, 0, 0, 0));
        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(1);
        gridPane.setHgap(1);
        //Setting the Grid alignment
        gridPane.setAlignment(Pos.TOP_LEFT);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(300);
       // cc.setPercentWidth(100/2);
        gridPane.getColumnConstraints().add(cc);
        // </editor-fold>

        final Label label = new Label("Fat32 Logical Image Analyzer");

        //A button with the specified text caption.
        button = new Button();
        button.setText("Clear Table");

        buttonExtractSelected = new Button();
        buttonExtractSelected.setText("Extract Selected");

        textArea = new TextArea();

        table.setMinSize(680, 400);
        //table.setMaxHeight(250);
        textArea.setMinSize(680, 360);
        //textArea.scroll
        //textArea.setMaxHeight(400);
        gridPane.add(textArea, gridMiddleX, gridMiddleY, 1, 1);
        //gridPane.add(button, 0, 8, 1, 1);
        //gridPane.add(buttonExtractSelected, 0, 9, 1, 1);

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        hbox.getChildren().addAll(button, buttonExtractSelected);//, openButton);

        Scene scene = new Scene(tabPane, 910, 670);

        // primaryStage.setWidth(1000);
        // primaryStage.setHeight(600);
        primaryStage.setTitle("Fat32 Analyzer - TurkerFat32");
        primaryStage.setScene(scene);
        // primaryStage.setScene(scene);

        gridPane.add(this.table, gridMiddleX, 0, 1, 1);

        gridPane.add(hbox, 0, gridMiddleY, 2, 2);

        //gridPane.add(label, 2, 2);
        //layout.getChildren().add(label);
        //layout.getChildren().add(table);
        //Scene scene = new Scene(layout, 300, 250);
        // Scene scene = new Scene(gridPane, 1300, 850);
        tab.setContent(gridPane);

        //y(partition);
        //TODO http://www.kware.net/?p=204
        //Filtering a JavaFX TreeView
        //Mesela Treedeki dosyalari saklamak istiyorum ama table tarafinda gorunmeleri lazim.
        //Halbuki javafxde saklamak/filtering olmayip remove etmek var ki isime yaramaz oyleyse.
        //Bu sebeple yukaridaki linklerde yazilanlari oku, isine yariyorsa kullan.
        /*
        //root of slack space
        FsNode slackRootFsNode = new FsNode();
        slackRootFsNode.setName("Root Of Slack Spaces");
        TreeItem<FsNode> slackRootTreeItem;
        slackRootTreeItem = new TreeItem<>();
        slackRootTreeItem.setValue(rootFsNode);
         */

        primaryStage.show();

        // primaryStage.close();
    }


    public void baslaParsellemeye(Stage primaryStage) throws IOException {
        tab0 = tabPane.getTabs().get(0);
        tab = tabPane.getTabs().get(1);


        tab2 = new Tab();
        tab2.setText("Filesystem Info");
        //tab2.setContent(new Rectangle(200,200, Color.LIGHTSTEELBLUE));
        tabPane.getTabs().add(tab2);

        tab3 = new Tab();
        tab3.setText("FsNode Help");
        //tab2.setContent(new Rectangle(200,200, Color.LIGHTSTEELBLUE));
        tabPane.getTabs().add(tab3);

        tab4 = new Tab();
        tab4.setText("FsNode TODO");
        //tab2.setContent(new Rectangle(200,200, Color.LIGHTSTEELBLUE));
        tabPane.getTabs().add(tab4);

        Tab tabx = new Tab();
        tabx.setText("About TurkerFat32");
        TextArea textAreaAbout = new TextArea();
        textAreaAbout.setText("2017 - 2023 Turker Ozturk\n\nVisit: https://github.com/turkerozturk/TurkerFat32");
        tabx.setContent(textAreaAbout);
        textAreaAbout.setWrapText(true);
        tabPane.getTabs().add(tabx);

        //bunu, pencerede guncelleme olmazsa cok az resize ederek saglamak icin kullanilyorlarmis ama 3 tane de zarari varmis.
        //tabPane.getScene().getWindow().setWidth(tabPane.getScene().getWidth() + 0.001);

        textAreaPartitionInformation = new TextArea();
        textAreaFsNodeHelp = new TextArea();
        textAreaTodo = new TextArea();
        textAreaTodo.setWrapText(true);
        textAreaTodo.setText("TODO Dosya gezgininde klasore ikinci kez tiklayinca da refresh etmesi gerekiyor.\r\n"
                + "TODO Slackspace için de tree yapısı oluştur.\r\n"
                + "BİTTİ folder extraction metodu da yaz.\r\n"
                + "TODO slack extraction metodu da yaz.\r\n"
                + "TODO gui kodundaki satırları derli toplu hale getir.\r\n"
                + "TODO herşey bittiğinde sadece kütüphanenin kullanılması için gerekli olan methodları public bırak.\r\n");

        tab4.setContent(textAreaTodo);






        tab2 = tabPane.getTabs().get(2);
        tab3 = tabPane.getTabs().get(3);
        tab4 = tabPane.getTabs().get(4);


        logicalDiskImageUri = "C:\\tmp\\fat32images\\USBLogicalFAT321GBFSF.dig"; // bu calisiyor problemsiz. benim olusturdugum imaj.



        // <editor-fold desc="Logical Imajla Baglantili Kodlar" defaultstate="collapsed">
        if (this.logicalDiskImageUri != null) {

            partition = new Fat32Partition(logicalDiskImageUri);
            textAreaPartitionInformation.setWrapText(true);
            textAreaPartitionInformation.setText(partition.showPartitionBootsectorValues());
            textAreaFsNodeHelp.setWrapText(true);
            textAreaFsNodeHelp.setText(OtherHelper.prettyPrintNodeHelp());
            tab2.setContent(textAreaPartitionInformation);
            tab3.setContent(textAreaFsNodeHelp);

            //TREE START
            //TREE Forensic Disk Image Node en ust dugumdur mount edilen bir dosyanin
            ForensicDiskImageNode forensicDiskImageNode = new ForensicDiskImageNode();
            forensicDiskImageNode.setBytesPerSector(512);
            forensicDiskImageNode.setImageType("Raw (dd)");
            forensicDiskImageNode.setSectorCount(0);
            forensicDiskImageNode.setSourcePath(logicalDiskImageUri);
            TreeItem<Object> forensicDiskImageTreeItem;
            forensicDiskImageTreeItem = new TreeItem<>();
            forensicDiskImageTreeItem.setValue(forensicDiskImageNode);
            this.lastSelectedObject = forensicDiskImageNode;
            Node forensicDiskImageIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/forensicdiskimageicon.png")) {
            });
            forensicDiskImageTreeItem.setGraphic(forensicDiskImageIcon);

            //Create treeView
            treeView = new TreeView<>(forensicDiskImageTreeItem);
            treeView.setShowRoot(true);
            treeView.setMinHeight(usttekilerMinHeight);
            treeView.setMaxWidth(350);
            gridPane.add(this.treeView, 0, 0, 1, 1);

            //TREE LOGICAL DATA IMAGE FILE SYSTEM INFORMATION en ust dugumun bir altidir eger mantiksal dd imaj ise
            FileSystemInformationNode fileSystemInformationNode = new FileSystemInformationNode();
            fileSystemInformationNode.setClusterCount(0);
            fileSystemInformationNode.setClusterSize(0);
            fileSystemInformationNode.setFileSystemType("FAT32");
            fileSystemInformationNode.setFreeClusterCount(0);
            fileSystemInformationNode.setUtcTimeStamps(false);
            fileSystemInformationNode.setVolumeName("NONAME");
            fileSystemInformationNode.setVolumeSerialNumber("ABCD-1234");
            TreeItem<Object> fileSystemInformationTreeItem;
            fileSystemInformationTreeItem = new TreeItem<>();
            fileSystemInformationTreeItem.setValue(fileSystemInformationNode);
            this.lastSelectedObject = fileSystemInformationNode;
            Node fileSystemInformationIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/filesysteminformationicon.png")) {
            });
            fileSystemInformationTreeItem.setGraphic(fileSystemInformationIcon);
            fileSystemInformationTreeItem.setExpanded(true);
            forensicDiskImageTreeItem.getChildren().add(fileSystemInformationTreeItem);

            /*
        FsNode imageFileFsNode = new FsNode();
        imageFileFsNode.setFsNodeFileSystemType(FsNodeFileSystemType.LOGICAL_PARTITION_NODE);
        imageFileFsNode.setName(logicalDiskImageUri);
        imageFileFsNode.setAbsolutePath(logicalDiskImageUri);
        TreeItem<Object> fileSystemInformationTreeItem;
        fileSystemInformationTreeItem = new TreeItem<>();
        fileSystemInformationTreeItem.setValue(imageFileFsNode);
        this.lastSelectedObject = imageFileFsNode;
        Node fileSystemInformationIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/partitionicon.png")) {
        });
        fileSystemInformationTreeItem.setGraphic(fileSystemInformationIcon);
        fileSystemInformationTreeItem.setExpanded(true);
        forensicDiskImageTreeItem.getChildren().add(fileSystemInformationTreeItem);
             */
            TreeItem<Object> rootTreeItem;
            rootTreeItem = new TreeItem<>();
            FsNode rootFsNode = partition.getRootNode();//new FsNode();
            rootTreeItem.setValue(rootFsNode);
            this.lastSelectedObject = rootFsNode;
            //http://www.javaquery.com/2015/10/how-to-create-resource-folder-in.html
            Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/partitionicon.png")) {
            });
            rootTreeItem.setGraphic(rootIcon);
            rootTreeItem.setExpanded(true);

            this.makeBranch(rootFsNode, fileSystemInformationTreeItem);

            ArrayList<Object> contentsOfDirectory = partition.getFat32Parser().getDataArea().directoryWalk(rootFsNode, false);
            for (Object object : contentsOfDirectory) {
                FsNode fsNode = (FsNode) object;

                makeBranch(fsNode, rootTreeItem);

            }

            // FAT1 metadatasının bağlandığı node
            FileSystemMetaDataNode fat1Node = new FileSystemMetaDataNode();
            fat1Node.setName("FAT1");
            fat1Node.setStartOffset(0);
            fat1Node.setSize(partition.getFat32Parser().getDataArea().getFatArea().getOneFatTableSizeAsByte());
            TreeItem<Object> fat1TreeItem;
            fat1TreeItem = new TreeItem<>();
            fat1TreeItem.setValue(fat1Node);
            Node fat1Icon = new ImageView(new Image(getClass().getResourceAsStream("/images/fileicon.png")) {
            });
            fat1TreeItem.setGraphic(fat1Icon);
            fat1TreeItem.setExpanded(true);
            fileSystemInformationTreeItem.getChildren().add(fat1TreeItem);
            //

            // FAT2 metadatasının bağlandığı node
            FileSystemMetaDataNode fat2Node = new FileSystemMetaDataNode();
            fat2Node.setName("FAT2");
            fat2Node.setStartOffset(0);
            fat2Node.setSize(partition.getFat32Parser().getDataArea().getFatArea().getOneFatTableSizeAsByte());
            TreeItem<Object> fat2TreeItem;
            fat2TreeItem = new TreeItem<>();
            fat2TreeItem.setValue(fat1Node);
            Node fat2Icon = new ImageView(new Image(getClass().getResourceAsStream("/images/fileicon.png")) {
            });
            fat2TreeItem.setGraphic(fat2Icon);
            fat2TreeItem.setExpanded(true);
            fileSystemInformationTreeItem.getChildren().add(fat2TreeItem);
            //

            // reserved sectors metadatasının bağlandığı node
            FileSystemMetaDataNode reservedSectorsNode = new FileSystemMetaDataNode();
            reservedSectorsNode.setName("reserved sectors");
            reservedSectorsNode.setStartOffset(0);
            reservedSectorsNode.setSize(16);
            TreeItem<Object> reservedSectorsTreeItem;
            reservedSectorsTreeItem = new TreeItem<>();
            reservedSectorsTreeItem.setValue(reservedSectorsNode);
            Node reservedSectorsIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/fileicon.png")) {
            });
            reservedSectorsTreeItem.setGraphic(reservedSectorsIcon);
            reservedSectorsTreeItem.setExpanded(true);
            fileSystemInformationTreeItem.getChildren().add(reservedSectorsTreeItem);
            //

            // VBR metadatasının bağlandığı node
            FileSystemMetaDataNode vbrNode = new FileSystemMetaDataNode();
            vbrNode.setName("VBR");
            vbrNode.setStartOffset(0);
            vbrNode.setSize(1);
            TreeItem<Object> vbrTreeItem;
            vbrTreeItem = new TreeItem<>();
            vbrTreeItem.setValue(vbrNode);
            Node vbrIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/fileicon.png")) {
            });
            vbrTreeItem.setGraphic(vbrIcon);
            vbrTreeItem.setExpanded(true);
            fileSystemInformationTreeItem.getChildren().add(vbrTreeItem);
            //

            //TREE item UnallocatedSpaceContainerNode;
            UnallocatedSpaceContainerNode unallocatedSpaceContainerNode = new UnallocatedSpaceContainerNode();
            TreeItem<Object> unallocatedSpaceContainerTreeItem;
            unallocatedSpaceContainerTreeItem = new TreeItem<>();
            unallocatedSpaceContainerTreeItem.setValue(unallocatedSpaceContainerNode);
            Node unallocatedSpaceContainerIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/foldericon.png")) {
            });
            unallocatedSpaceContainerTreeItem.setGraphic(unallocatedSpaceContainerIcon);
            unallocatedSpaceContainerTreeItem.setExpanded(true);
            fileSystemInformationTreeItem.getChildren().add(unallocatedSpaceContainerTreeItem);

            ArrayList<UnallocatedSpaceNode> unallocatedSpaces = getUnallocatedSpaces();

            /*
        //basladi unallocated space ana node
        UnallocatedSpaceNode unallocatedSpaceFsNode = new UnallocatedSpaceNode();
        unallocatedSpaceFsNode.setAbsoluteStartOffset(1);
        //unallocatedSpaceFsNode.setName("[unallocated space]");
        unallocatedSpaceFsNode.setSize(100);
        TreeItem<Object> unallocatedSpaceTreeItem;
        unallocatedSpaceTreeItem = new TreeItem<>();
        unallocatedSpaceTreeItem.setValue(unallocatedSpaceFsNode);
        //TODO this.lastSelectedObject = unallocatedSpaceFsNode;

        ///FsNode unallocatedSpaceFsNode = new FsNode();
        ///unallocatedSpaceFsNode.setFsNodeFileSystemType(FsNodeFileSystemType.FAT32_UNUSED_SPACE_NODE);
        ///unallocatedSpaceFsNode.setName("[unallocated space]");
        ///unallocatedSpaceFsNode.setAbsolutePath(logicalDiskImageUri + "/" + "[unallocated space]");
        ///TreeItem<Object> unallocatedSpaceTreeItem;
        ///unallocatedSpaceTreeItem = new TreeItem<>();
        ///unallocatedSpaceTreeItem.setValue(unallocatedSpaceFsNode);
        ///this.lastSelectedObject = unallocatedSpaceFsNode;


        Node unallocatedSpaceIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/partitionicon.png")) {
        });
        unallocatedSpaceTreeItem.setGraphic(unallocatedSpaceIcon);
        unallocatedSpaceTreeItem.setExpanded(true);
        unallocatedSpaceContainerTreeItem.getChildren().add(unallocatedSpaceTreeItem);
             */
            //     this.makeBranch(unallocatedSpaceFsNode, fileSystemInformationTreeItem);
            //bitti unallocated space
            //tableView'e veri koymak böyle oluyor.
            /* bu eger ilk acilista tabloya birseyler doldumak istersen:
        final ObservableList<Object> data = FXCollections.observableArrayList(
                contentsOfDirectory
        );
             */
            // table.setItems(data);
            table.getColumns().addAll(nameTableColumn, sizeTableColumn, typeTableColumn, dateModifiedTableColumn);//, sizeAsByteTableColumn, firstDataClusterIdOfFileTableColumn, deletedTableColumn, absolutePathTableColumn);

            treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {

                //http://www.javaquery.com/2015/10/how-to-create-resource-folder-in.html
                //Node folderIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/foldericon.png")) {
                //});
                TreeItem<Object> selectedTreeItem = v.getValue();
                //selectedTreeItem.setGraphic(folderIcon);

                Object selectedObject = selectedTreeItem.getValue();

                if (selectedObject.getClass().equals(FsNode.class)) {

                    FsNode selectedFsNode = (FsNode) selectedObject;
                    this.lastSelectedObject = selectedFsNode;
                    if (!selectedFsNode.getFsNodeFileSystemType().equals(FsNodeFileSystemType.LOGICAL_PARTITION_NODE)) {

                        if (selectedFsNode.getRawEntry() != null) {
                            textArea.setText(OtherHelper.prettyPrintNode(selectedFsNode));
                        } else {
                            textArea.clear();
                        }

                        // if(! selectedTreeItem.isExpanded()) {
                        if (selectedTreeItem.isLeaf()) {
                            if (newValue != null) {
                                System.out.println(selectedFsNode.getFirstDataClusterIdOfFile());

                                if (!selectedFsNode.isDeleted() & selectedFsNode.getEntryType().equals(EntryType.DIRECTORY)) {
                                    ArrayList<Object> contentsDirectory;
                                    try {
                                        //System.out.println("ĞAAAAAAAAAAAAAAAAAA " + selectedFsNode.getName());
                                        contentsDirectory = partition.getFat32Parser().getDataArea().directoryWalk(selectedFsNode, true);

                                        for (Object object : contentsDirectory) {
                                            FsNode fsNode = (FsNode) object;

                                            if (!fsNode.getEntryType().equals(EntryType.DOTDIRECTORY) | !fsNode.getEntryType().equals(EntryType.DOTDOTDIRECTORY)) {
                                                makeBranch(fsNode, treeView.getSelectionModel().getSelectedItem());
                                            }
                                        }

                                        // https://stackoverflow.com/questions/32176782/how-can-i-clear-the-all-contents-of-the-cell-data-in-every-row-in-my-tableview-i
                                        clearTable();

                                        final ObservableList<Object> data2 = FXCollections.observableArrayList(
                                                contentsDirectory
                                        );
                                        table.setItems(data2);

                                    } catch (IOException ex) {
                                        Logger.getLogger(Fat32Gui.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        } else {

                            clearTable();

                            copyTreeItemsToTable(selectedTreeItem.getChildren());

                            /*
                        ArrayList<Object> contentsDirectory = new ArrayList<>();

                        for (TreeItem<Object> treeItem : selectedTreeItem.getChildren()) {
                            Object object = treeItem.getValue();
                            contentsDirectory.add(object);
                        }

                        final ObservableList<Object> data2 = FXCollections.observableArrayList(
                            contentsDirectory
                        );

                        table.setItems(data2);
                             */
                        }
                    }

                } else if (selectedObject.getClass().equals(UnallocatedSpaceContainerNode.class)) {

                    clearTable();
                    final ObservableList<Object> data2 = FXCollections.observableArrayList(
                            unallocatedSpaces
                    );
                    table.setItems(data2);
                    textArea.clear();
                    textArea.setText("Unallocated Space Container");

                } else if (selectedObject.getClass().equals(UnallocatedSpaceNode.class)) {
                    this.lastSelectedObject = (UnallocatedSpaceNode) selectedObject;

                    textArea.clear();
                    textArea.setText("Unallocated Space");
                } else if (selectedObject.getClass().equals(FileSystemInformationNode.class)) {
                    this.lastSelectedObject = (FileSystemInformationNode) selectedObject;

                    clearTable();
                    copyTreeItemsToTable(selectedTreeItem.getChildren());
                    textArea.clear();
                    textArea.setText("File System Information");
                }
            });

            //   makeBranch(rootFsNode, slackRootTreeItem);
            //StackPane layout = new StackPane();
            /*
        TableColumn firstNameCol = new TableColumn("First Name");
        TableColumn lastNameCol = new TableColumn("Last Name");
        TableColumn emailCol = new TableColumn("Email");
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
             */
            //layout.getChildren().add(treeView);
            table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

                Object selectedObject = obs.getValue();
                if (selectedObject != null) {
                    if (selectedObject.getClass().equals(FsNode.class)) {

                        FsNode selectedFsNode = (FsNode) selectedObject;
                        if (!selectedFsNode.getFsNodeFileSystemType().equals(FsNodeFileSystemType.LOGICAL_PARTITION_NODE)) {

                            this.lastSelectedObject = selectedFsNode;
                            if (newSelection != null) {
                                textArea.clear();
                                textArea.setText(OtherHelper.prettyPrintNode(selectedFsNode));
                            }

                        }
                    } else if (selectedObject.getClass().equals(UnallocatedSpaceContainerNode.class)) {
                        System.out.println("Unallocated Space size: ");
                    }

                }
            });

            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // https://stackoverflow.com/questions/32176782/how-can-i-clear-the-all-contents-of-the-cell-data-in-every-row-in-my-tableview-i
                    clearTable();
                }
            });

            buttonExtractSelected.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //  webEngine.load("http://www.google.com.tr");
                    Object selectedObject = lastSelectedObject;
                    //System.out.println("" + selectedObject.getClass());
                    Class selectedClass = selectedObject.getClass();
                    if (selectedClass.equals(FsNode.class) | selectedClass.equals(UnallocatedSpaceNode.class) | selectedClass.equals(FileSystemMetaDataNode.class)) {
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        File selectedDirectory = directoryChooser.showDialog(primaryStage);
                        if (selectedDirectory == null) {
                            alert("No Directory selected", "No Output Directory selected");

                        } else {
                            if (selectedObject.getClass().equals(FsNode.class)) {
                                FsNode lastSelectedFsNode = (FsNode) lastSelectedObject;

                                if (lastSelectedFsNode.getEntryType().equals(EntryType.FILE)) {
                                    try {
                                        partition.extractFile(lastSelectedFsNode, selectedDirectory.getAbsolutePath());
                                        String messageText = "Selected file extracted(fake)" + lastSelectedFsNode.getFirstDataClusterIdOfFile() + ", " + selectedDirectory.getAbsolutePath();
                                        System.out.println(messageText);
                                        Alert alert = new Alert(AlertType.INFORMATION);
                                        alert.setTitle("Export File");
                                        alert.setHeaderText(messageText);
                                        alert.setContentText(messageText);
                                        alert.show();
                                    } catch (IOException ex) {
                                        Logger.getLogger(Fat32Gui.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {

                                    try {
                                        partition.extractFolder(lastSelectedFsNode, selectedDirectory.getAbsolutePath());
                                        //SaveFile(Santa_Claus_Is_Coming_To_Town, file);
                                        String messageText = "Directory Extract Completed" + lastSelectedFsNode.getFirstDataClusterIdOfFile() + ", " + selectedDirectory.getAbsolutePath();
                                        alert("Export File", messageText);
                                    } catch (IOException ex) {
                                        Logger.getLogger(Fat32Gui.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                }
                            } else if (selectedObject.getClass().equals(UnallocatedSpaceNode.class)) {
                                System.out.println("hebele");
                                try {
                                    partition.extractUnallocatedSpace((UnallocatedSpaceNode) lastSelectedObject, selectedDirectory.getAbsolutePath());
                                } catch (IOException ex) {
                                    Logger.getLogger(Fat32Gui.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else if (selectedObject.getClass().equals(FileSystemMetaDataNode.class)) {

                            }

                        }

                    }

                }
            });

            /* UNCOMMENT THIS IF YOU WANT TO SEE THE UNALLOCATED SPACE NODES IN THE TREE
        for (UnallocatedSpaceNode unallocatedSpaceFsNode : unallocatedSpaces) {
            TreeItem<Object> unallocatedSpaceTreeItem;
            unallocatedSpaceTreeItem = new TreeItem<>();
            unallocatedSpaceTreeItem.setValue(unallocatedSpaceFsNode);
            Node unallocatedSpaceIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/partitionicon.png")) {
            });
            unallocatedSpaceTreeItem.setGraphic(unallocatedSpaceIcon);
            unallocatedSpaceTreeItem.setExpanded(true);
            unallocatedSpaceContainerTreeItem.getChildren().add(unallocatedSpaceTreeItem);
        }
             */
        } // bitti logical imaj ile baglantili kodlar.

        // </editor-fold>



    }


    public void alert(String titleText, String messageText) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titleText);
        alert.setHeaderText(messageText);
        alert.setContentText(messageText);
        alert.show();
    }

    public ArrayList<UnallocatedSpaceNode> getUnallocatedSpaces() {
        ArrayList<UnallocatedSpaceNode> unallocatedSpaces = new ArrayList<>();

        Long[] fatEntries = partition.getFat32Parser().getDataArea().getFatTable().getEntries();
        int anchor = 0;
        LinkedList<Integer> freeAreaOffsets = new LinkedList<>();
        anchor = 0;
        boolean hasAnchor;
        hasAnchor = false;
        int kapcur = 0;
        int clusterAdedi;
        clusterAdedi = 0;
        final int clusterBoyutuAsKiloByte = 4;
        for (int currentEntry = 0; currentEntry < fatEntries.length; currentEntry++) {
            if (fatEntries[currentEntry] == 0) {
                clusterAdedi = clusterAdedi + 1;
                if (hasAnchor) {
                    anchor = anchor + 1;
                } else {
                    hasAnchor = true;
                    kapcur = currentEntry;
                    //System.out.println("kapcur = " + kapcur + ", liste[" + currentEntry + "] = " + fatEntries[currentEntry] + ", anchor = " + anchor + ", hasAnchor = " + hasAnchor + ", clusterAdedi = " + clusterAdedi);
                }
            } else {
                if (clusterAdedi > 0) {
                    UnallocatedSpaceNode unallocatedSpaceNode = new UnallocatedSpaceNode();
                    unallocatedSpaceNode.setAbsoluteStartOffset(kapcur);
                    unallocatedSpaceNode.setSize(clusterAdedi * clusterBoyutuAsKiloByte);
                    unallocatedSpaces.add(unallocatedSpaceNode);
                    // System.out.println("kapcur = " + kapcur + ", clusterAdedi x 4 = " + clusterAdedi * clusterBoyutuAsKiloByte);
                    if (clusterAdedi * clusterBoyutuAsKiloByte > 102400) {
                        //TODO System.out.println("AAAAAAAAAAAAAAAAAAAAa");
                    }
                }
                anchor = 0;
                hasAnchor = false;
                clusterAdedi = 0;
            }
        }

        if (clusterAdedi > 0) {
            // int toplamClusterBoyutuAsKiloByte = clusterAdedi * clusterBoyutuAsKiloByte;
            //System.out.println("kapcur = " + kapcur + ", clusterAdedi x 4 = " + clusterAdedi);
            if (clusterAdedi > 25600) {
                for (int i = 0; i < clusterAdedi; i++) {
                    if (i % 25600 == 0) {
                        int partOffset = kapcur + i;
                        UnallocatedSpaceNode unallocatedSpaceNode = new UnallocatedSpaceNode();
                        unallocatedSpaceNode.setAbsoluteStartOffset(partOffset);
                        unallocatedSpaceNode.setSize(25600 * 4);
                        unallocatedSpaces.add(unallocatedSpaceNode);
                        // System.out.println("offset: " + partOffset + " clusterAdedi: " +( 25600 * 4) );                     
                    }
                }
            }
            // System.out.println("offset: "  + " clusterAdedi: " +( (clusterAdedi % 25600) * 4) );     
            //                               unallocatedSpaces.add(unallocatedSpaceNode);
        }
        return unallocatedSpaces;
    }

    public TreeItem<Object> makeBranch(Object object, TreeItem<Object> parent) {
        FsNode fsNode = (FsNode) object;
        String imagePath = "";

        switch (fsNode.getEntryType()) {
            case DIRECTORY:
            case DOTDIRECTORY:
            case DOTDOTDIRECTORY:
                if (!fsNode.isDeleted()) {
                    imagePath = "/images/foldericon.png";
                } else {
                    imagePath = "/images/folderdeletedicon.png";
                }
                break;
            case FILE:
                if (!fsNode.isDeleted()) {
                    imagePath = "/images/fileicon.png";
                } else {
                    imagePath = "/images/filedeletedicon.png";
                }
                break;
            case VOLUME:
                imagePath = "/images/fileicon.png";
                break;

        }

        Node folderIcon = new ImageView(new Image(getClass().getResourceAsStream(imagePath)) {
        });
        TreeItem<Object> node = new TreeItem<>();

        node.setValue(fsNode);
        node.setGraphic(folderIcon);

        //node.setGraphic(rootIcon);
        parent.getChildren().add(node);
        return node;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void clearTable() {
        for (int i = 0; i < table.getItems().size(); i++) {
            table.getItems().clear();
        }
    }

    private void copyTreeItemsToTable(ObservableList<TreeItem<Object>> children) {
        ArrayList<Object> contentsDirectory = new ArrayList<>();

        for (TreeItem<Object> treeItem : children) {
            Object object = treeItem.getValue();
            contentsDirectory.add(object);
        }

        final ObservableList<Object> data2 = FXCollections.observableArrayList(
                contentsDirectory
        );

        table.setItems(data2);
    }

    public String getLogicalDiskImageUri() {
        return logicalDiskImageUri;
    }

    public void setLogicalDiskImageUri(String logicalDiskImageUri) {
        this.logicalDiskImageUri = logicalDiskImageUri;
    }

}

//   }
//System.out.println(": " + fatEntries[currentEntryAsInt]);
/*
            boolean inceledigimizDoluMu = fatEntries[currentEntry] != 0;
            boolean oncekiDeDoluMuydu = previousEntry != 0;

            if (inceledigimizDoluMu) {                      
                if (oncekiDeDoluMuydu) {
                    //devam ediyor sıfır olmayan değerler dizisi
                } else {
                    //bitti sıfır dizisi.
                    //sıfır dizisinin başlangıç adresini yazdır.
                   System.out.println("başlangıç: " + anchorEntry + " " + currentEntry);// + " NULT " + dosyaClusterSayisi );
                }                                          
            } else { // 0 ise;                
                if (oncekiDeDoluMuydu) {
                    //başladı sıfır dizisi
                  //  isFirstZero = true;
                    anchor = 0;   
                    anchorEntry = currentEntry;
                  //  dosyaClusterSayisi = 4L;
                } else {
                    //devam ediyor sıfır dizisi.
                    
                }      
                
            }
            previousEntry = currentEntry;
 */
//isFirstZero = false;
//               anchor++;
/*
                Long currentEntry = Integer.valueOf(currentEntryAsInt).longValue();

                    if(isFirstZero) {

                        if (currentEntry == anchorEntry + anchor) {
                        } else {
                            isFirstZero = false;
                            anchor = 0;   
                           // dosyaClusterSayisi = 0L;
                           // anchorEntry = currentEntry;
                           //freeAreaOffsets.add(anchorEntry);
                            System.out.println(currentEntryAsInt + " NULL " + dosyaClusterSayisi );
                        }
                    } else {
                    
                        isFirstZero = true;
                        anchor = 0;   
                        anchorEntry = currentEntry;
                        dosyaClusterSayisi = 4L;
                        System.out.println(currentEntryAsInt + " NULT " + dosyaClusterSayisi );

                    }           
                   
                dosyaClusterSayisi += 4L;                                             

 */
