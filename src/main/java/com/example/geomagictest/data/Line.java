package com.example.geomagictest.data;


public class Line {

    private Point start;
    private Point end;

    public Line(Point start, Point end){
        this.start = start;
        this.end = end;
    }

    public boolean containsPoint(Point point){
        boolean result = false;

        result = result || point.equals(start);
        result = result || point.equals(end);

        return result;
    }

    public Point getOpponentPoint(Point point){
        if(start.equals(point)){
            return end;
        } else if (end.equals(point)) {
            return start;
        }
        else{
            return null;
        }
    }

    public float getLength(){
        float deltaX = start.getX() - end.getX();
        float deltay = start.getY() - end.getY();

        float length = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltay, 2));

        return length;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }


}
