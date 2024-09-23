package org.example.app;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

/**
 *  Base class of the application responsible for processing the input parameters.
 */
@Command(name = "Amelia", mixinStandardHelpOptions = true, version = "0.1")
class Amelia implements Runnable{
    /**
     * Holds the path to the target file, the only positional argument. 
     */
    @Parameters(
        paramLabel="<filepath>",
        defaultValue="",
        description="The path to the target file")
    String targetPath;
    /**
     * Holds the format of thr output, defining the delegate among the default ones and file extension (if applicable). 
     */
    @Option(
        names={"-e","--file-output-format"},
        description="Specifies the format of the output file (json is default, but xml is also available)"
    )
    String outputFormat;
   /**
    * Holds the condition to make the application return no output, but the input will still be processed and any errors wil be reported. 
    */
    @Option(
        names={"-o","--validate-only"},
        description="Only does the validation, assuring that the file has no errors"+
        " or shows the probable erros, but produce no output "
    )
    boolean validateOnly;
    /**
     * Holds the name of the output file, if applicable. 
     */
    @Option(
        names={"-n","--output-file-name"},
        description="Defines the name of the output file"
    )
    String outputName;
    /**
     * Holds the path of the directory of the output file, if applicable. 
     */
    @Option(
        names={"-p","--output-path"},
        description="Defines the path of the folder where the final file should be stored",
        defaultValue=""
    )
    String outputPath;
    /**
     * Holds the configs to be used too post process the data. 
     */
    @Option(
        names={"-c","--output-configs"},
        description="A Key=Value list of conffigs to be passed to the outut generator")
    Map<String, Object> configs;
    /**
     * The actual body of the application, where the arguments are consumed. 
     *
     * @throws IndexOutOfBoundsException if the input file attempts to acess any list with invalid indexes.
     * @throws IllegalKeyTypeException if any instruction mistreats any collection for the wrong type.
     */
    @Override
    public void run(){
      String content = null; 
      Path filePath = Paths.get(outputName+"."+outputFormat);
      Path path = Paths.get(outputPath).resolve(filePath);
      Path target = Paths.get(targetPath);
      try{
          content = new String(Files.readAllBytes(target));
      }catch(IOException e){
          e.printStackTrace();
      }
      if(outputFormat.equals(AmeliaInterpreter.JSON)){
        AmeliaInterpreter interpreter = new AmeliaInterpreter(outputFormat);
        String result = interpreter.<String>run(content, configs);
        if(outputName == null){
          if(validateOnly){
            System.out.println("No errors found.");
            return;
          }
          System.out.println(result);
        }else{
          try{
            Files.writeString(path, result, StandardCharsets.UTF_8);
          }catch(IOException e){
            e.printStackTrace();
          }
        }         
      }
    }
}