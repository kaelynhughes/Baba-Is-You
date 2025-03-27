import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class LevelSelectView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.LevelSelect;
    private Font font;
    private int currentSelection;
    private Font fontMenu;
    private Font fontSelected;

    private List<List<String>> level_info = new ArrayList<>();
    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        currentSelection = -1;

        inputKeyboard = new KeyboardInput(graphics.getWindow());
        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });
        inputKeyboard.registerCommand(GLFW_KEY_ENTER, true, (double elapsedTime) -> {
            if(currentSelection!= -1) nextGameState = GameStateEnum.GamePlay;
        });

        inputKeyboard.registerCommand(GLFW_KEY_UP, true, (double elapsedTime) -> {

            if(currentSelection > 0){
                currentSelection -= 1;
            }

        });
        inputKeyboard.registerCommand(GLFW_KEY_DOWN, true, (double elapsedTime) -> {
            if(currentSelection < level_info.size()-1){
                currentSelection += 1;
            }
        });

        String directoryPath = "resources/levels";


        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) { // Ensure it's a file, not a directory
                    List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                    ArrayList<String> data = new ArrayList<>();
                    data.add(lines.get(0));
                    data.add(lines.get(1));
                    level_info.add(data);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!level_info.isEmpty()){

            currentSelection = 0;
        }

        System.out.println(level_info);

    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.LevelSelect;
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
        final String message = "Level Select";
        final String error_message = "Uh oh! No levels detected!";
        final float height = 0.075f;
        final float width = font.measureTextWidth(message, height);
        final float error_width = font.measureTextWidth(error_message, height);

        if(currentSelection == -1){
            graphics.drawTextByHeight(font, error_message, 0 - error_width / 2, 0 - height / 2, height, Color.YELLOW);
        } else{

            graphics.drawTextByHeight(font, message, 0 - width / 2, 0 - height / 2, height, Color.YELLOW);

            int iteration = 0;
            float top = height + (height*.5f);
            for(List<String> level: level_info){
                top = renderLevelOption( level.get(0),top, height, (currentSelection == iteration ? Color.YELLOW : Color.BLUE));
                iteration ++;
            }
        }
    }
    public float renderLevelOption(String name, float top, float height,Color color){

        float width = font.measureTextWidth(name, height);
        graphics.drawTextByHeight(font, name, 0.0f - width / 2, top, height, color);

        return top + height;


    }

}
