/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolter.jackcompiler.jackcompiler;

import java.io.IOException;

/**
 *
 * @author wolter
 */
public class CompilationEngine {
    
    private final JackTokenizer tokenizer;
    
    public CompilationEngine(String entrada) throws IOException
    {
        tokenizer = new JackTokenizer(entrada);
        tokenizer.advance();
    }
    
}
