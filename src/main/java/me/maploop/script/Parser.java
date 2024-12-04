package me.maploop.script;

import me.maploop.script.nodes.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int position = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ProgramNode parseProgram() {
        List<FunctionNode> functions = new ArrayList<>();
        while (!isAtEnd()) {
            functions.add(parseFunction());
        }
        return new ProgramNode(functions);
    }

    private FunctionNode parseFunction() {
        match(TokenType.FN);
        String name = match(TokenType.IDENTIFIER).value;
        match(TokenType.OPEN_PAREN);

        List<ParameterNode> parameters = new ArrayList<>();
        if (!check(TokenType.CLOSE_PAREN)) {
            do {
                parameters.add(parseParameter());
            } while (matchIf(TokenType.COMMA));
        }
        match(TokenType.CLOSE_PAREN);

        match(TokenType.ARROW);
        String returnType = match(TokenType.IDENTIFIER).value;

        BlockNode body = parseBlock();
        return new FunctionNode(name, parameters, returnType, body);
    }

    private ParameterNode parseParameter() {
        String name = match(TokenType.IDENTIFIER).value;
        match(TokenType.COLON);
        String type = match(TokenType.IDENTIFIER).value;
        return new ParameterNode(name, type);
    }

    private BlockNode parseBlock() {
        match(TokenType.OPEN_BRACE);
        List<ASTNode> statements = new ArrayList<>();
        while (!check(TokenType.CLOSE_BRACE)) {
            statements.add(parseStatement());
        }
        match(TokenType.CLOSE_BRACE);
        return new BlockNode(statements);
    }

    private ASTNode parseStatement() {
        if (matchIf(TokenType.RETURN)) {
            return new ReturnNode(parseExpression());
        } else if (matchIf(TokenType.VAR)) {
            return parseVariableDeclaration();
        } else if (check(TokenType.IDENTIFIER) && lookAhead(1).type == TokenType.OPEN_PAREN) {
            return parseFunctionCall();
        } else if (check(TokenType.SYS) && lookAhead(1).type == TokenType.DOT && lookAhead(2).type == TokenType.PRINTLN) {
            return parseSysPrintln();
        }
        throw new RuntimeException("Unexpected token: " + peek());
    }

    private VariableDeclarationNode parseVariableDeclaration() {
        String name = match(TokenType.IDENTIFIER).value;
        match(TokenType.COLON);
        String type = match(TokenType.IDENTIFIER).value;
        match(TokenType.EQUALS);
        ASTNode initializer = parseExpression();
        return new VariableDeclarationNode(name, type, initializer);
    }

    private FunctionCallNode parseFunctionCall() {
        String functionName = match(TokenType.IDENTIFIER).value;
        match(TokenType.OPEN_PAREN);

        List<ASTNode> arguments = new ArrayList<>();
        if (!check(TokenType.CLOSE_PAREN)) {
            do {
                arguments.add(parseExpression());
            } while (matchIf(TokenType.COMMA));
        }
        match(TokenType.CLOSE_PAREN);
        return new FunctionCallNode(functionName, arguments);
    }

    private ASTNode parseExpression() {
        ASTNode left = parsePrimary();
        if (matchIf(TokenType.PLUS) || matchIf(TokenType.MINUS) ||
                matchIf(TokenType.MULTIPLY) || matchIf(TokenType.DIVIDE) || matchIf(TokenType.POWER)) {
            String operator = previous().value;
            ASTNode right = parseExpression();
            return new BinaryOperationNode(left, operator, right);
        }
        return left;
    }

    private ASTNode parsePrimary() {
        if (matchIf(TokenType.NUMBER)) {
            return new LiteralNode(previous().value);
        } else if (matchIf(TokenType.STRING)) {
            return new LiteralNode(previous().value);
        } else if (matchIf(TokenType.IDENTIFIER)) {
            String identifier = previous().value;
            if (matchIf(TokenType.OPEN_PAREN)) {
                // Parse function call arguments
                List<ASTNode> arguments = new ArrayList<>();
                if (!check(TokenType.CLOSE_PAREN)) {
                    do {
                        arguments.add(parseExpression());
                    } while (matchIf(TokenType.COMMA));
                }
                match(TokenType.CLOSE_PAREN);
                return new FunctionCallNode(identifier, arguments);
            }
            // If not a function call, treat it as a variable reference
            return new VariableReferenceNode(identifier);
        }
        throw new RuntimeException("Unexpected token: " + peek());
    }

    private ASTNode parseSysPrintln() {
        match(TokenType.SYS);
        match(TokenType.DOT);
        match(TokenType.PRINTLN);
        match(TokenType.OPEN_PAREN);

        List<ASTNode> arguments = new ArrayList<>();
        if (!check(TokenType.CLOSE_PAREN)) {
            do {
                arguments.add(parseExpression());
            } while (matchIf(TokenType.COMMA));
        }
        match(TokenType.CLOSE_PAREN);

        return new FunctionCallNode("sys.println", arguments);
    }

    private boolean matchIf(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private Token match(TokenType type) {
        if (check(type)) {
            return advance();
        }
        throw new RuntimeException("Expected token: " + type + ", but got: " + peek());
    }

    private boolean check(TokenType type) {
        return !isAtEnd() && peek().type == type;
    }

    private Token lookAhead(int offset) {
        if (position + offset >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(position + offset);
    }

    private Token advance() {
        return tokens.get(position++);
    }

    private Token previous() {
        return tokens.get(position - 1);
    }

    private boolean isAtEnd() {
        return position >= tokens.size() || peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(position);
    }
}
