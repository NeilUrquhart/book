package book.ch12.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import book.ch12.mapElites.Solution;
import book.ch12.problem.MDProblem;

/*
 * Nei Urquhart 2021
 * This class writes a Map archive to a directory structure, which is
 * then incorporated in a .ZIP file
 * 
 */
public class ArchiveWriter {
	
	public static void createZip(String fileName, HashMap<String,Solution> archive){
		
		try{
			new File(fileName).mkdir();
			for (String key : archive.keySet()){
				Solution me = archive.get(key);
				key = key.replace(':', '_');
				new File(fileName+"/"+key).mkdir();
				File dir = new File(fileName+"/"+key);
				//Change working directory
				MDProblem.getInstance().setWorkingDir(dir.getAbsolutePath());
				MDProblem.getInstance().evaluate(me.getMyChromo(),false,key);
			}
			System.out.println("Writing ZIP");
			
			//Now copy directory structure to the zip

			String zipFile = fileName+".zip";
			String srcDir = fileName;
			try {
				FileOutputStream fos = new FileOutputStream(zipFile);
				ZipOutputStream zos = new ZipOutputStream(fos);		
				File srcFile = new File(srcDir);
				ArchiveWriter.addDirToArchive(zos, srcFile,"");
				// close the ZipOutputStream
				zos.close();
			}
			catch (IOException ioe) {
				System.out.println("Error creating zip file: " + ioe);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void addDirToArchive(ZipOutputStream zos, File srcFile, String dir) {
		File[] files = srcFile.listFiles();
		System.out.println("Adding directory: " + srcFile.getName());
		for (int i = 0; i < files.length; i++) {
			// if the file is directory, use recursion
			if (files[i].isDirectory()) {
				addDirToArchive(zos, files[i], files[i].getName());
				continue;
			}
			try {
				// create byte buffer
				byte[] buffer = new byte[1024];
				FileInputStream fis = new FileInputStream(files[i]);
				zos.putNextEntry(new ZipEntry(dir +"/"+files[i].getName()));
				int length;
				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}
				zos.closeEntry();
				// close the InputStream
				fis.close();
			} catch (IOException ioe) {
				System.out.println("IOException :" + ioe);
			}
		}
	}
}
