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

public class MapScriptEngine {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("[ERROR] No file provided! (Correct usage: java -jar MapScriptEngine.jar path/to/.maps/file)");
            System.exit(0);
        }
        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println("[ERROR] Could not find file " + file.getAbsolutePath() + "! Are you sure the provided path is correct?");
            System.exit(0);
        }
        if (!file.getName().endsWith(".maps"))
            System.out.println("[WARNING] File extension must be .maps! The program will continue to run but keep in mind this is bad practice.");

        System.out.println("[INFO] Running program: " + file.getName());
        System.out.println(" ");
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