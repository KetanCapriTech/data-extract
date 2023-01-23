package data_extrator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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


public class FaviconData {
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

	
	public static List<String>  favicon() throws IOException, InterruptedException {
		System.out.println("running");
		List<String> list=new ArrayList<String>(); 
		String url = null;
//		String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss z").format(new Date(System.currentTimeMillis()));
//		String outputFile = "data\\favicon\\" + "favicon_List" + date + ".csv";
//		String[] headers = { "URL", "          favicon" };
//		FileWriter outputfile = new FileWriter(outputFile);
//		CSVWriter writer = new CSVWriter(outputfile);
		//time out logic
		RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000)
				.setSocketTimeout(10000).build();
		CloseableHttpClient httpClient = HttpClients.createDefault();
//		writer.writeNext(headers);

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
       	          	    System.out.println("Contents of the web page: "+result1);
       	          	    Document doc = Jsoup.connect(url).get();
       	          	    System.out.println(url);
       	          	    String pageSource ;

       	          	    String pageSourceContent = doc.html();
       	          	    pageSource = pageSourceContent ;
						Pattern p = Pattern.compile("(?i)<link.*?rel=['\"]?(icon|shortcut icon|apple-touch-icon|mask-icon)['\"]?.*?href=['\"](.*?)['\"].*?>");
						Matcher matcher = p.matcher(pageSource);
						// here we are extracting contact no from document html that means from page
						// source
						HashSet<String> favicon = new HashSet<String>();
						while (matcher.find()) {
//       	          	        	System.out.println(favicon);
							favicon.add(matcher.group());

						}
						System.out.println(favicon);
//       	          	        else {
//       	          	        	System.out.println(str.getUrl()+" data not found");
//       	          	        }
//       	                    System.out.println(str.getUrl() + favicon );

						System.out.println(str.getUrl() + favicon);
						String[] data = { str.getUrl() + "," + favicon };
//						writer.writeNext(data);
						list.add( " "+favicon );
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("time out");
						String[] data1 = { str.getUrl() + "Time Out " };
//						writer.writeNext(data1);
						String[] data = { str.getUrl() + "     " };
//						writer.writeNext(data);
						continue;
					}
					// Retrieving the contents of the specified page

				} catch (Exception e) {
//    	                	 System.err.println(e);
					String[] data = { str.getUrl(), "favicon\\mobile no not Found in Source code" };
//					writer.writeNext(data);
					continue;
				}
//				writer.flush();
			}
			for (int i=0; i<0;i++) {
				System.out.println(list);
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
		}
		System.out.println("end of the data");
		return list;
		
		
	}

}

