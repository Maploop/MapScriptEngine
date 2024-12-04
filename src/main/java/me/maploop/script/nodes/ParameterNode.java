package me.maploop.script.nodes;

public class ParameterNode extends ASTNode {
    public String name;
    String type;

    public ParameterNode(String name, String type) {
        this.name = name;
        this.type = type;
    }
}

