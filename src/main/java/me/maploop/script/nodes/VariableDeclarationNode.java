package me.maploop.script.nodes;

public class VariableDeclarationNode extends ASTNode {
    public String name;
    String type;
    public ASTNode initializer;

    public VariableDeclarationNode(String name, String type, ASTNode initializer) {
        this.name = name;
        this.type = type;
        this.initializer = initializer;
    }
}
