import edu.usu.graphics.*;

import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    private final Graphics2D graphics;
    private HashMap<GameStateEnum, IGameState> states;
    private IGameState currentState;
    GameStateEnum nextStateEnum = GameStateEnum.MainMenu;
    GameStateEnum prevStateEnum = GameStateEnum.MainMenu;

    public Game(Graphics2D graphics) {
        this.graphics = graphics;
    }

    public void initialize() {
        states = new HashMap<>() {
            {
                put(GameStateEnum.MainMenu, new MainMenuView());
                put(GameStateEnum.GamePlay, new GamePlayView());
                put(GameStateEnum.LevelSelect, new LevelSelectView());
                put(GameStateEnum.Controls, new ControlsView());
                put(GameStateEnum.Credits, new CreditsView());
            }
        };

        // Give all game states a chance to initialize, other than the constructor
        for (var state : states.values()) {
            state.initialize(graphics);
        }

        currentState = states.get(GameStateEnum.MainMenu);
        currentState.initializeSession();
    }

    public void shutdown() {
    }

    public void run() {
        // Grab the first time
        double previousTime = glfwGetTime();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!graphics.shouldClose()) {
            double currentTime = glfwGetTime();
            double elapsedTime = currentTime - previousTime;    // elapsed time is in seconds
            previousTime = currentTime;

            processInput(elapsedTime);
            update(elapsedTime);
            render(elapsedTime);
        }
    }

    private void processInput(double elapsedTime) {
        // Poll for window events: required in order for window, keyboard, etc events are captured.
        glfwPollEvents();

        nextStateEnum = currentState.processInput(elapsedTime);
    }

    private void update(double elapsedTime) {
        // Special case for exiting the game
        if (nextStateEnum == GameStateEnum.Quit) {
            glfwSetWindowShouldClose(graphics.getWindow(), true);
        } else {
            if (nextStateEnum == prevStateEnum) {
                currentState.update(elapsedTime);
            } else {
                IGameState nextState = states.get(nextStateEnum);

                // If transitioning from LevelSelectView to GamePlayView, pass level data
                if (currentState instanceof LevelSelectView && nextState instanceof GamePlayView) {
                    List<String> selectedLevel = ((LevelSelectView) currentState).getSelectedLevel();
                    System.out.println("HELLO "+selectedLevel);
                    ((GamePlayView) nextState).setLevelData(selectedLevel);
                }

                currentState = nextState;
                currentState.initializeSession();
                prevStateEnum = nextStateEnum;
            }
        }
    }

    private void render(double elapsedTime) {
        graphics.begin();

        currentState.render(elapsedTime);

        graphics.end();
    }
}
