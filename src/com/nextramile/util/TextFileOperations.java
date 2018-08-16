package com.nextramile.util;

import java.io.FileWriter;
import java.io.IOException;

public class TextFileOperations {
    public static void writePageUrl(String filePath, String text) {
    	try
    	{
    	    FileWriter fw = new FileWriter(filePath,true);
    	    fw.write(Configs.getConfig("appurl") + text + "\n");
    	    fw.close();
    	} catch(IOException ioe) {
    	    System.err.println("IOException: " + ioe.getMessage());
    	}
    }
}
