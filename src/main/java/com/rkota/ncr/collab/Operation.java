package com.rkota.ncr.collab;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Operation {
    OperationType type;

    public enum OperationType {

        DEPEND,
        INSTALL,
        REMOVE,
        LIST

    }

    Set<Component> installComponents;
    String[] input;

    String inputString;


    public Operation(Set<Component> installed, String inputString) {
        this.inputString = inputString;
        this.input = inputString.split("\\s");
        this.type = OperationType.valueOf(input[0]);
        this.installComponents = installed;
        this.input = input;

    }

    public void process() {
        switch (type) {
            case DEPEND:
                System.out.println(inputString);
                Component parent = getComponent(input[1].trim());
                if (parent == null) {
                    parent = new Component(input[1]);
                    installComponents.add(parent);
                }

                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < input.length; i++) {
                    sb.append(input[i] + " ");
                    Component component = getComponent(input[i].trim());
                    if (component == null) {
                        component = new Component(input[i]);
                    }
                    if (component.getParents().contains(parent) ||
                            component.getDeps().contains(parent)) {
                        System.out.println(sb.toString() + " depends on " + parent.getName() + ", ignoring command");
                        continue;
                    }
                    component.getParents().add(parent);
                    parent.getDeps().add(component);
                }


                break;
            case INSTALL:
                System.out.println(inputString);
                Component installComp = getComponent(input[1]);
                if (installComp != null) {
                    if (installComp.isInstalled()) {
                        System.out.println(input[1] + " is already installed");
                        break;
                    }
                    installComp(installComponents, installComp);
                } else {
                    System.out.println("installing " + input[1]);
                    Component installNew = new Component(input[1]);
                    installNew.setInstalled(true);
                    installComponents.add(installNew);
                }
                break;

            case REMOVE:
                System.out.println(inputString);
                Component c = getComponent(input[1]);
                if (c == null || !c.isInstalled()) {
                    System.out.println(input[1] + " is not installed");
                    break;
                }
                if (c.getParents().size() > 0) {
                    System.out.println(c.getName() + " is still needed");
                    break;
                }

                removeComp(c, null);
                break;
            case LIST:
                System.out.println(inputString);
                desplayList(installComponents, new ArrayList<String>());

        }
    }


    private void desplayList(Set<Component> installComponents, List<String> dagCheck) {
        for (Component component : installComponents) {
            if (dagCheck.contains(component.getName())) continue;
            System.out.println(component.getName());
            dagCheck.add(component.getName());
            desplayList(component.getDeps(), dagCheck);
        }
    }

    private void removeComp(Component c, Component parent) {
        if (c.getParents().size() == 0) {
            parent = c;
            System.out.println("Removing " + c.getName());
            c.setInstalled(false);
        }
        for (Component dep : c.getDeps()) {
            dep.getParents().remove(parent);
            removeComp(dep, parent);
        }
    }

    private void installComp(Set<Component> installComponents, Component installComp) {

        if (!installComp.isInstalled()) {
            for (Component c : installComp.getDeps()) {
                if (c.installed) continue;
                installComp(c.getDeps(), c);
                c.setInstalled(true);
            }
            if (!installComp.isInstalled()) {
                System.out.println("Installing " + installComp.getName());
                installComp.setInstalled(true);
            }
        }
    }

    private Component getComponent(String s) {
        Set<Component> arr = new HashSet<>();
        for (Component c : installComponents) {
            if (c.name.equalsIgnoreCase(s)) return c;
            arr.add(c);
            Component ret = searchChild(arr, c.getDeps(), s);
            if (ret == null) continue;
            else return ret;

        }
        return null;
    }

    private Component searchChild(Set<Component> arr, Set<Component> deps, String s) {
        for (Component c : deps) {
            if (arr.contains(c)) continue;
            arr.add(c);
            if (c.name.equalsIgnoreCase(s)) return c;
            Component ret = searchChild(arr, c.getDeps(), s);
            if (ret == null) continue;
            else return ret;
        }
        return null;
    }

    private boolean isInstalled(String s) {
        if (installComponents.contains(s)) {
            return true;
        }
        return false;
    }

}
