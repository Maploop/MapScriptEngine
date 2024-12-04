package me.maploop.script;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.maploop.script.nodes.*;

public class Interpreter {
    private final Map<String, Object> variables = new HashMap<>();
    private final Map<String, FunctionNode> functions = new HashMap<>();

    public void interpret(ProgramNode program) {
        for (FunctionNode function : program.functions) {
            functions.put(function.name, function);
        }

        FunctionNode mainFunction = functions.get("main");
        if (mainFunction == null) {
            throw new RuntimeException("No 'main' function found");
        }

        executeFunction(mainFunction, List.of());
    }

    private Object executeFunction(FunctionNode function, List<Object> args) {
        Map<String, Object> localVariables = new HashMap<>(variables);

        for (int i = 0; i < function.parameters.size(); i++) {
            ParameterNode param = function.parameters.get(i);
            try {
                localVariables.put(param.name, args.get(i));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Expected " + function.parameters.size() + " parameters but received " + args.size());
            }
        }

        Object returnValue = executeBlock(function.body, localVariables);

        variables.putAll(localVariables);

        return returnValue;
    }

    private Object executeBlock(BlockNode block, Map<String, Object> localVariables) {
        for (ASTNode statement : block.statements) {
            if (statement instanceof ReturnNode) {
                return executeExpression(((ReturnNode) statement).value, localVariables);
            } else if (statement instanceof VariableDeclarationNode) {
                executeVariableDeclaration((VariableDeclarationNode) statement, localVariables);
            } else if (statement instanceof FunctionCallNode) {
                executeFunctionCall((FunctionCallNode) statement, localVariables);
            }
        }
        return null;
    }

    private void executeVariableDeclaration(VariableDeclarationNode node, Map<String, Object> localVariables) {
        Object value = executeExpression(node.initializer, localVariables);
        localVariables.put(node.name, value);
    }

    private Object executeExpression(ASTNode expression, Map<String, Object> localVariables) {
        if (expression instanceof LiteralNode) {
            return ((LiteralNode) expression).value;
        } else if (expression instanceof BinaryOperationNode) {
            return executeBinaryOperation((BinaryOperationNode) expression, localVariables);
        } else if (expression instanceof FunctionCallNode) {
            return executeFunctionCall((FunctionCallNode) expression, localVariables);
        } else if (expression instanceof VariableDeclarationNode) {
            VariableDeclarationNode varDecl = (VariableDeclarationNode) expression;
            return localVariables.get(varDecl.name);
        } else if (expression instanceof VariableReferenceNode) {
            String varName = ((VariableReferenceNode) expression).name;
            if (!localVariables.containsKey(varName)) {
                throw new RuntimeException("Undefined variable: " + varName);
            }
            return localVariables.get(varName);
        }
        throw new RuntimeException("Unknown expression type: " + expression.getClass().getName());
    }

    private Object executeBinaryOperation(BinaryOperationNode node, Map<String, Object> localVariables) {
        Object left = executeExpression(node.left, localVariables);
        Object right = executeExpression(node.right, localVariables);

        switch (node.operator) {
            case "+":

                try {
                    return Integer.parseInt(left.toString()) + Integer.parseInt(right.toString());
                } catch (Exception e) {
                    return left.toString() + right.toString();
                }

            case "-":
                return Integer.parseInt(left.toString()) - Integer.parseInt(right.toString());
            case "*":
                return Integer.parseInt(left.toString()) * Integer.parseInt(right.toString());
            case "/":
                return Integer.parseInt(left.toString()) / Integer.parseInt(right.toString());
            case "^":
                return Integer.parseInt(left.toString()) ^ Integer.parseInt(right.toString());
            default:
                throw new RuntimeException("Unknown operator: " + node.operator);
        }
    }

    private Object executeFunctionCall(FunctionCallNode node, Map<String, Object> localVariables) {
        if (node.functionName.equals("sys.println")) {
            for (ASTNode argument : node.arguments) {
                Object value = executeExpression(argument, localVariables);
                System.out.println(value);
            }
            return null;
        }

        FunctionNode function = functions.get(node.functionName);
        if (function == null) {
            throw new RuntimeException("Unknown function: " + node.functionName);
        }

        List<Object> arguments = node.arguments.stream()
                .map(arg -> executeExpression(arg, localVariables))
                .toList();

        return executeFunction(function, arguments);
    }
}
