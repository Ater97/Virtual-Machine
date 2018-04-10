/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author sebas
 */
public class VMS {
    Utilities U = new Utilities();
    ArrayList<String> MainList = new ArrayList<>();
    Stack<String> MainStack = new Stack<>();
    
    public void Read(File originalFile) throws IOException
    {
        try (Scanner scanner = new Scanner(originalFile)) {
            String str = null;
            while (scanner.hasNext())
            {
                str = scanner.nextLine();
                if ( !"".equals(str))
                    MainList.add(str);
            }
        }
        firstscan(MainList);//delete comments & blank space
        //SecondScan(MainList);
        createASM(originalFile.getName(),originalFile.getPath());
    }
    
    public void SecondScan(ArrayList<String> original)
    {   MainList = new ArrayList<>();
        for (int i = 0; i < original.size(); i++) {
            MainList.add(Translator(original.get(i)));
            
        }
    }
    
    public String Translator(String originalLine)
    {   //Memory Access Commands -> add, sub, neg,eq, gt, lt, and, or , not 
        switch(originalLine) 
        {
            case "add":
                break;
            case "sub":
                break;
            case "neg":
                break;
            case "eq":
                break;
            case "gt":
                break;
            case "lt":
                break;
            case "and":
                break;
            case "or":
                break;
            case "not":
                break;
            default: 
                return AccesCommands(originalLine);
        }
        
        return " ";
    }
    
    public String AccesCommands(String originalLine)
    {
        String[] parts = originalLine.split(" ");
        switch(parts[0])//Memory Acces commands-> push, pop
        {   
            case "push":
                return PushMemorySegments(originalLine);
            case "pop":
                return PopMemorySegments(originalLine);
            //Additionals commands -> label, goto, if-goto
            case "label":
                break;
            case "goto":
                break;
            case "if-goto":
                break;
            //Program flow commands -> function, call, return
            case "function":
                break;
            case "call":
                break;
            case "return":
                break;
        }
        return "";
    }
    public String PushMemorySegments(String originalLine)
    {   String[] parts = originalLine.split(" ");
        switch(parts[1])//Memory Segments -> argument, local, static, constant, this-that, pointer, temp  
        {
            case "argument":
                break;
            case "local":
                break;
            case "static":
                break;
            case "constant":
                break;
            case "this":
                break;
            case "that":
                break;
            case "pointer":
                break;
            case "temp":
                break;
            default:
                break;
        }
        return "";
    }
    public String PopMemorySegments(String originalLine)
    {   String[] parts = originalLine.split(" ");
        switch(parts[1])//Memory Segments -> argument, local, static, constant, this-that, pointer, temp  
        {
            case "argument":
                break;
            case "local":
                break;
            case "static":
                break;
            case "constant":
                break;
            case "this":
                break;
            case "that":
                break;
            case "pointer":
                break;
            case "temp":
                break;
            default:
                break;         
        }
        return "";
    }
    
    public void firstscan(ArrayList<String> original){ //delete comments & blank space
        MainList = new ArrayList<>();
        for (int i = 0; i < original.size(); i++) {
            if(original.get(i).contains("//")) {
                String[] parts = original.get(i).split("/");
                if ( !"".equals(parts[0]))
                MainList.add(parts[0]);}
            else{
                MainList.add(Translator(original.get(i)));}
        }  
    }
    
    public void createASM(String filename, String path) throws IOException
    {
        path = path.replace(filename, "");
        filename = filename.replace(".vm", "");  
        
        File fout = new File(path,filename+".asm");
	FileOutputStream fos = new FileOutputStream(fout);
 
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            for (int i = 0; i < MainList.size(); i++) {
                bw.write(MainList.get(i));
                bw.newLine();
            }
        }
    }
}
