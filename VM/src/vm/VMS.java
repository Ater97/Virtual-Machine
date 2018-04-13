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
    int operationLabel = 0;
    String tp ="";
    public void Read(File originalFile) throws IOException
    {
        try (Scanner scanner = new Scanner(originalFile)) {
            tp = originalFile.getName().replaceAll(".vm", "");
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
                        + "A=M-1\n" 
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
                operationLabel++;
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
                operationLabel++;
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
                        + "A=M-1\n" 
                        + "M=!M\n";
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
            //Program flow commands -> function, call, return
            case "function":
                break;
            case "call":
                break;
            //Additionals commands -> label, goto, if-goto
            case "label":
                break;
            case "goto":
                break;
            case "if-goto":
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
            case "argument": //Push the value of segment[index] onto the stack.
                return "@ARG\n"
                        + "D=M\n"
                        + "@" + parts[2].trim() +"\n"
                        + "A=D+A\n"
                        + "D=M\n"
                        
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 
                        + "@SP\n" 
                        + "M=M+1\n"; 
            case "local":
                return "@LCL\n"
                        + "D=M\n"
                        + "@" + parts[2].trim() + "\n"
                        + "A=D+A\n"
                        + "D=M\n"
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 
                        + "@SP\n" 
                        + "M=M+1\n";
            case "static":
                return "@" + tp +"." + parts[2].trim()+"\n"
                        + "D=M\n"
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 
                        + "@SP\n" 
                        + "M=M+1\n";
            case "constant":
                return "@" + parts[2].trim() + "\n"
                        + "D=A\n"
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 
                        + "@SP\n" 
                        + "M=M+1\n";

            case "this":
                return "@THIS\n"
                        + "D=M\n"
                        + "@" + parts[2].trim() +"\n"
                        + "A=D+A\n"
                        + "D=M\n"
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 
                        + "@SP\n" 
                        + "M=M+1\n";
            case "that":
                return "@THAT\n"
                        + "D=M\n"
                        + "@" + parts[2].trim()  +"\n"
                        + "A=D+A\n"
                        + "D=M\n"
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 
                        + "@SP\n" 
                        + "M=M+1\n";
            case "pointer":
                if(parts[2].trim().equals("0"))
                {
                    return "@THIS\n"
                            + "D=M\n"
                            + "@SP\n" 
                            + "A=M\n" 
                            + "M=D\n" 
                            + "@SP\n" 
                            + "M=M+1\n";
                }
                return "@THAT\n"
                        + "D=M\n"
                        
                        + "@SP\n"
                        + "A=M\n"
                        + "M=D\n"
                        + "@SP\n"
                        + "M=M+1\n";
            case "temp":
                return "@R5\n"
                        + "D=A\n"
                        + "@" + parts[2].trim() +"\n"
                        + "A=D+A\n"
                        + "D=M\n"
                        
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 
                        + "@SP\n" 
                        + "M=M+1\n";
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
                return "@ARG\n"
                        + "D=M\n" 
                        + "@" + parts[2].trim()+"\n"
                        + "D=D+A\n" 
                        + "@R13\n"
                        + "M=D\n"
                        + "@SP\n"
                        + "AM=M-1\n"
                        + "D=M\n"
                        + "@13\n"
                        + "A=M\n"
                        + "M=D\n";
                        
            case "local"://pop segment index Pop the top stack value 
                return "@LCL\n"
                        + "D=M\n" 
                        + "@" + parts[2].trim()+"\n"
                        + "D=D+A\n"
                        //and store it in segment[index].
                        + "@R13\n"
                        + "M=D\n"
                        + "@SP\n"
                        + "AM=M-1\n"
                        + "D=M\n"
                        + "@13\n"
                        + "A=M\n"
                        + "M=D\n";
            case "static":
                return  "@" + tp +"."+ parts[2].trim()+"\n"
                        + "@R13\n"
                        + "M=D\n"
                        + "@SP\n"
                        + "AM=M-1\n"
                        + "D=M\n"
                        + "@13\n"
                        + "A=M\n"
                        + "M=D\n";
            case "constant":
                break;
            case "this":
                return "@THIS\n"
                        + "D=M\n" 
                        + "@" + parts[2].trim()+"\n"
                        + "D=D+A\n" 
                        + "@R13\n"
                        + "M=D\n"
                        + "@SP\n"
                        + "AM=M-1\n"
                        + "D=M\n"
                        + "@13\n"
                        + "A=M\n"
                        + "M=D\n";
                        
            case "that":
                 return "@THAT\n"
                        + "D=M\n" 
                        + "@" + parts[2].trim()+"\n"
                        + "D=D+A\n" 
                        + "@R13\n"
                        + "M=D\n"
                        + "@SP\n"
                        + "AM=M-1\n"
                        + "D=M\n"
                        + "@13\n"
                        + "A=M\n"
                        + "M=D\n";
                        
            case "pointer":
                if(parts[2].trim().equals("0"))
                {
                    return "@THIS\n"
                            + "D=A\n"
                            
                            + "@R13\n"
                            + "M=D\n"
                            + "@SP\n"
                            + "AM=M-1\n"
                            + "D=M\n"
                            + "@13\n"
                            + "A=M\n"
                            + "M=D\n";
                }
                return "@THAT\n"
                        + "D=A\n"

                        + "@R13\n"
                        + "M=D\n"
                        + "@SP\n"
                        + "AM=M-1\n"
                        + "D=M\n"
                        + "@13\n"
                        + "A=M\n"
                        + "M=D\n";
            case "temp":
                return "@R5\n"
                        + "D=A\n"
                        + "@" + parts[2].trim()+"\n"
                        + "D=D+A\n"
                        + "@R13\n"
                        + "M=D\n"
                        + "@SP\n"
                        + "AM=M-1\n"
                        + "D=M\n"
                        + "@13\n"
                        + "A=M\n"
                        + "M=D\n";
                        
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
                MainList.add(Translator(parts[0]));}
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
