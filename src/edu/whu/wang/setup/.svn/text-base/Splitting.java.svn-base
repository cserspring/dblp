package edu.whu.wang.setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Splitting {

	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("files/dblp_new.xml")));
			FileWriter fw = new FileWriter("dblp-splitted-files/dblp_1.xml");
			String line = null;
			int i = 0;
			int j = 0;
			while (br.ready()) {
				line = br.readLine();
				fw.write(line + "\n");
				i++;
				j++;
				if (i >= 90000) {
					if (line.startsWith("</")) {
						if (!line.equals("</dblp>")) {
							fw.write("</dblp>");
						}
						fw.close();
						fw = new FileWriter("dblp-splitted-files/dblp_" + (j + 1) + ".xml");
						fw.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
						fw.write("<!DOCTYPE dblp SYSTEM \"dblp.dtd\">\n");
						fw.write("<dblp>\n");
						i = 0;
					}
				}
			}
			fw.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
