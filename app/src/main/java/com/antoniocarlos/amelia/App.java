package com.antoniocarlos.amelia;

import picocli.CommandLine;


/**
 * Main class responsible for passing the command line args to picocli.
 */
public class App{
    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new Amelia());
        cmd.execute(args);
    }
}
