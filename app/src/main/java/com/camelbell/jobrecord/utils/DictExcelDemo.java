package com.camelbell.jobrecord.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import android.database.SQLException;

public class DictExcelDemo {
	 public static void main(String[] args) {
	  DictExcelDemo ded = new DictExcelDemo();
	  Connection conn = ded.getConnection();
	  ded.readExcel_(conn);
	 }

	 private Connection getConnection(){
	  Connection conn = null;
	  try {
	   Class.forName("org.sqlite.JDBC");
	   conn = DriverManager.getConnection("jdbc:sqlite:database.db");
	   Statement stat = conn.createStatement();
	   stat.executeUpdate("create  table if not exists dictionary(enword varchar(200), cnword varchar(200));");// 创建一个表，两列

	  } catch (ClassNotFoundException e) {
	   e.printStackTrace();
	  } catch (SQLException e) {
	   e.printStackTrace();
	  } catch (java.sql.SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return conn;
	 }

	 private void readExcel_(Connection conn) {
	  try {
	   Workbook book = Workbook.getWorkbook(new File("English.xls"));
	   PreparedStatement prep = conn.prepareStatement("insert into dictionary(enword,cnword) values(?,?);");

	   for (int a = 0; a < 26; a++) {
	    // 获得第一个工作表对象
	    Sheet sheet = book.getSheet(a);
	    // 得到第一列第一行的单元格
	    // 得到第一列第一行的单元格
	    int columnum = sheet.getColumns();// 得到列数
	    int rownum = sheet.getRows();// 得到行数
	    for (int i = 1; i < rownum; i++)// 循环进行读写
	    {// 行
	     String key = "";
	     String value = "";
	     for (int j = 0; j < columnum; j++) {// 列
	      Cell cell1 = sheet.getCell(j, i);
	      String result = cell1.getContents();
	      if (j == 0) {
	       key += result;
	      } else {
	       value += result;
	      }
	     }
	     // System.out.println(key+"=="+value);
	     prep.setString(1, key);
	     prep.setString(2, value);
	     prep.addBatch();
	    }
	   }
	   conn.setAutoCommit(false);
	   prep.executeBatch();
	   conn.setAutoCommit(true);
	   conn.close();
	   book.close();
	  } catch (Exception e) {
	   System.out.println(e);
	  }
	 }
	}


