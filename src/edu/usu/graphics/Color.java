/*
Copyright (c) 2024 James Dean Mathias

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package edu.usu.graphics;

public class Color {
    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1;
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(1, 1, 1);
    public static final Color RED = new Color(1, 0, 0);
    public static final Color GREEN = new Color(0, 1.0f, 0);
    public static final Color BLUE = new Color(0, 0, 1);
    public static final Color PURPLE = new Color(0.5f, 0, 0.5f);
    public static final Color YELLOW = new Color(1, 1, 0);

    public static final Color CORNFLOWER_BLUE = new Color(100 / 255f, 149 / 255f, 237 / 255f);



    //LOGAN'S MODIFICATIONS
    // Additional Game Object Colors
    public static final Color PINK = new Color(1.0f, 0.75f, 0.8f);
    public static final Color ORANGE = new Color(1.0f, 0.5f, 0.0f);
    public static final Color LIGHT_GRAY = new Color(0.8f, 0.8f, 0.8f);
    public static final Color DARK_GRAY = new Color(0.3f, 0.3f, 0.3f);
    public static final Color BROWN = new Color(139 / 255f, 69 / 255f, 19 / 255f);
    public static final Color MAGENTA = new Color(1.0f, 0.0f, 1.0f);
    public static final Color CYAN = new Color(0.0f, 1.0f, 1.0f);
    public static final Color FOREST_GREEN = new Color(34 / 255f, 139 / 255f, 34 / 255f);

    // Object-Specific Colors
    public static final Color BABA_COLOR = PINK;
    public static final Color FLAG_COLOR = YELLOW;
    public static final Color FLOOR_COLOR = BROWN;
    public static final Color FLOWERS_COLOR = MAGENTA;
    public static final Color GRASS_COLOR = GREEN;
    public static final Color HEDGE_COLOR = FOREST_GREEN;
    public static final Color LAVA_COLOR = RED;
    public static final Color ROCK_COLOR = LIGHT_GRAY;
    public static final Color WALL_COLOR = DARK_GRAY;
    public static final Color WATER_COLOR = BLUE;

    // Word Object Colors
    public static final Color WORD_BABA_COLOR = PINK;
    public static final Color WORD_FLAG_COLOR = YELLOW;
    public static final Color WORD_IS_COLOR = WHITE;
    public static final Color WORD_KILL_COLOR = RED;
    public static final Color WORD_LAVA_COLOR = ORANGE;
    public static final Color WORD_PUSH_COLOR = CYAN;
    public static final Color WORD_ROCK_COLOR = LIGHT_GRAY;
    public static final Color WORD_SINK_COLOR = BLUE;
    public static final Color WORD_STOP_COLOR = RED;
    public static final Color WORD_WALL_COLOR = DARK_GRAY;
    public static final Color WORD_WATER_COLOR = BLUE;
    public static final Color WORD_WIN_COLOR = GREEN;
    public static final Color WORD_YOU_COLOR = WHITE;
}