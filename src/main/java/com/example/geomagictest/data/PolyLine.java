package com.example.geomagictest.data;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class PolyLine {

    private ArrayList<Line> lines;
    private Color color;

    private String name;

    public PolyLine(ArrayList<Line> lines){
        this.lines = lines;

        Random random = new Random();
        this.color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    public float getLength(){
        float length = 0;

        for(Line line : lines){
            length += line.getLength();
        }

        return length;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
