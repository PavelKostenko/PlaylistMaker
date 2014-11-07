package com.playlistmaker.utils;

import java.io.File;
import javax.swing.JOptionPane;

public class RmDir {

	public static void rmdir(final File folder) {
		 
	      // Check if the folder file is a real folder
	      if (folder.isDirectory()) {
	          File[] list = folder.listFiles();
	          if (list != null) {
	              for (int i = 0; i < list.length; i++) {
	                  File tmpF = list[i];
	                  if (tmpF.isDirectory()) {
	                      rmdir(tmpF);
	                  }
	                  tmpF.delete();
	              }
	          }
	          if (!folder.delete()) {
	        	  JOptionPane.showMessageDialog(null,
	        			    "A folder cannot be deleted",
	        			    "Warning",
	        			    JOptionPane.WARNING_MESSAGE);
	          }
	          
	      //If this is a file (not a folder)
	      } else {
	    	  if (!folder.delete()) {
	        	  JOptionPane.showMessageDialog(null,
	        			    "A file cannot be deleted",
	        			    "Warning",
	        			    JOptionPane.WARNING_MESSAGE);
	    	  }  
	      }
	 }
}