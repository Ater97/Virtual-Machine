/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
    String FunctionName ="";
    
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
        //createASM(originalFile.getName(),originalFile.getPath());
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
            case "function":// f n Here starts the code of a function named f that has n local variables
                FunctionName = parts[1];
                String result = "(" + FunctionName + ")\n"
                        + "@SP\n"
                        + "A=M\n";
                for (int i = 0; i < Integer.parseInt(parts[2]) ; i++) {
                    result += "M=0\n"
                            + "A=A+1\n";
                }
                return result + "D=A\n"
                        + "@SP\n"
                        + "M=D\n";
                
            case "call"://f m Call function f, stating that m arguments have already been pushed onto the stack by the caller
                operationLabel++;
                return // SP -> R13
                        "@SP\n"
                        + "D=M\n" 
                        + "@R13\n" 
                        + "M=D\n" 
                        // @RET -> *SP
                        + "@RET." + operationLabel + "\n" 
                        + "D=A\n" 
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 

                        + "@SP\n" 
                        + "M=M+1\n" 
                        // LCL to *SP
                        + "@LCL\n" 
                        + "D=M\n" 
                        + "@SP\n"
                        + "A=M\n" 
                        + "M=D\n" 

                        + "@SP\n" 
                        + "M=M+1\n" 
                        // ARG to *SP
                        + "@ARG\n" 
                        + "D=M\n" 
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 

                        + "@SP\n" 
                        + "M=M+1\n" 
                        // THIS to *SP
                        + "@THIS\n" 
                        + "D=M\n" 
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 

                        + "@SP\n" 
                        + "M=M+1\n" 
                        // THAT to *SP
                        + "@THAT\n" 
                        + "D=M\n" 
                        + "@SP\n" 
                        + "A=M\n" 
                        + "M=D\n" 

                        + "@SP\n" 
                        + "M=M+1\n" 

                        + "@R13\n" 
                        + "D=M\n" 
                        + "@" + parts[2] + "\n" 
                        + "D=D-A\n" 
                        + "@ARG\n" 
                        + "M=D\n" 
                        // SP to LCL
                        + "@SP\n" 
                        + "D=M\n" 
                        + "@LCL\n" 
                        + "M=D\n" 
                        + "@" + parts[1] + "\n" 
                        + "0;JMP\n" 
                        + "(RET." + operationLabel + ")\n";
                
            case "return": // Return to the calling function.
                return // *(LCL - 5) to R13
                        "@LCL\n" 
                        + "D=M\n" 
                        + "@5\n" 
                        + "A=D-A\n" 
                        + "D=M\n" 
                        + "@R13\n" 
                        + "M=D\n" 
                        // *(SP - 1) to *ARG
                        + "@SP\n" 
                        + "A=M-1\n" 
                        + "D=M\n" 
                        + "@ARG\n" 
                        + "A=M\n" 
                        + "M=D \n" 
                        // ARG + 1 to SP
                        + "D=A+1\n" 
                        + "@SP\n" 
                        + "M=D\n" 
                        // *(LCL - 1) to THAT
                        + "@LCL\n" 
                        + "AM=M-1\n" 
                        + "D=M\n" 
                        + "@THAT\n" 
                        + "M=D\n" 
                        // *(LCL - 1) to THIS
                        + "@LCL\n" 
                        + "AM=M-1\n" 
                        + "D=M\n" 
                        + "@THIS\n" 
                        + "M=D\n" 
                        // *(LCL - 1) to ARG
                        + "@LCL\n" 
                        + "AM=M-1\n" 
                        + "D=M\n" 
                        + "@ARG\n" 
                        + "M=D\n" 
                        // *(LCL - 1) to LCL
                        + "@LCL\n" 
                        + "A=M-1\n" 
                        + "D=M\n" 
                        + "@LCL\n" 
                        + "M=D\n" 
                        // R13 to A
                        + "@R13\n" 
                        + "A=M\n" 
                        + "0;JMP\n";
                
            //Additionals commands -> label, goto, if-goto
            case "label": // labels the current location in the function’s code. 
                return "(" + FunctionName + "$"  + parts[1] + ")\n";
                
            case "goto"://effects an unconditional goto operation, causing execution to continue from
                //the location marked by the label. The jump destination must be located in the same function. 
                return "@" + FunctionName + "$" +  parts[1] + "\n" +
                        "0;JMP\n";
                
            case "if-goto"://The stack’s topmost value is popped; if the value is not zero, execution continues
                //from the location marked by the label; otherwise, execution continues from the next command in 
                //the program. The jump destination must be located in the same function.
                return "@SP\n"
                        + "AM=M-1\n" 
                        + "D=M\n" 
                        + "@" + FunctionName + "$" + parts[1] + "\n" 
                        + "D;JNE\n";
            default:
                 return "";
        }
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
                        + "@R13\n"
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
        MainList = new ArrayList<>();
    }
    public void appendASM(String filename, String path)
    {
        try(FileWriter fw = new FileWriter(path + filename + ".asm", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw))
        {
            for (int i = 0; i < MainList.size(); i++) {
                out.println(MainList.get(i));
                out.println("\n");
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
    
    public void MergeFiles(List<File> fileList) throws IOException
    {
        String folderName ="";
        String folderPath ="";
        String filePath ="";
        boolean first = true;
        
        if(fileList.size()<2)
        {
            Read(fileList.get(0));
            createASM(fileList.get(0).getName(),fileList.get(0).getPath());
            System.out.println("one file");
        }
        else
        {
            for (File file : fileList){
                if(file.getParentFile().getName().equals(folderName))//same folder
                {
                    Read(file);
                    filePath = file.getPath();
                    filePath = filePath.replace(file.getName(), "");
                    appendASM(file.getParentFile().getName(),filePath);  
                    
                    System.out.println("same folder ");
                    System.out.println("file: " + file.getName());
                    System.out.println(file.getParentFile().getName());
                    System.out.println("file: " + folderName + "\n");
                }
                else
                {   
                    Read(file);
                    filePath = file.getPath();
                    filePath = filePath.replace("\\"+file.getName(), "");
                    createASM(file.getParentFile().getName(),filePath);
                }
                
                folderName = file.getParentFile().getName();
                folderPath = file.getPath();
            }
        }
    }
}
