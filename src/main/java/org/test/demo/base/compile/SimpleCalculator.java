package org.test.demo.base.compile;

import java.util.LinkedList;

/**
 * 语法分析
 * 
 * 上下文无关文法，递归下降算法（左递归无限调用问题），生成抽象语法树
 *
 * 实现一个计算器，但计算的结合性是有问题的。因为它使用了下面的语法规则：
 *
 * additive -> multiplicative | multiplicative + additive multiplicative ->
 * primary | primary * multiplicative
 *
 * 递归项在右边，会自然的对应右结合。我们真正需要的是左结合。
 * 
 * @author gx
 * @create 2019-09-19 11:39
 */
public class SimpleCalculator {
    public static void main(String[] args) {
        // LinkedList<Token> tokens = new SimpleLexer().parse("int age = 45");
        LinkedList<Token> tokens = new SimpleLexer().parse("int age = 2 + 3 * 5");
        try {
            // 生成变量声明语句的抽象语法树
            SimpleASTNode intDeclAstNode = new SimpleCalculator().intDeclare(tokens);
            SimpleASTNode ast = new SimpleASTNode(ASTNodeType.Program, new StringBuilder("Calculator"));
            if (intDeclAstNode != null) {
                ast.addChild(intDeclAstNode);
            }
            ast.forEach("");

            // 计算表达式部分
            System.out.println(new SimpleCalculator().evaluate(ast, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 计算表达式
     */
    int evaluate(SimpleASTNode node, String indent) {
        int result = 0;
        System.out.println(indent + "Calculating: " + node.getType());
        switch (node.getType()) {
        case IntDeclaration:
        case Program:
            for (SimpleASTNode child : node.getChilds()) {
                result = evaluate(child, indent + "\t");
            }
            break;
        case AdditiveExp:
            SimpleASTNode child1 = node.getChilds().get(0);
            SimpleASTNode child2 = node.getChilds().get(1);
            int val1 = evaluate(child1, indent + "\t");
            int val2 = evaluate(child2, indent + "\t");
            result = val1 + val2;
            break;
        case MultiplicativeExp:
            child1 = node.getChilds().get(0);
            child2 = node.getChilds().get(1);
            val1 = evaluate(child1, indent + "\t");
            val2 = evaluate(child2, indent + "\t");
            result = val1 * val2;
            break;
        case IntLiteral:
            result = Integer.parseInt(node.getText().toString());
            break;
        default:
            break;
        }
        System.out.println(indent + "Result: " + result);
        return result;
    }

    // 语法解析：int 类型变量的声明
    // intDeclaration : Int Identifier ('=' additiveExpression)?;
    // 匹配 Int 关键字
    // 匹配标识符
    // 匹配等号
    // 匹配表达式
    SimpleASTNode intDeclare(LinkedList<Token> tokens) throws Exception {
        SimpleASTNode node = null;
        // 预读
        Token token = peek(tokens);
        // 匹配 int
        if (token != null && token.getType() == TokenType.Int) {
            // 消耗掉 int
            read(tokens);
            // 匹配 标识符
            if ((token = peek(tokens)) != null && token.getType() == TokenType.Identifier) {
                // 消耗掉 标识符
                read(tokens);
                // 创建当前节点，并记录变量名到AST节点的文本值中
                node = new SimpleASTNode(ASTNodeType.IntDeclaration, token.getText());
                // 匹配 ‘=’
                if ((token = peek(tokens)) != null && token.getType() == TokenType.Assignment) {
                    // 消耗掉 ‘=’
                    read(tokens);
                    // 匹配表达式
                    SimpleASTNode child = additive(tokens);
                    if (child == null) {
                        throw new Exception("invalide variable initialization, expecting an expression");
                    } else {
                        node.addChild(child);
                    }
                }
            } else {
                throw new Exception("variable name expected");
            }
        }

        return node;
    }

    // 语法解析：算数表达式
    // A->M|A+M ; M->int|M*int

    // additiveExpression
    // : multiplicativeExpression
    // | multiplicativeExpression Plus additiveExpression
    // ;
    SimpleASTNode additive(LinkedList<Token> tokens) throws Exception {
        // 计算第一个子节点
        SimpleASTNode child1 = multiplicative(tokens);
        // 如果没有第二个子节点，就返回这个单节点
        SimpleASTNode node = child1;
        Token token = peek(tokens);
        if (child1 != null && token != null) {
            if (token.getType() == TokenType.Plus) {
                // 消耗 ‘+’
                read(tokens);
                // 递归地解析第二个节点
                SimpleASTNode child2 = additive(tokens);
                if (child2 != null) {
                    // token.getText : '+'
                    node = new SimpleASTNode(ASTNodeType.AdditiveExp, token.getText());
                    node.addChild(child1);
                    node.addChild(child2);
                } else {
                    throw new Exception("invalid additive expression, expecting the right part");
                }
            }
        }

        return node;
    }

    // multiplicativeExpression
    // : IntLiteral
    // | IntLiteral Star multiplicativeExpression
    // ;
    private SimpleASTNode multiplicative(LinkedList<Token> tokens) throws Exception {
        Token token = peek(tokens);
        SimpleASTNode node = null;
        if (token != null && token.getType() == TokenType.IntLiteral) {
            // 消耗掉这个字面量
            read(tokens);
            // 创建节点
            SimpleASTNode child1 = new SimpleASTNode(ASTNodeType.IntLiteral, token.getText());
            node = child1;
            if ((token = peek(tokens)) != null && token.getType() == TokenType.Star) {
                read(tokens);
                // 递归调用
                SimpleASTNode child2 = multiplicative(tokens);
                if (child2 != null) {
                    node = new SimpleASTNode(ASTNodeType.MultiplicativeExp, token.getText());
                    node.addChild(child1);
                    node.addChild(child2);
                } else {
                    throw new Exception("invalid multiplicative expression, expecting the right part");
                }
            }
        }

        return node;
    }

    Token peek(LinkedList<Token> tokens) {
        if (tokens == null || tokens.size() < 1) {
            return null;
        }
        return tokens.getFirst();
    }

    Token read(LinkedList<Token> tokens) {
        if (tokens == null || tokens.size() < 1) {
            return null;
        }
        return tokens.removeFirst();
    }
}

class SimpleASTNode {
    private LinkedList<SimpleASTNode> childs = new LinkedList<>();
    // 节点类型
    private ASTNodeType type;
    // 节点值（如标识符）
    private StringBuilder text;

    public SimpleASTNode(ASTNodeType type, StringBuilder text) {
        this.type = type;
        this.text = text;
    }

    public ASTNodeType getType() {
        return type;
    }

    public void setType(ASTNodeType type) {
        this.type = type;
    }

    public StringBuilder getText() {
        return text;
    }

    public void setText(StringBuilder text) {
        this.text = text;
    }

    public LinkedList<SimpleASTNode> getChilds() {
        return childs;
    }

    public void setChilds(LinkedList<SimpleASTNode> childs) {
        this.childs = childs;
    }

    public void addChild(SimpleASTNode child) {
        this.childs.addLast(child);
    }

    public void forEach(String indent) {
        System.out.println(indent + this.type.name() + " : " + this.text.toString());
        childs.forEach(t -> {
            t.forEach(indent + "\t");
        });
    }
}

enum ASTNodeType {
    IntDeclaration, AdditiveExp, MultiplicativeExp, IntLiteral, Program
}