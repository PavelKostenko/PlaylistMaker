package com.playlistmaker.gui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import com.playlistmaker.utils.*;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/*WHAT NEEDS TO BE DONE:
 * - DONE. checking whether directory with today's playlist already exist.
 * - DONE. adding .mp3 files from different directories.
 * - DONE. removing .mp3 file from the list
 * - DONE. compose good comments
 * - DONE. parameterize JList
 * - DONE. show coping report
 * - DONE. add utils package where to gather all util classes (methods here)
 * - DONE. make report window appear at the end o the coping
 * - DONE. correct a bug when pressing "Cancel" when folder already exists do the same as "Add"
 * - DONE. create technical documentation with functionality description
 * - DONE. test the program
 * - DONE. create distrubutable .jar
 * - DONE. move a program to GitHub
 * */

public class MainWindow implements ActionListener {
	
	private JFrame mainFrame;
	private JPanel windowContent;
	private JScrollPane filesPanel;
	private JPanel buttonsUpperPanel;
	private JPanel buttonsLowerPanel;
	private JButton addFilesButton;
	private JButton removeFilesButton;
	private JButton createPlaylistButton;
	private JList <File> displayList;
	private final String PLAYLIST_PARENT_DIRECTORY = "d:\\Playlists\\";
	private DefaultListModel <File> model;
	private File playlist;
	
	MainWindow(){
		
		addFilesButton = new JButton("Add mp3 files");
		addFilesButton.setPreferredSize(new Dimension(200,25));
		
		removeFilesButton = new JButton("Remove selected files");
		removeFilesButton.setPreferredSize(new Dimension(200,25));
		
		
		createPlaylistButton = new JButton("Create a folder for listed above files and create a playlist in it");
		createPlaylistButton.setPreferredSize(new Dimension(400,25));
		
		addFilesButton.addActionListener(this);
		removeFilesButton.addActionListener(this);
		createPlaylistButton.addActionListener(this);
		
		buttonsUpperPanel = new JPanel(); //I use a panel so in the future I can add more buttons
		GridLayout gL1 = new GridLayout(1,2); 
		buttonsUpperPanel.setLayout(gL1);
		buttonsUpperPanel.add(addFilesButton);
		buttonsUpperPanel.add(removeFilesButton);
		
		filesPanel = prepareFilesPanel();
		
		buttonsLowerPanel = new JPanel();  //I use a panel so in the future I can add more buttons
		GridLayout gL2 = new GridLayout (1,1);
		buttonsLowerPanel.setLayout(gL2);
		buttonsLowerPanel.add(createPlaylistButton);
		
		windowContent = new JPanel();
		BorderLayout bL = new BorderLayout();
		windowContent.setLayout(bL);
		windowContent.add(buttonsUpperPanel,BorderLayout.NORTH);
		windowContent.add(filesPanel,BorderLayout.CENTER);
		windowContent.add(buttonsLowerPanel,BorderLayout.SOUTH);
		
		mainFrame = new JFrame("Playlist creator");
		mainFrame.setContentPane(windowContent);
		mainFrame.setPreferredSize(new Dimension(800,400));
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	private JScrollPane prepareFilesPanel () {
		
    	model = new DefaultListModel<File>();
		displayList = new JList<File>(model);
        
        displayList.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        displayList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        
        //Wrapping is determined by the width of the list;
        displayList.setVisibleRowCount(-1);
        displayList.setName("displayList");
        
        //To have a nicer look of added music files in the files panel
        displayList.setCellRenderer(new MyCellRenderer());
        
        return new JScrollPane(displayList);
	}
	
	/*private static class MyCellRenderer extends DefaultListCellRenderer  {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof File) {
                File file = (File) value;
                setText(file.getName());
                setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
                if (isSelected) {
                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                } else {
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }
                setEnabled(list.isEnabled());
                setFont(list.getFont());
                setOpaque(true);
            }
            return this;
        }
    }*/
	
	public void actionPerformed (ActionEvent e){
		JButton buttonClicked = (JButton)e.getSource();
		String buttonText = buttonClicked.getText();
		
		if (buttonText.equals("Add mp3 files")){
			
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("d:\\Pavel\\Media\\Music\\"));
		    chooser.setDialogTitle("Select music files");
		    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		    chooser.setMultiSelectionEnabled(true);
		    chooser.setAcceptAllFileFilterUsed(false);
		    chooser.addChoosableFileFilter(new FileFilter() {
		    	 
		        //Show all directories and .mp3 files.
		        public boolean accept(File f) {
		            if (f.isDirectory()) {
		                return true;
		            }
		            String fileNameE = f.getName();
		            
		            if (fileNameE.toLowerCase().endsWith(".mp3")){
		            	return true;
		            }		     
		            return false;
		        }
		        
		        //The description of this filter
		        public String getDescription() {
		            return "mp3 files";
		        }
		    	});
		    
			int returnVal = chooser.showOpenDialog(mainFrame);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	
		    	//Getting selected files and adding them to the display list
		    	File [] selectedMusicFiles = chooser.getSelectedFiles();
		    	for (File f:selectedMusicFiles){
		    		if (!model.contains(f)){
		    			model.addElement(f);	
		    		} else {
		    			System.out.println("The .mp3 file " + f.getName() + " already in the list");
		    		}
		    		
		    	}
		    }
		}
		
		if (buttonText.equals("Remove selected files")){
			int [] indicesToRemove = displayList.getSelectedIndices();
			for(int i = indicesToRemove.length;i>0;i--){
				model.removeElementAt(indicesToRemove[i-1]);
			}
		}
		
