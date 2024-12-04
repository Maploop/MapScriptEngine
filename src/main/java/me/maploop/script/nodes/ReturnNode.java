package me.maploop.script.nodes;

public class ReturnNode extends ASTNode {
    public ASTNode value;

    public ReturnNode(ASTNode value) {
        this.value = value;
    }
}
