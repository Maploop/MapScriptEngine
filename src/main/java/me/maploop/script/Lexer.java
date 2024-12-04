package me.maploop.script;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String source;
    private int position = 0;

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (!isAtEnd()) {
            char current = peek();
            if (Character.isWhitespace(current)) {
                advance(); // Ignore whitespace
            } else if (Character.isLetter(current) || current == '_') {
                tokens.add(identifierOrKeyword());
            } else if (Character.isDigit(current)) {
                tokens.add(number());
            } else if (current == '"') {
                tokens.add(string());
            } else {
                tokens.add(symbol());
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private Token identifierOrKeyword() {
        StringBuilder builder = new StringBuilder();
        while (Character.isLetterOrDigit(peek()) || peek() == '_') {
            builder.append(advance());
        }
        String word = builder.toString();
        switch (word) {
            case "fn": return new Token(TokenType.FN, word);
            case "var": return new Token(TokenType.VAR, word);
            case "return": return new Token(TokenType.RETURN, word);
            case "sys": return new Token(TokenType.SYS, word);
            case "println": return new Token(TokenType.PRINTLN, word);
            default: return new Token(TokenType.IDENTIFIER, word);
        }
    }

    private Token number() {
        StringBuilder builder = new StringBuilder();
        while (Character.isDigit(peek())) {
            builder.append(advance());
        }
        return new Token(TokenType.NUMBER, builder.toString());
    }

    private Token string() {
        advance();
        StringBuilder builder = new StringBuilder();
        while (peek() != '"' && !isAtEnd()) {
            builder.append(advance());
        }
        if (isAtEnd()) {
            throw new RuntimeException("Unterminated string literal");
        }
        advance();
        return new Token(TokenType.STRING, builder.toString());
    }

    private Token symbol() {
        char current = advance();
        switch (current) {
            case ':': return new Token(TokenType.COLON, ":");
            case ',': return new Token(TokenType.COMMA, ",");
            case '(': return new Token(TokenType.OPEN_PAREN, "(");
            case ')': return new Token(TokenType.CLOSE_PAREN, ")");
            case '{': return new Token(TokenType.OPEN_BRACE, "{");
            case '}': return new Token(TokenType.CLOSE_BRACE, "}");
            case '[': return new Token(TokenType.OPEN_BRACKET, "[");
            case ']': return new Token(TokenType.CLOSE_BRACKET, "]");
            case '.': return new Token(TokenType.DOT, ".");
            case '+': return new Token(TokenType.PLUS, "+");
            case '-': return new Token(TokenType.MINUS, "-");
            case '*': return new Token(TokenType.MULTIPLY, "*");
            case '/': return new Token(TokenType.DIVIDE, "/");
            case '^': return new Token(TokenType.POWER, "^");
            case '=':
                if (peek() == '>') {
                    advance();
                    return new Token(TokenType.ARROW, "=>");
                }
                return new Token(TokenType.EQUALS, "=");
            default:
                throw new RuntimeException("Unexpected character: " + current);
        }
    }

    private char peek() {
        return isAtEnd() ? '\0' : source.charAt(position);
    }

    private char advance() {
        return source.charAt(position++);
    }

    private boolean isAtEnd() {
        return position >= source.length();
    }
}
