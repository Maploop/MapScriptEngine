package me.maploop.script.nodes;

public class VariableReferenceNode extends ExpressionNode {
    public String name;

    public VariableReferenceNode(String name) {
        this.name = name;
    }
}
