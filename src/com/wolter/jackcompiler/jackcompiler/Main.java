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
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        /*
        JackTokenizer jack;
        jack = new JackTokenizer("./Exemplos_Jack/Square.jack");
        System.out.println("<tokens>");
        while(jack.hasMoreTokens())
        {
            jack.advance();
            System.out.println(jack.tagToken());
        }
        System.out.println("</tokens>");
        
        */
        
        CompilationEngine comp = new CompilationEngine("./Exemplos_Jack/Square.jack");
        System.out.println(comp.compileClass());
    }
    
}
