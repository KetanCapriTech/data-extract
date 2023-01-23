package data_extrator;

//package com.alluresecurity.learningautomation.word_counter;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//
//import com.alluresecurity.learningautomation.domain.BrandItem;
//import com.alluresecurity.utilitylib.util.Datasets;
//import com.alluresecurity.utilitylib.util.PageUrlUtil;
//import com.opencsv.CSVWriter;
//
//public class Manual {
//
//	public static Stream<BrandItem> loadInitialList() {
//		ArrayList<BrandItem> brandList = new ArrayList<>();
//		String inputfile = "data" + File.separator + "MaualCheckFile.txt";
//		System.out.println(inputfile);
//		for (String url : Datasets.readRawUrlsFromFile(inputfile)) {
//			BrandItem item = new BrandItem();
//			item.setUrl(url);
//			item.setBrand(PageUrlUtil.findFullSubDomain(url));
//			brandList.add(item);
//		}
//		return brandList.stream();
//	}
//
//	public static void main(String[] args) throws Exception {
//
//		String url = null;
//		String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss z").format(new Date(System.currentTimeMillis()));
//		String outputFile = "data\\ManualCheckFile\\" + "ManualCheckFile_result" + date + ".csv";
//		String[] headers = { "URL", "          info" };
//		FileWriter outputfile = new FileWriter(outputFile);
//		CSVWriter writer = new CSVWriter(outputfile);
//		writer.writeNext(headers);
//		
//		
//			
//		try {
//			Stream<BrandItem> brandItemData = loadInitialList();
//			ArrayList<BrandItem> others = brandItemData.collect(Collectors.toCollection(ArrayList::new));
//			for (BrandItem str : others) {
//				
//				try {
//					// Set the URL of the website you want to find the sitemap for
//					
//
//					// Send an HTTP request to the sitemap.xml URL
//					HttpURLConnection connection = (HttpURLConnection) new URL(url + "sitemap.xml").openConnection();
//					HttpURLConnection connection1 = (HttpURLConnection) new URL(url + "robots.txt").openConnection();
//
//					connection.setRequestMethod("HEAD");
//
//					// Check the response status code
//					int statusCode = connection.getResponseCode();
//					int statusCode1 = connection1.getResponseCode();
//					
//					if (statusCode == HttpURLConnection.HTTP_OK) {
//						// If the status code is 200 (OK), the sitemap exists at the URL
//						System.out.println("Sitemap URL: " + url + "sitemap.xml");
//						String[] data = { str.getUrl() + "/sitemap.xml"  };
//						writer.writeNext(data);
//					} else if (statusCode1 == HttpURLConnection.HTTP_OK || statusCode1 == 503 || statusCode1 == 500) {
//						System.out.println("robots.txt  found at " + url + "robots.txt");
//						String[] data = { str.getUrl() + "/robots.txt " };
//						writer.writeNext(data);
//					} else {
//						System.out.println("not found " + statusCode);
//						String[] data = { str.getUrl() + " Not found" };
//						writer.writeNext(data);
//
//					}
//					System.out.println(str.getUrl() + statusCode );
////					String[] data = { str.getUrl() + "," + statusCode  };
////					writer.writeNext(data);
//				} catch (Exception e) {
//// 	                	 System.err.println(e);
//					String[] data = { str.getUrl(), "not found " };
//					writer.writeNext(data);
//					continue;
//				}
//				writer.flush();
//			}
//		} catch (Exception e) {
//			System.err.println(e);
//		} finally {
//		}
//	}
//
//}

//package com.alluresecurity.learningautomation.word_counter;

import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openqa.selenium.devtools.v85.runtime.model.ExceptionDetails;

import com.alluresecurity.learningautomation.domain.BrandItem;
import com.alluresecurity.utilitylib.util.Datasets;
import com.alluresecurity.utilitylib.util.PageUrlUtil;
import com.opencsv.CSVWriter;

public class Manual {

	public static Stream<BrandItem> loadInitialList() {
		ArrayList<BrandItem> brandList = new ArrayList<>();
		String inputfile = "data" + File.separator + "MaualCheckFile.txt";
		System.out.println(inputfile);
		for (String url : Datasets.readRawUrlsFromFile(inputfile)) {
			BrandItem item = new BrandItem();
			item.setUrl(url);
			item.setBrand(PageUrlUtil.findFullSubDomain(url));
			brandList.add(item);
		}
		return brandList.stream();
	}

