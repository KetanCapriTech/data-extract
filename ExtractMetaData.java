package data_extrator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ExtractMetaData {

    public static ArrayList<String> extractKeywords(String url) throws IOException {
        ArrayList<String> keywords = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        String textContent = doc.body().text();
        Scanner scanner = new Scanner(textContent);
        scanner.useDelimiter("[^a-zA-Z']+");

        while (scanner.hasNext()) {
            String word = scanner.next().toLowerCase();
            if (!keywords.contains(word)) {
                keywords.add(word);
            }
        }
        scanner.close();
        return keywords;
    }

    public static void main(String[] args) throws IOException {
        String url = "https://www.cumberland.co.uk/";
        ArrayList<String> keywords = extractKeywords(url);
        System.out.println("Extracted keywords: " + keywords);
    }
}
