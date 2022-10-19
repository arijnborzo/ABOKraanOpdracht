import java.util.ArrayList;
import java.util.List;

public class Trajectory
{
    List<Movement> trajectory ;

    public Trajectory()
    {
        trajectory = new ArrayList<>();
    }

    public void addMovement(Movement m)
    {
        trajectory.add(m);
    }
}
