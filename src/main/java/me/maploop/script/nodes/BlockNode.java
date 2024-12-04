package me.maploop.script.nodes;

import java.util.List;

public class BlockNode extends ASTNode {
    public List<ASTNode> statements;

    public BlockNode(List<ASTNode> statements) {
        this.statements = statements;
    }
}
