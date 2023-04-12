package com.example.geomagictest;

import com.example.geomagictest.data.Line;
import com.example.geomagictest.data.Point;
import com.example.geomagictest.data.PolyLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class GeoMagicTestModel {

    private File inputFile;

    private ArrayList<Line> lines;
    private ArrayList<PolyLine> polyLines;
    public GeoMagicTestModel(){
        this.inputFile = null;
        this.lines = new ArrayList<Line>();
        this.polyLines = new ArrayList<PolyLine>();
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public void extractPolyLinesFromFile() throws FileNotFoundException {

        if(this.inputFile != null){
            this.lines = new ArrayList<Line>();
            this.polyLines = new ArrayList<PolyLine>();

            Scanner scanner = new Scanner(this.inputFile);

            while(scanner.hasNextLine()){
                String rawLine = scanner.nextLine();
                this.lines.add(createLineFromString(rawLine));
            }
        }

        this.polyLines = createPolyLinesFromLines();
    }

    private Line createLineFromString(String rawLine){
        String[] rawCoordinates = rawLine.split(" ");

        if(rawCoordinates.length != 4){
            return null;
        }

        return new Line(new Point(Float.valueOf(rawCoordinates[0]), Float.valueOf(rawCoordinates[1])), new Point(Float.valueOf(rawCoordinates[2]), Float.valueOf(rawCoordinates[3])));
    }

    private ArrayList<PolyLine> createPolyLinesFromLines(){
        final ArrayList<PolyLine> polyLines = new ArrayList<PolyLine>();
        final ArrayList<Line> lines = new ArrayList<>(this.lines);

        while(!lines.isEmpty()){
            Line startLine = lines.get(0);

            PolyLine polyLine = createPolyLine(startLine, lines);

            if(polyLine != null){
                polyLine.setName("Polyline" + (polyLines.size() + 1));

                polyLines.add(polyLine);
                lines.removeAll(polyLine.getLines());
            }
            else{
                lines.remove(startLine);
            }
        }

        polyLines.sort(new PolyLineComparator());
        return polyLines;
    }

    private PolyLine createPolyLine(Line startLine, ArrayList<Line> lines){
        ArrayList<Line> resultLines = new ArrayList<Line>();

        resultLines.addAll(createPolyLineByPoint(startLine.getStart(), startLine, lines));
        resultLines.add(startLine);
        resultLines.addAll(createPolyLineByPoint(startLine.getEnd(), startLine, lines));

        if(resultLines.size() > 1){
            return new PolyLine(resultLines);
        }

        return null;
    }

    private ArrayList<Line> createPolyLineByPoint(Point point, Line startLine, ArrayList<Line> lines){
        ArrayList<Line> result = new ArrayList<Line>();
        ArrayList<Line> linePool = new ArrayList<>();

        for(Line line : this.lines){
            if(line.containsPoint(point)){
                linePool.add(line);
            }
        }

        if(linePool.size() == 2){

            Line newLine = null;
            Point newPoint = null;

            if(startLine.equals(linePool.get(0))){
                newLine = linePool.get(1);
                newPoint = newLine.getOpponentPoint(point);
            } else if (startLine.equals(linePool.get(1))) {
                newLine = linePool.get(0);
                newPoint = newLine.getOpponentPoint(point);
            }
            else{
                return new ArrayList<Line>();
            }

            lines.removeAll(linePool);

            result.add(newLine);
            result.addAll(createPolyLineByPoint(newPoint, newLine, lines));

            return result;
        }
        else {
            return new ArrayList<Line>();
        }
    }

    public ArrayList<Line> getLines(){
        return lines;
    }

    public ArrayList<PolyLine> getPolyLines(){
        return polyLines;
    }

    private class PolyLineComparator implements Comparator<PolyLine>{

        @Override
        public int compare(PolyLine o1, PolyLine o2){
            if(o1.getLength() < o2.getLength()){
                return 1;
            } else if (o1.getLength() > o2.getLength()){
                return -1;
            }
            else{
                return 0;
            }
        }
    }
}
