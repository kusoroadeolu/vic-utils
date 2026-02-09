
void main() {
    var point = new Point(1, 2);
    var point2 = new Point(2, 3);
    var p3 = point.combine(point2);
    IO.println(p3);
}


record Point(int x, int y){
    Point combine(Point p){
        if (y == p.x){
            return new Point(x, p.y);
        }
        else return this;
    }
}




