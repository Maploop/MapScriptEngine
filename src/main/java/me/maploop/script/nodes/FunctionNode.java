package me.maploop.script.nodes;

import java.util.List;

public class FunctionNode extends ASTNode {
    public String name;
    public List<ParameterNode> parameters;
    String returnType;
    public BlockNode body;

    public FunctionNode(String name, List<ParameterNode> parameters, String returnType, BlockNode body) {
        this.name = name;
        this.parameters = parameters;
        this.returnType = returnType;
        this.body = body;
    }
}
