import ecs.Components.Movable;
import ecs.ControlRegistry;
import ecs.ControlRegistry.ControlInfo;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class ControlsView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.Controls;
    private Font font;
    private ControlRegistry controlRegistry;
    private int selectedIndex;
    private boolean awaitingInput;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics.getWindow());
        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> nextGameState = GameStateEnum.MainMenu);
    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.Controls;
        selectedIndex = 0;
        controlRegistry = ControlRegistry.getInstance();
        inputKeyboard.registerCommand(GLFW_KEY_DOWN, true, (double elapsedTime) -> {
            if (awaitingInput) return;
            selectedIndex++;
            if (selectedIndex >= controlRegistry.getControlsCount()) {
                selectedIndex = 0;
            }
        });
        inputKeyboard.registerCommand(GLFW_KEY_UP, true, (double elapsedTime) -> {
            if (awaitingInput) return;
            selectedIndex--;
            if (selectedIndex < 0) {
                selectedIndex = controlRegistry.getControlsCount() - 1;
            }
        });
        inputKeyboard.registerCommand(GLFW_KEY_ENTER, true, (double elapsedTime) -> {
            awaitingInput = true;
            glfwSetKeyCallback(graphics.getWindow(), (window, key, scancode, action, mods) -> {
                if (action == GLFW_PRESS) {
                    awaitingInput = false;
                    Movable.Direction replaceDirection = controlRegistry.getControls().get(selectedIndex).getOutcome();
                    controlRegistry.updateKey(replaceDirection, key);
                    glfwSetKeyCallback(graphics.getWindow(), (window1, key1, scancode1, action1, mods1) -> {});
                }
            });
        });
        awaitingInput = false;
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        // Updating the keyboard can change the nextGameState
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
    }

    @Override
    public void render(double elapsedTime) {
        List<ControlInfo> controls = controlRegistry.getControls();
        String message = "Change Controls";
        float height = 0.075f;
        float top = -1f / 2;
        float width = font.measureTextWidth(message, height);
        Color color = Color.YELLOW;

        graphics.drawTextByHeight(font, message, 0.0f - width / 2, top, height, color);

        height = 0.05f;
        top += height;
        for (int i = 0; i < controls.size(); i++) {
            ControlInfo control = controls.get(i);
            boolean isSelected = i == selectedIndex;
            message = isSelected && awaitingInput ?
                    "Select " + control.getOutcomeDisplay() + " key...":
                    control.getOutcomeDisplay() + ": " + control.getKeyDisplay();
            color = isSelected ? Color.BLUE : Color.GREEN;
            top += height;
            width = font.measureTextWidth(message, height);
            graphics.drawTextByHeight(font, message, 0f - width / 2, top, height, color);
        }
    }
}
