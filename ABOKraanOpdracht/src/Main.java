import org.json.*;
public class Main {
    public static void main(String[] args)
    {
        Movement move = new Movement(1,1,new Coordinate(1,1), new Coordinate(5,5),10.0,4.0);
        System.out.println(move.getTravelTime());
        Movement move2 = new Movement(2,1,new Coordinate(6,6), new Coordinate(3,4),10.0,3.0);


        System.out.println(move.collision(move,move2));
        for(int i = 0; i < move.getOverlapArea(move,move2).length; i++) {
            System.out.print(move.getOverlapArea(move,move2)[i]);
        }

    }
}
