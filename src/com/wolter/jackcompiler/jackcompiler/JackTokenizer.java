/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wolter.jackcompiler.jackcompiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wolter
 */
public class JackTokenizer {

    private final File arquivo;
    private FileReader fr;
    private BufferedReader br;
    
    private String tokenCorrente;
    private int indiceTokenCorrente = -1;
    private String linhaCodigo;
    private String codigo = "";
    private final List<String> listaTokens = new ArrayList<>();
    
    // Expressões regulares
    private static final String rKeyWords = "int|class|constructor|function|method|"
                                            + "field|static|var|char|boolean|void|"
                                            + "true|false|null|this|let|do|if|else|"
                                            + "while|return";
    private static final String rSymbols = "[{}()\\[\\].,;+-/&|<>=~*]";
    private static final String rIntConst = "[0-9]+";
    private static final String rStringConst = "(\".+?\")"; // String entre aspas
    private static final String rIdentifier = "[a-zA-Z][a-zA-Z0-9_]*";
    
    private static final String combinarRegex = rKeyWords + "|" + rSymbols + "|"
                                                + rIntConst + "|" + rStringConst + "|"
                                                + rIdentifier;
    
    private final Pattern padrao = Pattern.compile(combinarRegex);
    private Matcher m;
    
    public JackTokenizer(String entrada) throws FileNotFoundException, IOException
    {
        arquivo = new File(entrada);
        if(!arquivo.exists())
        {
            System.out.println("Arquivo não existe");
            System.exit(0);
        }
        fr = new FileReader(arquivo);
        br = new BufferedReader(fr);
        while(br.ready())
        {
            linhaCodigo = br.readLine();
            codigo += linhaCodigo+"\n";
        }
        // Remove comentarios do codigo
        codigo = codigo.replaceAll("(?m)\\s*//.+$", "");
        // Remove linhas em branco
        codigo = codigo.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
        
        m = padrao.matcher(codigo);
        while(m.find())
        {
            listaTokens.add(m.group());
        }
    }
    
    public boolean hasMoreTokens()
    {
        return indiceTokenCorrente != listaTokens.size()-1;
    }
    
    public void advance()
    {
        if(hasMoreTokens())
        {
            if(indiceTokenCorrente == -1)
            {
                indiceTokenCorrente = 0;
            }
            else
            {
                indiceTokenCorrente++;
            }
        }
    }
    
    public Token tokenType()
    {
        Token tipoToken = null;
        tokenCorrente = listaTokens.get(indiceTokenCorrente);
        if(tokenCorrente.matches(rKeyWords))
        {
            tipoToken = Token.KEYWORD;
        }
        else
        {
            if(tokenCorrente.matches(rSymbols))
            {
                tipoToken = Token.SYMBOL;
            } else if(tokenCorrente.matches(rIntConst))
            {
                tipoToken = Token.INTCONST;
            } else if(tokenCorrente.matches(rStringConst))
            {
                tipoToken = Token.STRINGCONST;
            } else if(tokenCorrente.matches(rIdentifier))
            {
                tipoToken = Token.IDENTIFIER;
            }
        }
        return tipoToken;
    }
    
    public Keyword keyWord()
    {
        Keyword tipoKeyword = null;
        tokenCorrente = listaTokens.get(indiceTokenCorrente);
        if("class".equals(tokenCorrente))
            tipoKeyword = Keyword.CLASS;
        if("method".equals(tokenCorrente))
            tipoKeyword = Keyword.METHOD;
        if("function".equals(tokenCorrente))
            tipoKeyword = Keyword.FUNCTION;
        if("constructor".equals(tokenCorrente))
            tipoKeyword = Keyword.CONSTRUCTOR;
        if("int".equals(tokenCorrente))
            tipoKeyword = Keyword.INT;
        if("boolean".equals(tokenCorrente))
            tipoKeyword = Keyword.BOOLEAN;
        if("char".equals(tokenCorrente))
            tipoKeyword = Keyword.CHAR;
        if("void".equals(tokenCorrente))
            tipoKeyword = Keyword.VOID;
        if("var".equals(tokenCorrente))
            tipoKeyword = Keyword.VAR;
        if("static".equals(tokenCorrente))
            tipoKeyword = Keyword.STATIC;
        if("field".equals(tokenCorrente))
            tipoKeyword = Keyword.FIELD;
        if("let".equals(tokenCorrente))
            tipoKeyword = Keyword.LET;
        if("do".equals(tokenCorrente))
            tipoKeyword = Keyword.DO;
        if("if".equals(tokenCorrente))
            tipoKeyword = Keyword.IF;
        if("else".equals(tokenCorrente))
            tipoKeyword = Keyword.ELSE;
        if("while".equals(tokenCorrente))
            tipoKeyword = Keyword.WHILE;
        if("return".equals(tokenCorrente))
            tipoKeyword = Keyword.RETURN;
        if("true".equals(tokenCorrente))
            tipoKeyword = Keyword.TRUE;
        if("false".equals(tokenCorrente))
            tipoKeyword = Keyword.FALSE;
        if("null".equals(tokenCorrente))
            tipoKeyword = Keyword.NULL;
        if("this".equals(tokenCorrente))
            tipoKeyword = Keyword.THIS;
        return tipoKeyword;
    }
    
    public char symbol()
    {
        char tokenSymbol;
        tokenSymbol = listaTokens.get(indiceTokenCorrente).charAt(0);
        return tokenSymbol;
    }
    
    public String identifier()
    {
        String tokenIdentifier;
        tokenIdentifier = listaTokens.get(indiceTokenCorrente);
        return tokenIdentifier;
    }
    
    public int intVal()
    {
        int tokenIntVal;
        tokenIntVal = Integer.parseInt(listaTokens.get(indiceTokenCorrente));
        return tokenIntVal;
    }
    
    public String stringVal()
    {
        String tokenStringVal;
        tokenStringVal = listaTokens.get(indiceTokenCorrente);
        tokenStringVal = tokenStringVal.substring(1, tokenStringVal.length()-1);
        return tokenStringVal;
    }
    
    public String tagToken()
    {
        String tokenTag = null;
        tokenCorrente = listaTokens.get(indiceTokenCorrente);
        if(this.tokenType() == Token.KEYWORD)
        {
            tokenTag = "<keyword>" + tokenCorrente + "</keyword>";
        }
        if(this.tokenType() == Token.IDENTIFIER)
        {
            tokenTag = "<identifier>" + tokenCorrente + "</identifier>";
        }
        if(this.tokenType() == Token.INTCONST)
        {
            tokenTag = "<integerConstant>" + tokenCorrente + "</integerConstant>";
        }
        if(this.tokenType() == Token.STRINGCONST)
        {
            tokenTag = "<stringConstant>" + this.stringVal() + "</stringConstant>";
        }
        if(this.tokenType() == Token.SYMBOL)
        {
            if("<".equals(tokenCorrente))
            {
                tokenTag = "<symbol>&lt</symbol>";
            } else if(">".equals(tokenCorrente))
            {
                tokenTag = "<symbol>&gt</symbol>";
            } else if("\"".equals(tokenCorrente))
            {
                tokenTag = "<symbol>&quot</symbol>";
            } else if("&".equals(tokenCorrente))
            {
                tokenTag = "<symbol>&amp</symbol>";
            } else {
                tokenTag = "<symbol>" +tokenCorrente + "</symbol>";
            }
        }
        return tokenTag;
    }
}
