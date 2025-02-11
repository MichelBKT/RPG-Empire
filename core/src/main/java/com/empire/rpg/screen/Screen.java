package com.empire.rpg.screen;

public interface Screen {
    void show();
    void render(float delta);
    void resize(int width, int height);
    void dispose();

}
