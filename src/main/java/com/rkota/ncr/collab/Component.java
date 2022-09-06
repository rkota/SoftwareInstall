package com.rkota.ncr.collab;

import java.util.*;

public class Component {
    String name;
    Set<Component> parents = new HashSet<Component>();
    Set<Component> deps =  new HashSet<Component>();
    boolean installed = false;
    public Component(String name){
        this.name= name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Component> getParents() {
        return parents;
    }


    public Set<Component> getDeps() {
        return deps;
    }


    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Component)) return false;
        Component component = (Component) o;
        return Objects.equals(name, component.name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


}
