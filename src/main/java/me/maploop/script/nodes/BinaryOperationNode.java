package me.maploop.script.nodes;

public class BinaryOperationNode extends ExpressionNode {
    public ASTNode left;
    public String operator;
    public ASTNode right;

    public BinaryOperationNode(ASTNode left, String operator, ASTNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
