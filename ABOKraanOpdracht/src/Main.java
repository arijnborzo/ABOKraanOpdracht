public class Main {
    public static void main(String[] args)
    {
        Movement move = new Movement(1,new Coordinate(1,1), new Coordinate(5,5),10.0,3.0);
        System.out.println(move.getTravelTime());
        Movement move2 = new Movement(1,new Coordinate(6,6), new Coordinate(7,7),10.0,3.0);


        System.out.println(move.collision(move,move2));
    }

}
