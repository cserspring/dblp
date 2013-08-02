package edu.whu.wang.test;

import java.io.File;
import java.util.Date;

import edu.whu.wang.index.*;
import edu.whu.wang.search.*;
import edu.whu.wang.setup.DBLP;
import edu.whu.wang.setup_new.Experiment;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//解析XML
//		File file = new File("dbEnv_graph");
//		if (!file.exists())
//			file.mkdirs();
//		if (file.isDirectory()) {
//			File[] files = file.listFiles();
//			for (File f : files) {
//				f.delete();
//			}
//		}
//		DBLP dblp = new DBLP("dbEnv_graph");
//		dblp.init();
//		Date stime = new Date();
//		dblp.X2G("files/dblp_5000029.xml");
//		dblp.procRef_Cite();
//
//		Date etime = new Date();
//		dblp.print();
//		dblp.close();
//		dblp.statistic();
//		System.out.println("Time exhausted: "
//				+ (etime.getTime() - stime.getTime()) + " ms");
//
//		// 生成keyword -> vertex 索引
//		InvertedList il = new InvertedList();
//		il.parseVertex("graph/vertex.txt");

		Date date1 = new Date();
		SimpleQueryProcessingManager sqpm = SimpleQueryProcessingManager
				.getInstance();
		sqpm.init("graph/edge.txt", 198462);
		sqpm.searchFromIndex("xml java", 100);
		
		Date date2 = new Date();
		System.out.println(date2.getTime() - date1.getTime());
	}
}
