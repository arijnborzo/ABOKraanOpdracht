public class Main {
    public static void main(String[] args)
    {
        Movement move = new Movement(1,new Coordinate(1,1), new Coordinate(5,2),10.0,3.0);
        System.out.println(move.getTravelTime());
    }

}
