/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolter.jackcompiler.jackcompiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author wolter
 */

public class CompilationEngine {
    
    private final JackTokenizer tokenizer;
    private String xml;
    private final ArrayList<String> operadores = new ArrayList<>();
    private final ArrayList<String> operadoresUnarios = new ArrayList<>();
    private final ArrayList<String> listaTipos = new ArrayList<>();
    private final ArrayList<String> listaTiposSubrotina = new ArrayList<>();
    private final String op[] = {"+","-","*","/","&","|","<",">","="};
    private final String opUn[] = {"-","~"};
    private final String tipos[] = {"int","char","boolean","Square"};
    private final String tiposSubrotina[] = {"constructor","function","method"};
    
    private void tagTerminal(String terminal)
    {
        xml += terminal + "\n";
    }
    
    private void iniciarTagNaoTerminal(String naoTerminal)
    {
        xml += "<" + naoTerminal + ">\n";
    }
    
    private void fecharTagNaoTerminal(String naoTerminal)
    {
       xml += "</" + naoTerminal + ">\n";
    }
    
    private void xmlToken(String token)
    {
        if(tokenizer.getToken().equals(token))
        {
            tagTerminal(tokenizer.tagToken());
            tokenizer.advance();
        }
        else{
            System.out.println("Erro -> token incorreto: " + token);
            System.exit(0);
        }
    }
    
    private void xmlTipoToken(String tipo)
    {
        String tipoToken;
        tipoToken = tokenizer.tokenType().toString().toLowerCase();
        if(tipoToken.equals(tipo))
        {
            xml += "<" + tipoToken + ">" + tokenizer.getToken() + "</" + tipoToken + ">\n";
            tokenizer.advance();
        }
        else{
            System.out.println("Erro -> tipo de token incorreto: " + tipo);
            System.exit(0);
        }
    }
    
    public CompilationEngine(String entrada) throws IOException
    {
        tokenizer = new JackTokenizer(entrada);
        tokenizer.advance();
        xml = "";
        Collections.addAll(operadores, op);
        Collections.addAll(operadoresUnarios, opUn);
        Collections.addAll(listaTipos, tipos);
        Collections.addAll(listaTiposSubrotina, tiposSubrotina);
    }
    
    /* Não Terminal */
    
    // 'class' className '{' classVarDec* subroutineDec* '}'
    public void compileClass()
    {
        iniciarTagNaoTerminal("class");
        xmlToken("class");
        className();
        xmlToken("{");
        compileClassVarDec();
        compileSubroutine();
        xmlToken("}");
        fecharTagNaoTerminal("class");
    }
    
    // identifier
    private void className()
    {
        xmlTipoToken("identifier");
    }
    
    // ( 'static' | 'field' ) type varName ( ',' varName)* ';'
    public void compileClassVarDec()
    {
        if(tokenizer.getToken().equals("static") || tokenizer.getToken().equals("field"))
        {
            iniciarTagNaoTerminal("classVarDec");
            xmlToken(tokenizer.getToken());
            type();
            varName();
            while(tokenizer.getToken().equals(","))
            {
                xmlToken(",");
                varName();
            }
            xmlToken(";");
            fecharTagNaoTerminal("classVarDec");
            compileClassVarDec();
        }
    }
    
    // 'int' | 'char' | 'boolean' | className
    private void type()
    {
        if(listaTipos.contains(tokenizer.getToken()))
        {
            xmlToken(tokenizer.getToken());
        }
        else{
            System.out.println("Erro -> tipo não definido: " + tokenizer.getToken() );
            System.exit(0);
        }
    }
    
    // identifier
    private void varName()
    {
        xmlTipoToken("identifier");
    }
    
    // ( 'constructor' | 'function' | 'method' ) ( 'void' | type) subroutineName '(' parameterList ')' subroutineBody
    public void compileSubroutine()
    {
        if(listaTiposSubrotina.contains(tokenizer.getToken()))
        {
            iniciarTagNaoTerminal("subroutineDec");
            xmlToken(tokenizer.getToken());
            if(tokenizer.getToken().equals("void"))
            {
                xmlToken("void");
            }
            else{
                type();
            }
            subroutineName();
            xmlToken("(");
            parameterList();
            xmlToken(")");
            subroutineBody();
            fecharTagNaoTerminal("subroutineDec");
            compileSubroutine();
        }
    }
    
    // identifier
    private void subroutineName()
    {
        xmlTipoToken("identifier");
    }
    
    // ((type varName) ( ',' type varName)*)?
    private void parameterList()
    {
        iniciarTagNaoTerminal("parameterList");
        while(!tokenizer.getToken().equals(")"))
        {
            type();
            varName();
            if(tokenizer.getToken().equals(","))
            {
                xmlToken(",");
            }
        }
        fecharTagNaoTerminal("parameterList");
    }
    
    // '{' varDec* statements '}'
    private void subroutineBody()
    {
        iniciarTagNaoTerminal("subroutineBody");
        xmlToken("{");
        while(tokenizer.getToken().equals("var"))
        {
            varDec();
        }
        compileStatements();
        xmlToken("}");
        fecharTagNaoTerminal("subroutineBody");
    }
    
