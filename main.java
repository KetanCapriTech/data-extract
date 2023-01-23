package data_extrator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.kafka.clients.Metadata;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alluresecurity.learningautomation.domain.BrandItem;
import com.alluresecurity.utilitylib.util.Datasets;
import com.alluresecurity.utilitylib.util.PageUrlUtil;
import com.opencsv.CSVWriter;

public class main {
	static String domain = "";
	static List<String> url;

	public static List<String> domainUrl() {
		List<String> list = new ArrayList<String>();

		ArrayList<BrandItem> brandList = new ArrayList<>();
		String inputfile = "data" + File.separator + "Phase1Merging.txt";
		System.out.println(inputfile);
		for (String url : Datasets.readRawUrlsFromFile(inputfile)) {
			BrandItem item = new BrandItem();
			item.setUrl(url);
			item.setBrand(PageUrlUtil.findFullSubDomain(url));
			brandList.add(item);
			domain = url;
			list.add(url);

		}
		return list;
	}
	public static void main(String[] args) throws IOException, InterruptedException {

		ArrayList<BrandItem> brandList = new ArrayList<>();
		String inputfile = "data" + File.separator + "Phase1Merging.txt";
		System.out.println(inputfile);
		for (String url : Datasets.readRawUrlsFromFile(inputfile)) {
			BrandItem item = new BrandItem();
			item.setUrl(url);
			item.setBrand(PageUrlUtil.findFullSubDomain(url));
			brandList.add(item);

//		String html = HtmlContent.html();
			try {
				Document doc = Jsoup.connect(url).get();
				System.out.println(url);
				String pageSourceContent = doc.html();
				String pageSource = pageSourceContent;
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage()+e.getLocalizedMessage());
			}
			System.out.println("runnning");
			String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss z").format(new Date(System.currentTimeMillis()));
			String outputFile = "data\\MergedDataPhase0\\" + "FinalData" + date + ".csv";
			String[] headers = { "                     sitemap", "                     Phone",
					"                          Email", "                          AppleId",
					"                                favicon", "                                keyword",
					"                            description", "                              title" };
			FileWriter outputfile = new FileWriter(outputFile);
			CSVWriter writer = new CSVWriter(outputfile);
			writer.writeNext(headers);
			try {
				System.out.println("runnning");

				List<String> sitemap = SiteMapChecker.SiteMapChecker();
				List<String> email = EmailFinderCS.mailFinder();
				List<String> phone = PhoneNumberFinder.Phone();
				List<String> title = GetTitle.title();
				List<String> description = MetaDescription.description();
				List<String> favicon = FaviconData.favicon();
				List<String> appleId = AppleIdFinder.appleId();
				List<String> keyword = MetaKeyword.extractKeywords();

//				System.out.println(phone + " ----------" + email + " --------- " + "-------" + sitemap + "  --  "
//						+ "---- " + " ---  " + description + " ---- " + title);

				for (int i = 0; i < Math.max(Math.max(keyword.size(), keyword.size()),Math.max(keyword.size(), keyword.size())); i++) {
					String[] data = {
							i < sitemap.size() ? sitemap.get(i) : "",
							i < phone.size() ? phone.get(i) : "",
							i < email.size() ? email.get(i) : "",
                    		i < appleId.size() ? appleId.get(i) : "",
							i < favicon.size() ? favicon.get(i) : "",
							i < keyword.size() ? keyword.get(i) : "",
							i < description.size() ? description.get(i) : "",
							i < title.size() ? title.get(i) : "",
					
					};
					writer.writeNext(data);
				}
				System.out.println("end of the main method");

			} catch (Exception e) {
				System.out.println("something wrong" + e.getLocalizedMessage()+ e.getStackTrace() + e.getMessage());
			}

			writer.flush();

		}
	}

}
