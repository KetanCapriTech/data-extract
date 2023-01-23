package data_extrator;

import java.io.File;
import data_extrator.main;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;

import com.alluresecurity.learningautomation.domain.BrandItem;
import com.alluresecurity.utilitylib.util.Datasets;
import com.alluresecurity.utilitylib.util.PageUrlUtil;
import com.opencsv.CSVWriter;

public class EmailFinderCS {
	String[] data1;
	public static Stream<BrandItem> loadInitialList() {
		ArrayList<BrandItem> brandList = new ArrayList<>();
		String inputfile = "data" + File.separator + "Phase1Merging.txt";
		System.out.println(inputfile);
		for (String url : Datasets.readRawUrlsFromFile(inputfile)) {
			BrandItem item = new BrandItem();
			item.setUrl(url);
			item.setBrand(PageUrlUtil.findFullSubDomain(url));
			brandList.add(item);
		}
		return brandList.stream();
	}

	public static String html2text(String html) {
		try {
			html = html.replaceAll(">", "> ");
			return Jsoup.parse(html).text();
		} catch (Exception e) {
			;
			return null;
		}
	}

	public static List<String> mailFinder() throws IOException, InterruptedException {
		List<String> list=new ArrayList<String>(); 
		String url = "";
		String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss z").format(new Date(System.currentTimeMillis()));
		String outputFile = "data\\email\\" + "Email_List" + date + ".csv";
		String[] headers = { "URL", "          Email" };
		FileWriter outputfile = new FileWriter(outputFile);
		CSVWriter writer = new CSVWriter(outputfile);
		//time out logic
		RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000)
				.setSocketTimeout(10000).build();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		writer.writeNext(headers);

		try {
			Stream<BrandItem> brandItemData = loadInitialList();
			ArrayList<BrandItem> others = brandItemData.collect(Collectors.toCollection(ArrayList::new));
			for (BrandItem str : others) {

				try {
					url = str.getUrl();
					URL url1 = new URL(str.getUrl());

					HttpGet httpGet = new HttpGet(url);
					httpGet.setConfig(config);
					CloseableHttpResponse response = null;
					try {
						response = httpClient.execute(httpGet);
						int responseCodeo = response.getStatusLine().getStatusCode();
						System.out.println("HTTP response code: " + responseCodeo);
						Scanner sc1 = new Scanner(url1.openStream());
						// Instantiating the StringBuffer class to hold the result
						StringBuffer sb1 = new StringBuffer();
						while (sc1.hasNext()) {
							sb1.append(sc1.next() + "\n");
							// System.out.println(sc.next());
						}
						// Retrieving the String from the String Buffer object
						String result1 = sb1.toString();
//       	          	      FunctionsToGetDataForBothLists forBothLists = new FunctionsToGetDataForBothLists();

						result1 = html2text(result1);
//       	          	      System.out.println("Contents of the web page: "+result1);

						Pattern p = Pattern.compile(
								"[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+|/^(.+)@(.+)$| ^[A-Za-z0-9+_.-]+@(.+)$| ^[a-zA-Z0-9_!#$%&’*+\\=?`{|}~^.-]+@[a-zA-Z0-9.-]+$| ^[a-zA-Z0-9_!#$%&’*+\\=?`{|}~^-]+(?:\\\\.[a-zA-Z0-9_!#$%&’*+\\=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\\\.[a-zA-Z0-9-]+)*$| ^[\\\\w!#$%&’*+\\=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&’*+\\=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$|^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$\r\n"
										+ "/gm");
						Matcher matcher = p.matcher(result1);
						// here we are extracting contact no from document html that means from page
						// source
						HashSet<String> email = new HashSet<String>();
						while (matcher.find()) {
//       	          	        	System.out.println(phone);
							email.add(matcher.group());

						}
						System.out.println(email);
//       	          	        else {
//       	          	        	System.out.println(str.getUrl()+" data not found");
//       	          	        }
//       	                    System.out.println(str.getUrl() + phone );

						System.out.println(str.getUrl() + email);
						String[] data = { str.getUrl() + "," + email };
						writer.writeNext(data);
						list.add( " "+email );
						
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("time out");
						String[] data1 = { str.getUrl() + "Time Out " };
						writer.writeNext(data1);
						String[] data = { str.getUrl() + "     " };
						writer.writeNext(data);
						
						continue;
					}
					// Retrieving the contents of the specified page

				} catch (Exception e) {
//    	                	 System.err.println(e);
					String[] data = { str.getUrl(), "email no not Found in Source code" };
					writer.writeNext(data);
					
					continue;
				}
				writer.flush();
				for (int i=0; i<0;i++) {
					System.out.println(list);
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
		}
		System.out.println("end of the data");
		return list;
		
	}

}