    // 'var' type varName ( ',' varName)* ';'
    private void varDec()
    {
        iniciarTagNaoTerminal("varDec");
        xmlToken("var");
        type();
        varName();
        while(tokenizer.getToken().equals(","))
        {
            xmlToken(",");
            varName();
        }
        xmlToken(";");
        fecharTagNaoTerminal("varDec");
    }

    /*Statements*/

    // statement*
    public void compileStatements()
    {
        iniciarTagNaoTerminal("statements");
        while(!tokenizer.getToken().equals("}"))
        {
            statement();
        }
        fecharTagNaoTerminal("statements");
    }
    
    // letStatement | ifStatement | whileStatement | doStatement | returnStatement
    private void statement()
    {
        switch (tokenizer.keyWord()) {
            case LET:
                compileLet();
                break;
            case IF:
                compileIf();
                break;
            case WHILE:
                compileWhile();
                break;
            case DO:
                compileDo();
                break;
            case RETURN:
                compileReturn();
                break;
            default:
                System.out.println("Erro -> statement não definido");
                break;
        }
    }

    // 'let' varName ( '[' expression ']' )? '=' expression ';'
    public void compileLet()
    {
        iniciarTagNaoTerminal("letStatement");
        xmlToken("let");
        varName();
        if(tokenizer.getToken().equals("["))
        {
            xmlToken("[");
            compileExpression();
            xmlToken("]");
        }
        xmlToken("=");
        compileExpression();
        xmlToken(";");
        fecharTagNaoTerminal("letStatement");
    }
    
    // 'if' '(' expression ')' '{' statements '}' ( 'else' '{' statements '}' )?
    public void compileIf()
    {
        iniciarTagNaoTerminal("ifStatement");
        xmlToken("if");
        xmlToken("(");
        compileExpression();
        xmlToken(")");
        xmlToken("{");
        compileStatements();
        xmlToken("}");
        if(tokenizer.getToken().equals("else"))
        {
            xmlToken("else");
            xmlToken("{");
            compileStatements();
            xmlToken("}");
        }
        fecharTagNaoTerminal("ifStatement");
    }

    // 'while' '(' expression ')' '{' statements '}'
    public void compileWhile() 
    {
        iniciarTagNaoTerminal("whileStatement");
        xmlToken("while");
        xmlToken("(");
        compileExpression();
        xmlToken(")");
        xmlToken("{");
        compileStatements();
        xmlToken("}");
        fecharTagNaoTerminal("whileStatement");
    }
    
    // 'do' subroutineCall ';'
    public void compileDo()
    {
        iniciarTagNaoTerminal("doStatement");
        xmlToken("do");
        subroutineCall();
        xmlToken(";");
        fecharTagNaoTerminal("doStatement");
    }
    
    // 'return' expression? ';'
    public void compileReturn()
    {
        iniciarTagNaoTerminal("returnStatement");
        xmlToken("return");
        if(!tokenizer.getToken().equals(";"))
        {
            compileExpression();
        }
        xmlToken(";");
        fecharTagNaoTerminal("returnStatement");
    }
    
    /* Expressions */
    
    // term (op term)
    public void compileExpression()
    {
        iniciarTagNaoTerminal("expression");
        compileTerm();
        while(tokenizer.getToken().contains(operadores.toString()))
        {
            op();
            compileTerm();
        }
        fecharTagNaoTerminal("expression");
    }
    
    // '+' | '-' | '* | '/' | '&' | '|' | '<' | '>' | '='
    private void op()
    {
        if(tokenizer.getToken().contains(operadores.toString()))
        {
            xmlToken(tokenizer.getToken());
        } else
        {
            System.out.println("Erro -> operador incorreto");
            System.exit(0);
        }
    }
    
    // '-' | '~'
    private void unaryOp()
    {
        if(tokenizer.getToken().contains(operadoresUnarios.toString()))
        {
            xmlToken(tokenizer.getToken());
        } else
        {
            System.out.println("Erro -> operador unario incorreto");
            System.exit(0);
        }
        
    }
    
    // integerConstant | stringConstant | keywordConstant | varName
    public void compileTerm()
    {
        iniciarTagNaoTerminal("term");
        if(tokenizer.tokenType().equals(Token.INTCONST) ||
           tokenizer.tokenType().equals(Token.STRINGCONST) ||
           tokenizer.tokenType().equals(Token.KEYWORD)){
            xmlToken(tokenizer.getToken());
        } else
        {
            varName();
        }
        fecharTagNaoTerminal("term");
    }
    
    // (expression ( ',' expression)* )?
    public void compileExpressionList()
    {
        iniciarTagNaoTerminal("expressionList");
        while(!tokenizer.getToken().equals(")"))
        {
            compileExpression();
            if(tokenizer.getToken().equals(","))
            {
                xmlToken(",");
            }
        }
        fecharTagNaoTerminal("expressionList");
    }
    
    // subroutineName '(' expressionList ')' | (className|varName) '.' subroutineName '(' expressionList ')'
    private void subroutineCall()
    {
        className();
        if(tokenizer.getToken().equals("."))
        {
            xmlToken(".");
            subroutineName();
            xmlToken("(");
            compileExpressionList();
            xmlToken(")");
        } else
        {
            xmlToken("(");
            compileExpressionList();
            xmlToken(")");
        }
    }  
    
    public void compilarXml()
    {
        System.out.println(xml);
    }
}
