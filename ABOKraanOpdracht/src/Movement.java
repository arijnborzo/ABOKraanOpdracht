public class Movement {

    private double startTime;
    private double endTime;
    private Coordinate startLocation;
    private Coordinate endLocation;

    private double xSpeed;
    private double ySpeed;

    public Movement(int t, Coordinate p1 , Coordinate p2 , double vx , double vy)
    {
        startTime = t;
        startLocation = p1;
        endLocation = p2;

        xSpeed = vx;
        ySpeed = vy;

        endTime = t + getTravelTime();

    }

    public double getTravelTime()
    {
        double xTijd = Math.abs(endLocation.getX() - startLocation.getX()) / xSpeed ;
        double yTijd = Math.abs(endLocation.getY() - startLocation.getY()) / ySpeed ;

        if(xTijd > yTijd) return xTijd;
        else return yTijd ;
    }
}