		if (buttonText.equals("Create a folder for listed above files and create a playlist in it")){
			if (model.getSize()>0){
				
				//I want to include current date in the name of the playlist folder and
				//playlist itself. For this I need to get current date, month and year using getDateAddition
				//method. A folder where today's playlist will be created will have this path:
				String playListDirectoryPath = PLAYLIST_PARENT_DIRECTORY +"Playlist_" + DateAddition.getDateAddition() + "\\";
				playlist = new File(playListDirectoryPath + "Playlist_" + DateAddition.getDateAddition() + ".m3u");
				
				//We need to create directory for today's playlist if it does not exists already;
				if (!(playlist.getParentFile().exists() && playlist.getParentFile().isDirectory())){
					playlist.getParentFile().mkdirs();
					this.copyMusicFiles();
					
				//If you already used a program today and playlist's directory exist
				//then a dialog appears asking whether you want to replace or add new files
				//there;
				} else {
					Object[] options = {"Replace","Add selected","Cancel"};
					int n = JOptionPane.showOptionDialog(mainFrame,
							"Directory " + '"' + playlist.getParentFile().getName() + '"' +  " already exists. \n" +
							"Do you want to REPLACE existing folder or ADD new files to it?",
							"Folder already exists", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
					
					//If a user want to remove old folder then remove it and create new before
					//copying files
					if (n == JOptionPane.YES_OPTION) {
						File [] playlistDirContent = playlist.getParentFile().listFiles();
						for (File f:playlistDirContent){
							RmDir.rmdir(f);
						}
						this.copyMusicFiles();
						
					//If a user want to add selected files to the old folder then just run
					//copyMusicFiles method as it knows how to add new .mpe and
					//re-compose the playlist
					}
					
					if (n == JOptionPane.NO_OPTION) {
						this.copyMusicFiles();
					}
				}
			}
		}
	}
	
	private void copyMusicFiles (){
		//Preparing a reporting window which will be shown after coping process
		JDialog copyProgressDialog = new JDialog(mainFrame,"Files are being copied",false);
		DefaultListModel<String> copyProgressDialogModel = new DefaultListModel<String>();
		copyProgressDialogModel.addElement("COPING REPORT:");
		JList <String> copyProgressDialogJList = new JList<String>(copyProgressDialogModel);
		JScrollPane copyPane = new JScrollPane(copyProgressDialogJList);
		copyProgressDialog.getContentPane().add(copyPane);
		copyProgressDialog.setSize(500,400);
		
		for (int i=0;i<model.getSize();i++){
			File f = model.getElementAt(i);
			Path source = f.toPath();
			String fileName = f.getName();
			File targetFile = new File (playlist.getParentFile()+ "\\" + fileName);
			
			if (!targetFile.exists()){
				Path target = targetFile.toPath();
				
				try {
					Files.copy(source,target);
					
					//I tried to make refreshing of progress window in real time however
					//it is not possible due to starting GUI through "invokeLater"
					//copyProgressDialogJList.revalidate();
					//copyProgressDialogJList.repaint();
					copyProgressDialogModel.addElement("File " + fileName + " was successfuly copied");
					System.out.println("File " + fileName + " was successfuly copied");
					
				} catch (IOException e1) {
					e1.printStackTrace();
					
					//Even if something goes wrong we need to have an updated playlist
					//consisting of the files which are already in the playlist's folder:
					composePlaylist (playlist);
					
					copyProgressDialogModel.addElement("An error occured when coping " + fileName);
					System.out.println("An error occured when coping " + fileName);
				}
				
			} else {					
				System.out.println("File " + fileName + " already exists");
				copyProgressDialogModel.addElement("File " + fileName + " already exists");
			}
		}
		composePlaylist (playlist);
		
		//Show report only after all files are copied.
		copyProgressDialog.setVisible(true);
		
	}
	
	private void composePlaylist (File playlistFile){
		
		FileOutputStream fOS = null;
		OutputStreamWriter oSW = null;
		BufferedWriter writer = null;
		
		try {
		
			//Creating streams for composing an .m3u file
			fOS = new FileOutputStream(playlistFile);
			oSW = new OutputStreamWriter(fOS, "windows-1252");
			writer = new BufferedWriter(oSW);
			
			//We will include only .mp3 files in the playlist
			File [] listOfFiles = playlistFile.getParentFile().listFiles((new FilenameFilter() {
					    public boolean accept(File dir, String name) {
					        return name.toLowerCase().endsWith(".mp3");
					    }
					}));
			
			for (File f:listOfFiles){
							
				String fileName = f.getName();
							
				//Inserting an entry to .m3u file containing a name of the current file
				writer.write(fileName);
				writer.newLine();
			}
				
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			try {
				if (fOS!=null){
					writer.flush();
					writer.close();
					oSW.close();
					fOS.close();
				}
			} catch (IOException e3){
				e3.printStackTrace();
			}
		}
	}
	
	/*private String getDateAddition(){
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		
		String dayOfMonthStr = String.valueOf(dayOfMonth) + "_";
		String monthStr = String.valueOf(month) + "_";
		String yearStr = String.valueOf(year);
		
		return dayOfMonthStr + monthStr + yearStr;
	}*/
	
	/*public void rmdir(final File folder) {
		 
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
	        	  JOptionPane.showMessageDialog(mainFrame,
	        			    "A folder cannot be deleted",
	        			    "Warning",
	        			    JOptionPane.WARNING_MESSAGE);
	          }
	      }
	  }*/
	
	public static void main(String[] args) {
		
		//Avoiding race conditions in UI
		EventQueue.invokeLater(new Runnable() {

	            @Override
	            public void run() {
	            	new MainWindow();
	            }
	    });
	}
}