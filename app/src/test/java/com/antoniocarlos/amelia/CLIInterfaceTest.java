package com.antoniocarlos.amelia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
                
import picocli.CommandLine;

class CLIInterfaceTest{
    
    @Test 
    void testCLIInterface() {
        Amelia a = new Amelia();
        CommandLine cli = new CommandLine(a);
        
        cli.execute(new String[]{"./src/test/res/testfile.txt", "-e", "json", "-n", "message_log", "-p", "./src/test/res/"});


        String targetPath = "./src/test/res/testfile.txt";
        boolean validateOnly = false;
        String outputFormat = "json";
        String outputName = "message_log";
        String outputPath = "./src/test/res/";
        assertEquals(a.targetPath,targetPath)                ;
        assertEquals(a.outputFormat,outputFormat);
        assertEquals(a.validateOnly,validateOnly);
        assertEquals(a.outputName,outputName);
        assertEquals(a.outputPath, outputPath);
    }
}
