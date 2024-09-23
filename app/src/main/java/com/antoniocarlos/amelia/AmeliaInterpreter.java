package com.antoniocarlos.amelia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;               

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Class responsible for processing the input string and returning the appropriate content for the client.
 * The appropriate content that cn be returned is solely defined by the delegate instance currently used.
 */
public class AmeliaInterpreter{
    /**
     * Constant for use with the String constructor.
     */
    public static final String JSON = "json";
    /**
     * Constant for use with the String Constructor. Returns a nested collection that mixes Lists and Maps.
     */
    public static final String BASE = "base";
    /**
     * Listener for the Antlr parsing events, holds a delegate that allow custom handling of those events and retrieval of data at custom specified formats.
     */
    AmeliaParseListener listener;
     /**
     * Constructor preferred for outputting custom delegates.
     *
     * @param delegate a string that has as value one of the defaults delegates currently supported.
     * 
     */
    public AmeliaInterpreter(String delegate){
      switch(delegate){
        case BASE:
          this.listener = new AmeliaParseListener(new BaseDelegate());   
        break;
        case JSON:
          this.listener = new AmeliaParseListener(new JSONDelegate());   
        break;
        default:
          throw new IllegalArgumentException("Unknown default delegate of type :" + delegate);   
     }
    }
    /**
     * Constructor preferred for outputting custom delegates.
     *
     * @param delegate the  delegate that will handle the actual processin of the input file contents, through handling of antlr events.
     * 
     */
    public AmeliaInterpreter(Delegate delegate, String outputFile){
        this.listener = new AmeliaParseListener(delegate); 
    }
     /**
     * This method does the actual processing, setting up and invoking antlr classes pipeline to allow processing of the input file.
     *
     * @param rawInput the actual content that will be processed.
     * @param configs the configuration Dictionaruy that allows tells the delegate how to postprocess the output.
     * @return the final form ofthe data produced by the current delegate by processing given input.
     * @throws IndexOutOfBoundsException if any piece of the input attempts to acess an element outside of he bounds of the any list (a parsing exception will be raised, if there's an attempt touse negative indexes).
     * @throws NullPointerException if an abscent name is passed for a nested element in the datastructure.
     * @throws IllegalKeyTypeException if any point the codes atempts to retrieve named keys from lists or numerical keys from a dictionary.
     *
     */
    public <X> X run(String rawInput, Map<String, Object> config){
        CharStream input = CharStreams.fromString(rawInput);
        AmeliaLexer lexer = new AmeliaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AmeliaParser parser = new AmeliaParser(tokens);
        ParseTree tree = parser.root();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);
        return (X) listener.getDelegate().getData(config);
    }
    /**
     * This method does the actual processing, setting up and invoking antlr classes pipeline to allow processing of the input file.
     *
     * @param rawInput the actual content that will be processed.
     * @return the final form ofthe data produced by the current delegate by processing given input.
     * @throws IndexOutOfBoundsException if any piece of the input attempts to acess an element outside of he bounds of the any list (a parsing exception will be raised, if there's an attempt touse negative indexes).
     * @throws NullPointerException if an abscent name is passed for a nested element in the datastructure.
     * @throws IllegalKeyTypeException if any point the codes atempts to retrieve named keys from lists or numerical keys from a dictionary.
     *
     */
    public <X> X run(String rawInput){
        CharStream input = CharStreams.fromString(rawInput);
        AmeliaLexer lexer = new AmeliaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AmeliaParser parser = new AmeliaParser(tokens);
        ParseTree tree = parser.root();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);
        return (X) listener.getDelegate().getData(null);
    }
}
