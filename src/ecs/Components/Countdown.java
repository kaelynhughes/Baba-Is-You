package ecs.Components;

public class Countdown extends Component {
    public double timeRemaining;

    public Countdown(double howLong) {
        timeRemaining = howLong;
    }

    @Override
    public Component copy() {
        return new Countdown(timeRemaining);
    }
}
