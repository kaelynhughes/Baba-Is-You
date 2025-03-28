import edu.usu.graphics.Graphics2D;

import java.util.List;

public interface IGameState {
    void initialize(Graphics2D graphics);

    void initializeSession();

    GameStateEnum processInput(double elapsedTime);

    void update(double elapsedTime);

    void render(double elapsedTime);

    default void setLevelData(List<String> level) {}
}
