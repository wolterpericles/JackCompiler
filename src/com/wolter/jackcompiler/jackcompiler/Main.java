/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wolter.jackcompiler.jackcompiler;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

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
        File arquivo = new File("./");
        File arquivos[] = arquivo.listFiles();
        
        switch(args.length)
        {
            case 0:
                for(File arquivoPasta : arquivos){
                    if(arquivoPasta.getName().endsWith(".jack"))
                    {
                        CompilationEngine comp = new CompilationEngine(arquivoPasta.getName());
                        System.out.println("Compilando arquivo: "+arquivoPasta.getName());
                        gerarXML(comp.compileClass(), arquivoPasta.getAbsolutePath());
                    }
                }
                break;
            case 1:
                File arquivoPasta = new File(args[0]);
                if(arquivoPasta.getName().endsWith(".jack"))
                {
                    CompilationEngine comp = new CompilationEngine(arquivoPasta.getName());
                    System.out.println("Compilando arquivo: "+arquivoPasta.getName());
                    gerarXML(comp.compileClass(), arquivoPasta.getAbsolutePath());
                }
                else
                {
                    System.out.println("Arquivo com extensão incorreta.");
                }
                break;
            default:
                System.out.println("Argumento incorreto.");
                break;
        }
    }
    
    public static void gerarXML(String xml, String nomeArquivo)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        String nome = nomeArquivo.substring(0, nomeArquivo.lastIndexOf('.'));
        
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File("/"+nome+".xml"));
            transformer.transform(domSource, streamResult);
        } catch(Exception e)
        {
            e.printStackTrace();
        }  
    }
    
}