	public static void main(String[] args) throws Exception {

		String url = null;
		String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss z").format(new Date(System.currentTimeMillis()));
		String outputFile = "data\\ManualCheckFile\\" + "ManualCheckFile_result" + date + ".csv";
		String[] headers = { "URL", "          info" };
		FileWriter outputfile = new FileWriter(outputFile);
		CSVWriter writer = new CSVWriter(outputfile);
		writer.writeNext(headers);
		String sitemap = "sitemap.xml";
		String sitemap1 = "wp-sitemap.xml";
		int statusCode = 0;
		int statusCode1 = 0;

		try {
			Stream<BrandItem> brandItemData = loadInitialList();
			ArrayList<BrandItem> others = brandItemData.collect(Collectors.toCollection(ArrayList::new));
			for (BrandItem str : others) {

				try {
					// Set the URL of the website you want to find the sitemap for
					url = str.getUrl();
					
					HttpURLConnection connection1 = (HttpURLConnection) new URL(url + "robots.txt").openConnection();

					// Send an HTTP request to the sitemap.xml URL
					try {
						if (statusCode == 200) {
							HttpURLConnection connection = (HttpURLConnection) new URL(url + sitemap).openConnection();
							connection.setConnectTimeout(15000); // timeout in milliseconds
							connection.setReadTimeout(15000); // timeout in milliseconds
							connection.setRequestMethod("HEAD");

							// Check the response status code
							statusCode = connection.getResponseCode();
						} else {
							HttpURLConnection connection = (HttpURLConnection) new URL(url + sitemap1).openConnection();
							connection.setConnectTimeout(15000); // timeout in milliseconds
							connection.setReadTimeout(15000); // timeout in milliseconds
							connection.setRequestMethod("HEAD");

							// Check the response status code
							statusCode = connection.getResponseCode();
						}

					} catch (Exception e) {

						// TODO: handle exception
						if(statusCode1 == 200 && statusCode == 200) {
							String[] data = { str.getUrl() + " site map found" };
							writer.writeNext(data); 
							continue;
						}
						System.out.println("time out " + url + statusCode + " " + statusCode1);
						String[] data = { str.getUrl() + " >>> Not found or site certifcate issue or time out" };
						writer.writeNext(data); 
						continue;
					} 	

					try {
						connection1 = (HttpURLConnection) new URL(url + "robots.txt").openConnection();
						connection1.setConnectTimeout(15000); // timeout in milliseconds
						connection1.setReadTimeout(15000); // timeout in milliseconds
						connection1.setRequestMethod("HEAD");

						// Check the response status code
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("time out error " + url);
						continue;

					}
					statusCode1 = connection1.getResponseCode();

					if (statusCode == HttpURLConnection.HTTP_OK || statusCode ==200 || statusCode1==200) {
						// If the status code is 200 (OK), the sitemap exists at the URL
						System.out.println("Sitemap URL: " + url + "sitemap.xml");
						String[] data = { str.getUrl() + sitemap };
						writer.writeNext(data);
						continue;
					} else if (statusCode == HttpURLConnection.HTTP_OK ) {
						System.out.println("Sitemap URL: " + url + "sitemap.xml");
						String[] data = { str.getUrl() + sitemap1 };
						writer.writeNext(data);
						continue;
					}

					else if (statusCode1 == HttpURLConnection.HTTP_OK || statusCode1 == 503 || statusCode1 == 500 || statusCode == 200 || statusCode1 ==200) {
						System.out.println("robots.txt  found at " + url + "robots.txt");
						String[] data = { str.getUrl() + "robots.txt " };
						writer.writeNext(data);
						continue;
					} else {
						System.out.println("not found " + statusCode);
						String[] data = { str.getUrl() + " not found " + statusCode };
						writer.writeNext(data);
						continue;
					}

				} catch (Exception e) {
				System.out.println( str.getUrl() + " " +statusCode);
					String[] data = { str.getUrl() + " Exception occured "  };
					writer.writeNext(data);
					e.printStackTrace();
				}
			}
			writer.flush();
			outputfile.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("end of the data");
		}
	}
}
