package me.maploop.script.nodes;

import java.util.List;

public class FunctionCallNode extends ExpressionNode {
    public String functionName;
    public List<ASTNode> arguments;

    public FunctionCallNode(String functionName, List<ASTNode> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }
}
