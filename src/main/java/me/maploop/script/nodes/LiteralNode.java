package me.maploop.script.nodes;

public class LiteralNode extends ExpressionNode {
    public String value;

    public LiteralNode(String value) {
        this.value = value;
    }
}
