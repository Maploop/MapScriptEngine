package me.maploop;

import me.maploop.script.Interpreter;
import me.maploop.script.Lexer;
import me.maploop.script.Parser;
import me.maploop.script.Token;
import me.maploop.script.nodes.ProgramNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File file = new File("./main.maps");

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Lexer lexer = new Lexer(content.toString());
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        ProgramNode program = parser.parseProgram();

        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
    }
}