package ecs;

import edu.usu.graphics.Color;
import java.util.HashMap;
import java.util.Map;

public class GameObjectRegistry {
    public static class GameObjectInfo {
        String imagePath;
        Color color;

        public GameObjectInfo(String imagePath, Color color) {
            this.imagePath = imagePath;
            this.color = color;
        }

        public String getImagePath() {
            return imagePath;
        }

        public Color getColor() {
            return color;
        }
    }

    private static final Map<String, GameObjectInfo> objectRegistry = new HashMap<>();

    static {
        // Objects
        objectRegistry.put("BABA", new GameObjectInfo("resources/images/square-outline.png", Color.BABA_COLOR));
        objectRegistry.put("FLAG", new GameObjectInfo("resources/images/flag.png", Color.FLAG_COLOR));
        objectRegistry.put("FLOOR", new GameObjectInfo("resources/images/floor.png", Color.FLOOR_COLOR));
        objectRegistry.put("FLOWERS", new GameObjectInfo("resources/images/flowers.png", Color.FLOWERS_COLOR));
        objectRegistry.put("GRASS", new GameObjectInfo("resources/images/grass.png", Color.GRASS_COLOR));
        objectRegistry.put("HEDGE", new GameObjectInfo("resources/images/hedge.png", Color.HEDGE_COLOR));
        objectRegistry.put("LAVA", new GameObjectInfo("resources/images/lava.png", Color.LAVA_COLOR));
        objectRegistry.put("ROCK", new GameObjectInfo("resources/images/rock.png", Color.ROCK_COLOR));
        objectRegistry.put("WALL", new GameObjectInfo("resources/images/wall.png", Color.WALL_COLOR));
        objectRegistry.put("WATER", new GameObjectInfo("resources/images/water.png", Color.WATER_COLOR));

        // Word objects
        objectRegistry.put("WORD_BABA", new GameObjectInfo("resources/images/word-baba.png", Color.WORD_BABA_COLOR));
        objectRegistry.put("WORD_FLAG", new GameObjectInfo("resources/images/word-flag.png", Color.WORD_FLAG_COLOR));
        objectRegistry.put("WORD_IS", new GameObjectInfo("resources/images/word-is.png", Color.WORD_IS_COLOR));
        objectRegistry.put("WORD_DEFEAT", new GameObjectInfo("resources/images/word-kill.png", Color.WORD_KILL_COLOR));
        objectRegistry.put("WORD_LAVA", new GameObjectInfo("resources/images/word-lava.png", Color.WORD_LAVA_COLOR));
        objectRegistry.put("WORD_PUSH", new GameObjectInfo("resources/images/word-push.png", Color.WORD_PUSH_COLOR));
        objectRegistry.put("WORD_ROCK", new GameObjectInfo("resources/images/word-rock.png", Color.WORD_ROCK_COLOR));
        objectRegistry.put("WORD_SINK", new GameObjectInfo("resources/images/word-sink.png", Color.WORD_SINK_COLOR));
        objectRegistry.put("WORD_STOP", new GameObjectInfo("resources/images/word-stop.png", Color.WORD_STOP_COLOR));
        objectRegistry.put("WORD_WALL", new GameObjectInfo("resources/images/word-wall.png", Color.WORD_WALL_COLOR));
        objectRegistry.put("WORD_WATER", new GameObjectInfo("resources/images/word-water.png", Color.WORD_WATER_COLOR));
        objectRegistry.put("WORD_WIN", new GameObjectInfo("resources/images/word-win.png", Color.WORD_WIN_COLOR));
        objectRegistry.put("WORD_YOU", new GameObjectInfo("resources/images/word-you.png", Color.WORD_YOU_COLOR));
    }

    public static GameObjectInfo getObjectInfo(String name) {
        return objectRegistry.get(name.toUpperCase());
    }
}
