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
    int operationLabel = 0;
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
                return "@SP\n"
                        + "AM=M-1\n" 
                        + "D=M\n" 
                        + "A=A-1\n" 
                        + "M=D+M\n";
            case "sub":
                return "@SP\n"
                        + "AM=M-1\n" 
                        + "D=M\n" 
                        + "A=A-1\n"
                        + "M=M-D\n";
            case "neg":
                return "@SP\n"
                        + "AM=M-1\n" 
                        + "M=-M\n";
            case "eq":
                operationLabel++;
                return "@SP\n"
                        + "AM=M-1\n"
                        + "D=M\n"
                        + "A=A-1\n"
                        + "D=M-D\n"
                        
                        + "@true"+ operationLabel +"\n"
                        + "D;JEQ\n"
                        //false
                        + "@SP\n"
                        + "A=M-1\n"
                        + "M=0\n"
                        + "@finish"+ operationLabel +"\n"
                        + "0;JMP\n"
                        //true
                        + "(true"+ operationLabel +")\n"
                        + "@SP\n"
                        + "A=M-1\n"
                        + "M=-1\n"
                        + "(finish"+ operationLabel +")\n";
                
            case "gt":
                return "@SP\n"
                        + "AM=M-1\n"
                        + "D=M\n"
                        + "A=A-1\n"
                        + "D=M-D\n"
                        + "@true"+ operationLabel +"\n"
                        + "D;JGT\n"
                        + "@SP\n"
                        + "A=M-1\n"
                        + "M=0\n"
                        + "@finish"+ operationLabel +"\n"
                        + "0;JMP\n"
                        + "(true"+ operationLabel +")\n"
                        + "@SP\n"
                        + "A=M-1\n"
                        + "M=-1\n"
                        + "(finish"+ operationLabel +")\n";
            case "lt":
                return "@SP\n"
                        + "AM=M-1\n"
                        + "D=M\n"
                        + "A=A-1\n"
                        + "D=M-D\n"
                        + "@true"+ operationLabel +"\n"
                        + "D;JLT\n"
                        + "@SP\n"
                        + "A=M-1\n"
                        + "M=0\n"
                        + "@finish"+ operationLabel +"\n"
                        + "0;JMP\n"
                        + "(true"+ operationLabel +")\n"
                        + "@SP\n"
                        + "A=M-1\n"
                        + "M=-1\n"
                        + "(finish"+ operationLabel +")\n";
            case "and":
                return "@SP\n"
                        + "AM=M-1\n" 
                        + "D=M\n" 
                        + "A=A-1\n"
                        + "M=D&M\n";
            case "or":
                return "@SP\n"
                        + "AM=M-1\n" 
                        + "D=M\n" 
                        + "A=A-1\n"
                        + "M=D|M\n";
            case "not":
                return "@SP\n"
                        + "AM=M-1\n" 
                        + "M=!M";
            default: 
                return AccesCommands(originalLine);
        }
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
                return "@" + parts[2].trim()
                        + "\nD=A\n"
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 
                        + "@SP\n" 
                        + "M=M+1\n";

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
