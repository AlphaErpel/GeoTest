package com.example.geomagictest;

import com.example.geomagictest.data.Line;
import com.example.geomagictest.data.PolyLine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GeoMagicTestController {

    private GeoMagicTestModel model;
    private Stage stage;
    private TableView polyLineDetailsTable;
    @FXML
    private VBox sidbarVBox;
    @FXML
    private Canvas polyLineCanvas;

    public GeoMagicTestController(){
        model = new GeoMagicTestModel();
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    protected void loadDataButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(this.stage);

        if(selectedFile != null){
            model.setInputFile(selectedFile);

            try {
                model.extractPolyLinesFromFile();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        refreshUI();
    }

    private void refreshUI(){

        drawPolyLinesOnCanvas();
        fillPolyLinesDetailsTable();
    }

    private void drawPolyLinesOnCanvas(){

        GraphicsContext graphicsContext = polyLineCanvas.getGraphicsContext2D();
        ArrayList<PolyLine> polyLines = model.getPolyLines();

        for(PolyLine polyLine : polyLines){
            drawPolyLine(polyLine, graphicsContext);
        }
    }

    private void drawPolyLine(PolyLine polyLine, GraphicsContext graphicsContext){
        graphicsContext.setStroke(polyLine.getColor());

        for(Line line : polyLine.getLines()){
            graphicsContext.strokeLine(line.getStart().getX(), line.getStart().getY(), line.getEnd().getX(), line.getEnd().getY());
        }
    }

    private void fillPolyLinesDetailsTable(){
        initPolyLinesDetailsTable();

        ObservableList<Map<String, Object>> polyLineItems = FXCollections.<Map<String, Object>>observableArrayList();

        for(PolyLine polyLine : model.getPolyLines()){
            Map<String, Object> polyLineItem = new HashMap<>();

            polyLineItem.put("name", polyLine.getName());
            polyLineItem.put("length" , polyLine.getLength());
            polyLineItem.put("color" , createColorPanel(polyLine.getColor()));

            polyLineItems.add(polyLineItem);
        }

        polyLineDetailsTable.getItems().addAll(polyLineItems);
    }

    private void initPolyLinesDetailsTable(){
        sidbarVBox.getChildren().remove(polyLineDetailsTable);

        polyLineDetailsTable = new TableView();

        TableColumn polyLineColumn = new TableColumn("PolyLine");
        TableColumn lengthColumn = new TableColumn("Length");
        TableColumn colorColumn = new TableColumn("Color");

        polyLineColumn.setCellValueFactory(new MapValueFactory<>("name"));
        lengthColumn.setCellValueFactory(new MapValueFactory<>("length"));
        colorColumn.setCellValueFactory(new MapValueFactory<>("color"));

        polyLineColumn.setMinWidth(100);
        lengthColumn.setMinWidth(100);
        colorColumn.setMinWidth(100);

        polyLineDetailsTable.getColumns().addAll(polyLineColumn, lengthColumn, colorColumn);

        sidbarVBox.getChildren().add(polyLineDetailsTable);
    }

    private Pane createColorPanel(Color color){
        Pane colorPanel = new Pane();
        colorPanel.setBackground(new Background(new BackgroundFill(color, null, null)));

        return colorPanel;
    }
}