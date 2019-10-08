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
        
        switch(args.length)
        {
            case 0:
                System.out.println("Compilando");
                break;
            case 1:
                CompilationEngine comp = new CompilationEngine(args[0]);
                System.out.println(comp.compileClass());
                break;
            default:
                System.out.println("Erro");
                break;
        }
    }
    
}
