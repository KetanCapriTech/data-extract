package data_extrator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.opencsv.CSVWriter;
import com.alluresecurity.learningautomation.domain.BrandItem;
import com.alluresecurity.utilitylib.util.Datasets;
import com.alluresecurity.utilitylib.util.PageUrlUtil;

public class SiteMapChecker {

//	static int responseCode1;
//	static String url = "";

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

	private static int checkUrlForSitemap(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		return con.getResponseCode();
	}

	public static List<String>  SiteMapChecker() throws IOException, InterruptedException {
		List<String> list=new ArrayList<String>(); 
		int responseCode0 = 0;
		int responseCode1 = 0;
		int responseCode2 = 0;
		int responseCode3 = 0;
		int responseCode4 = 0;
		int responseCode5 = 0;

		String url = null;

		String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss z").format(new Date(System.currentTimeMillis()));
		String outputFile = "data\\sitemap\\" + "SiteMap" + date + ".csv";
		// output file manual check
		String ManualCheckFile = "data\\ManualCheckFile\\" + "ManualCheckFile" + date + ".csv";
		String[] headers = { "URL", "                               sitemap_address","                                status" };
		String[] headers2 = { "URL", "                               sitemap_address","                                status" };
		
		FileWriter outputfile = new FileWriter(outputFile);
		FileWriter outputfile1 = new FileWriter(ManualCheckFile);

		try (CSVWriter writer = new CSVWriter(outputfile)) {
			writer.writeNext(headers);
			try (CSVWriter writer1 = new CSVWriter(outputfile1)) {
				writer1.writeNext(headers2);

				try {
					Stream<BrandItem> brandItemData = loadInitialList();
					ArrayList<BrandItem> others = brandItemData.collect(Collectors.toCollection(ArrayList::new));
					for (BrandItem str : others) {
						try {
							url = str.getUrl();
							System.out.println(url);
//    	            		
							URL url1 = new URL(url + "/sitemap.xml");
							URL url2 = new URL(url + "/sitemap_index.xml");
							URL url3 = new URL(url + "/sitemap.php");
							URL url4 = new URL(url + "/sitemaps-1-sitemap.xml");
							URL url5 = new URL(url + "/sitemap.xml.gz");
							URL url6 = new URL(url + "/sitemap/index.xml");

							// Create an array of URLs to check
							URL[] urls = { url1, url2, url3, url4, url5, url6 };
							RequestConfig config = RequestConfig.custom()
													.setConnectTimeout(10000)
													.setConnectionRequestTimeout(10000)
													.setSocketTimeout(10000).build();
							// Iterate through the array of URLs
							CloseableHttpClient httpClient = HttpClients.createDefault();
							for (URL sitemapUrl : urls) {
								HttpGet httpGet = new HttpGet(url);
								httpGet.setConfig(config);
								CloseableHttpResponse response = null;
								// Check the URL for the presence of a sitemap
								try {
									response = httpClient.execute(httpGet);
									int responseCodeo = response.getStatusLine().getStatusCode();
									System.out.println("HTTP response code: " + responseCodeo);

									// Get the response entity
									// Do something with the response entity
									// ...

									switch (checkUrlForSitemap(sitemapUrl)) {
									case 200:
										// If the response code is 200 (OK), then the sitemap was found
										System.out.println("Sitemap found at: " + sitemapUrl);
										break;
									case 404:
										// If the response code is 404 (Not Found), then the sitemap was not found
										System.out.println("Sitemap not found at: " + sitemapUrl);
										break;
									default:
										// If the response code is anything else, then there was an error
										System.out.println("Error checking URL: " + sitemapUrl);
										break;

									}

									HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
									HttpURLConnection huc1 = (HttpURLConnection) url2.openConnection();
									HttpURLConnection huc2 = (HttpURLConnection) url3.openConnection();
									HttpURLConnection huc3 = (HttpURLConnection) url4.openConnection();
									HttpURLConnection huc4 = (HttpURLConnection) url5.openConnection();
									HttpURLConnection huc5 = (HttpURLConnection) url6.openConnection();

									huc.setConnectTimeout(5000);
									huc1.setConnectTimeout(5000);
									huc2.setConnectTimeout(5000);
									huc3.setConnectTimeout(5000);
									huc4.setConnectTimeout(5000);
									huc5.setConnectTimeout(5000);

									responseCode0 = huc.getResponseCode();
									responseCode1 = huc1.getResponseCode();
									responseCode2 = huc2.getResponseCode();
									responseCode3 = huc3.getResponseCode();
									responseCode4 = huc4.getResponseCode();
									responseCode5 = huc5.getResponseCode();
									System.out.println(responseCode0);
//									|| huc1.getResponseCode() == 200|| huc2.getResponseCode() == 200 || huc3.getResponseCode() == 200 || huc4.getResponseCode() == 200 || huc5.getResponseCode() == 200
									if (huc.getResponseCode() == 200 )
									
									{
										System.out.println(responseCode0);
										System.out.println(url + "sitemap.xml" + " , Sitemap Found" + ",  Status code :- " + responseCode0);
										String[] data = { str.getUrl(), "   ", url + "sitemap.xml", "   ", "Sitemap found ", " " + responseCode0};
										writer.writeNext(data);
										list.add( " "+sitemapUrl );
										break;
									}
									else if (huc1.getResponseCode() == 200) {
										System.out.println(responseCode1);
										System.out.println(url + "sitemap.xml" + " , Sitemap Found" + ",  Status code :- " + responseCode1);
										String[] data = { str.getUrl(), "   ", url + "sitemap.xml", "   ", "Sitemap found ", " " + responseCode1};
										writer.writeNext(data);
										list.add( " "+sitemapUrl );
										break;
									}
									else if (huc2.getResponseCode() == 200 ) {
										System.out.println(responseCode2);
										System.out.println(url + "sitemap.xml" + " , Sitemap Found" + ",  Status code :- " + responseCode2);
										String[] data = { str.getUrl(), "   ", url + "sitemap.xml", "   ", "Sitemap found ", " " + responseCode2};
										writer.writeNext(data);
										list.add( " "+sitemapUrl );
										break;
									}
									else if (huc3.getResponseCode() == 200) {
										System.out.println(responseCode3);
										System.out.println(url + "sitemap.xml" + " , Sitemap Found" + ",  Status code :- " + responseCode3);
										String[] data = { str.getUrl(), "   ", url + "sitemap.xml", "   ", "Sitemap found ", " " + responseCode3};
										writer.writeNext(data);
										list.add( " "+sitemapUrl );
										break;
										
									}
									else if (huc4.getResponseCode() == 200) {
										System.out.println(responseCode4);
										System.out.println(url + "sitemap.xml" + " , Sitemap Found" + ",  Status code :- " + responseCode4);
										String[] data = { str.getUrl(), "   ", url + "sitemap.xml", "   ", "Sitemap found ", " " + responseCode4};
										writer.writeNext(data);
										list.add( " "+sitemapUrl );
										break;
										
									}
									else if (huc5.getResponseCode() == 200) {
										System.out.println(responseCode5);
										System.out.println(url + "sitemap.xml" + " , Sitemap Found" + ",  Status code :- " + responseCode5);
										String[] data = { str.getUrl(), "   ", url + "sitemap.xml", "   ", "Sitemap found ", " " + responseCode5};
										writer.writeNext(data);
										list.add(" "+sitemapUrl );
										break;
										
									}

									else if (responseCode0 == 404 || responseCode0 == 403 || responseCode1 == 404|| responseCode2 == 404 || responseCode3 == 404 || responseCode4 == 404|| responseCode5 == 404) {
										System.out.println(url + "robots.txt");
										System.out.println(url + " robots.txt " + responseCode0);
										String[] data = { str.getUrl(), "   ", url + "robots.txt", "   ","robots.txt found ", "   " + responseCode0 };
										writer.writeNext(data);
										list.add( " "+sitemapUrl );
										break;
//    	            			System.out.println(url +" ->>>>>> , Sitemap not found" + ", Status code :- "+responseCode);
									} else if (responseCode0 == 400 || responseCode0 == 401 || responseCode0 == 402 || responseCode0 == 403 || responseCode0 == 500 || responseCode0 == 302) {
										System.out.println(responseCode0);
//    	            			continue;
									} else {

										System.out.println("went wroung ");

									}


								} catch (Exception e) {
									System.out.println("time out");
									String[] data1 = { str.getUrl() + "Check in manualCheckFile.csv " };
									writer.writeNext(data1);
									String[] data = { str.getUrl() + "     " };
									writer1.writeNext(data);
									list.add(" "+sitemapUrl );
									break;
								}
							}

						} catch (Exception ConnectException) {
							String[] data = { str.getUrl() + " bad url or Connection error " + responseCode0  };//need to update
							writer.writeNext(data);
							list.add(" "+data );
							System.err.println(str.getUrl() + " bad url or Connection error " + responseCode0);
							break;
						}
						writer.flush();
						writer1.flush();
					}
				} catch (Exception e) {
					System.err.println(e);
				} finally {
					System.out.println("End of the urls");
				}
			}
		}
		return list;

	}

	
}
