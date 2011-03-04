package net.caprazzi.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Get random quotes from an array
 */
public class QuoteService {

	private final String[] quotes;
	private final Random random = new Random();
	
	public QuoteService(String[] quotes) {
		this.quotes = quotes;				
	}
	
	public String getRandomQuote() {
		int pos = random.nextInt(this.quotes.length);
		return quotes[pos];
	}
	
	/**
	 * Static factory. Builds a QuoteService from a text files where each line is a quote
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static QuoteService fromFile(String filename) throws IOException {
		File file = new File(filename);
		FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        String[] quotes = lines.toArray(new String[lines.size()]);
        return new QuoteService(quotes);
	}	
	
}
