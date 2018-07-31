package com.nextramile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileOperations {

	public static void copyFile(String sourcePath, String destinationPath) {
		File destination = new File(destinationPath);
		File source = new File(sourcePath);
		if(!destination.exists()) {
			if(source.exists()) {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = new FileInputStream(source);
					out = new FileOutputStream(destination);
					byte[] buf = new byte[1024];
					int bytesRead;
					while((bytesRead = in.read(buf)) > 0) {
						out.write(buf, 0, bytesRead);
					}
				} catch(IOException e) {} finally {
					try { in.close();} catch (IOException e) {}
					try { out.close();} catch (IOException e) {}
				}
			}
		}
	}
}
