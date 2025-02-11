package com.empire.rpg.system;

import com.empire.rpg.component.Component;

public interface GameSystem<T extends Component> {
    void update(T component);
}
