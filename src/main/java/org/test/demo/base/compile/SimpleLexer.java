package org.test.demo.base.compile;

import java.util.LinkedList;

import static org.test.demo.base.compile.TokenType.*;

/**
 * 词法分析器
 *
 * 正则文法，匹配token（单词）
 *
 * @author gx
 * @create 2019-09-06 17:39
 */
public class SimpleLexer {
    public static void main(String[] args) {
        new SimpleLexer().parse("age >= 45")
                .forEach(t -> System.out.println(t.getType().name() + '\t' + t.getText().toString()));
        new SimpleLexer().parse("int age = 40")
                .forEach(t -> System.out.println(t.getType().name() + '\t' + t.getText().toString()));
        new SimpleLexer().parse("intA = 10")
                .forEach(t -> System.out.println(t.getType().name() + '\t' + t.getText().toString()));
        new SimpleLexer().parse("in = 10")
                .forEach(t -> System.out.println(t.getType().name() + '\t' + t.getText().toString()));
        new SimpleLexer().parse("2+3*5")
                .forEach(t -> System.out.println(t.getType().name() + '\t' + t.getText().toString()));

    }

    private Token token;
    private LinkedList<Token> tokens = new LinkedList<>();

    public LinkedList<Token> parse(String in) {
        final char[] chars = in.toCharArray();

        TokenType state = Initial;
        for (char ch : chars) {
            switch (state) {
            case Initial:
                // 重新确定后续状态
                state = initToken(ch);
                break;
            case Identifier:
                if (isAlpha(ch) || isDigit(ch)) {
                    // 保持标识符状态
                    token.getText().append(ch);
                } else {
                    // 保存token，退出标识符状态
                    tokens.addLast(token);
                    state = initToken(ch);
                }
                break;
            case Id_int1:
                if (ch == 'n') {
                    // 进入特殊状态Id_int2
                    state = Id_int2;
                    token.setType(state);
                    token.getText().append(ch);
                } else if (isAlpha(ch) || isDigit(ch)) {
                    // 进入标识符状态
                    state = Identifier;
                    token.setType(state);
                    token.getText().append(ch);
                } else {
                    // 进入标识符状态
                    state = Identifier;
                    token.setType(state);
                    // 保存token，退出标识符状态
                    tokens.addLast(token);
                    state = initToken(ch);
                }
                break;
            case Id_int2:
                if (ch == 't') {
                    // 进入特殊状态Id_int3
                    state = Id_int3;
                    token.setType(state);
                    token.getText().append(ch);
                } else if (isAlpha(ch) || isDigit(ch)) {
                    // 进入标识符状态
                    state = Identifier;
                    token.setType(state);
                    token.getText().append(ch);
                } else {
                    // 进入标识符状态
                    state = Identifier;
                    token.setType(state);
                    // 保存token，退出标识符状态
                    tokens.addLast(token);
                    state = initToken(ch);
                }
                break;
            case Id_int3:
                if (isBlank(ch)) {
                    // 进入Int状态
                    token.setType(Int);
                    // 保存token，退出标识符状态
                    tokens.addLast(token);
                    state = initToken(ch);
                } else {
                    // 进入标识符状态
                    state = Identifier;
                    token.setType(state);
                    token.getText().append(ch);
                }
                break;
            case GT:
                if (ch == '=') {
                    // 转换成GE
                    state = GE;
                    token.getText().append(ch);
                    token.setType(state);
                } else {
                    // 保存token，退出GT状态
                    tokens.addLast(token);
                    state = initToken(ch);
                }
                break;
            case Plus:
            case Minus:
            case Star:
            case Slash:
            case GE:
                // 保存token，退出GE状态
            case Assignment:
                // 保存token，退出Assignment状态
                tokens.addLast(token);
                state = initToken(ch);
                break;
            case IntLiteral:
                if (isDigit(ch)) {
                    // 保持在数字字面量状态
                    token.getText().append(ch);
                } else {
                    // 保存token，退出数字字面量状态
                    tokens.addLast(token);
                    state = initToken(ch);
                }
                break;
            default:
                break;
            }
        }

        // 避免最后一个token遗漏
        if (!state.equals(Initial)) {
            tokens.addLast(token);
        }

        return tokens;
    }

    private boolean isAlpha(char ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isBlank(int ch) {
        return ch == ' ' || ch == '\t' || ch == '\n';
    }

    /*
     * 初始化token
     */
    private TokenType initToken(char ch) {
        TokenType state = Initial;
        token = new Token(new StringBuilder(), state);

        if (isAlpha(ch)) {
            if (ch == 'i') {
                // 第一个字符是i，进入特殊状态Id_int1
                state = Id_int1;
            } else {
                // 第一个字符是其他字母，进入标识符状态
                state = Identifier;
            }
            token.getText().append(ch);
            token.setType(state);
        } else if (isDigit(ch)) {
            // 第一个字符是数字，进入数字字面量状态
            state = IntLiteral;
            token.getText().append(ch);
            token.setType(state);
        } else if (ch == '>') {
            // 第一个字符是'>'，进入GT状态
            state = GT;
            token.getText().append(ch);
            token.setType(state);
        } else if (ch == '=') {
            // 第一个字符是'='，进入Assignment状态
            state = Assignment;
            token.getText().append(ch);
            token.setType(state);
        } else if (ch == '+') {
            // 第一个字符是'+'，进入Plus状态
            state = Plus;
            token.getText().append(ch);
            token.setType(state);
        } else if (ch == '-') {
            // 第一个字符是'-'，进入Minus状态
            state = Minus;
            token.getText().append(ch);
            token.setType(state);
        } else if (ch == '*') {
            // 第一个字符是'*'，进入Star状态
            state = Star;
            token.getText().append(ch);
            token.setType(state);
        } else if (ch == '/') {
            // 第一个字符是'/'，进入Slash状态
            state = Slash;
            token.getText().append(ch);
            token.setType(state);
        }
        
        return state;
    }
}

class Token {
    private StringBuilder text;
    private TokenType type;

    public Token(StringBuilder text, TokenType type) {
        this.text = text;
        this.type = type;
    }

    public StringBuilder getText() {
        return text;
    }

    public void setText(StringBuilder text) {
        this.text = text;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
}

enum TokenType {
    Initial, Identifier, GT, GE, IntLiteral, Assignment, Int, Plus, Minus, Star, Slash, Id_int1, Id_int2, Id_int3
}
