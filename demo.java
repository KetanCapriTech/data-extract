package data_extrator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
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
import org.jsoup.nodes.Document;

import com.alluresecurity.learningautomation.domain.BrandItem;
import com.alluresecurity.utilitylib.util.Datasets;
import com.alluresecurity.utilitylib.util.PageUrlUtil;


public class demo {
	static String pageSource ;
	static List<String> list;
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

	public static void main(String[] args) throws IOException, InterruptedException {
		 list = AppliId();
		System.out.println(AppliId());
	}

	public static  List<String> AppliId() throws IOException, InterruptedException {
		System.out.println("running Applid");
		List<String> list=new ArrayList<String>(); 

		String url = null;
		
		//time out logic
		RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000)
				.setSocketTimeout(10000).build();
		CloseableHttpClient httpClient = HttpClients.createDefault();

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
						
//       	          	      FunctionsToGetDataForBothLists forBothLists = new FunctionsToGetDataForBothLists();

						Document doc = Jsoup.connect(url).get();
						System.out.println(url);
						
						String pageSourceContent = doc.html();
						pageSource = pageSourceContent ;
//       	          	      System.out.println("Contents of the web page: "+result1);
						String regex = "(?i)<meta.*?name=['\"]?" + "description" + "['\"]?.*?content=['\"](.*?)['\"].*?>";
						Pattern pattern = Pattern.compile(regex);
						Matcher matcher = pattern.matcher(pageSource);
						
						// here we are extracting contact no from document html that means from page
						// source
						HashSet<String> appleid = new HashSet<String>();
						while (matcher.find()) {
//       	          	        	System.out.println(appleid);
//							appleid.add(matcher.group());
							list.add(matcher.group());

						}
						System.out.println(appleid);
//       	          	        else {
//       	          	        	System.out.println(str.getUrl()+" data not found");
//       	          	        }
//       	                    System.out.println(str.getUrl() + appleid );

						System.out.println(str.getUrl() + appleid);
						String[] data = { str.getUrl() + "," + appleid };
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("time out");
						String[] data1 = { str.getUrl() + "Time Out " };
						String[] data = { str.getUrl() + "     " };
						
						continue;
					}
					// Retrieving the contents of the specified page

				} catch (Exception e) {
//    	                	 System.err.println(e);
					String[] data = { str.getUrl(), "appleid\\mobile no not Found in Source code" };
					continue;
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

