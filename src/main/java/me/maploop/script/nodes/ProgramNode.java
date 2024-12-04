package me.maploop.script.nodes;

import java.util.List;

public class ProgramNode extends ASTNode {
    public List<FunctionNode> functions;

    public ProgramNode(List<FunctionNode> functions) {
        this.functions = functions;
    }
}
