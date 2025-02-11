package com.empire.rpg.entity;

import com.empire.rpg.component.Component;
import com.empire.rpg.component.StateComponent;

import java.util.Map;
import java.util.UUID;

public class Chest extends Entity{
    private String name;


    public Chest(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Entity addEntity() {
        return null;
    }

    @Override
    public Entity removeEntity(String name) {
        return null;
    }

    public void open() {
        StateComponent state = (StateComponent) this.getComponent(StateComponent.class);
        state.setCurrentState(true);
        state.setNameState("ouvert");
    }

    public void close() {
        StateComponent state = (StateComponent) this.getComponent(StateComponent.class);
        state.setCurrentState(false);
        state.setNameState("fermé");
    }

    public boolean isOpen() {
        StateComponent state = (StateComponent) this.getComponent(StateComponent.class);
        return state.isCurrentState();
    }

    public boolean isClosed() {
        StateComponent state = (StateComponent) this.getComponent(StateComponent.class);
        return !state.isCurrentState();
    }


}
