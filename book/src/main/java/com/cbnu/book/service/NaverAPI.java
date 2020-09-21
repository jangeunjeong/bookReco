package com.cbnu.book.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.cbnu.book.dto.BookDTO;

@Service
public class NaverAPI {
	String clientId = "HTTapmIjIebyzQeJI0ta";// 애플리케이션 클라이언트 아이디값";
	String clientSecret = "9ZZCPpOIFq";// 애플리케이션 클라이언트 시크릿값";

	public List<BookDTO> getBookByBookName(String bookName) {
		List<BookDTO> books = new ArrayList<BookDTO>();

		try {
			String text = URLEncoder.encode(bookName, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/search/book_adv.json" + "?d_titl=" + text
					+ "&start=1&display=10";// +"&display=10&start=1";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			// string -> Object
			JSONParser jsonParse = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParse.parse(response.toString());
			JSONArray items = (JSONArray) jsonObj.get("items");

			for (int i = 0; i < items.size(); i++) {
				JSONObject item = (JSONObject) items.get(i);
				StringTokenizer line = new StringTokenizer(item.get("isbn").toString());
				String isbn = null;

				if (line.countTokens() == 2) {
					System.out.println(line.nextToken());
					isbn = line.nextToken();
					System.out.println(isbn);
				} else {
					isbn = line.nextToken();
					System.out.println(isbn);
				}
				System.out.println("=============");
				System.out.println(item.toString());
				BookDTO book = new BookDTO();
				book.setBookISBN(isbn);
				book.setBookName(item.get("title").toString().replace("<b>", "").replace("</b>", ""));
				book.setBookPrice(Integer.parseInt(item.get("price").toString()));
				book.setPublisher(item.get("publisher").toString());
				book.setPublishDate(item.get("pubdate").toString());
				book.setAuthor(item.get("author").toString());
				book.setBookImg(item.get("image").toString());
				books.add(book);
			}
			for (int i = 0; i < books.size(); i++) {
				System.out.println(books.get(i));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return books;
	}

	public JSONObject getBookByISBN(String bookISBN) {
		List<BookDTO> books = new ArrayList<BookDTO>();
		JSONObject item = new JSONObject();

		try {
			String text = URLEncoder.encode(bookISBN, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/search/book_adv.json" + "?d_isbn=" + text
					+ "&start=1&display=10";// +"&display=10&start=1";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			// string -> Object
			JSONParser jsonParse = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParse.parse(response.toString());
			JSONArray items = (JSONArray) jsonObj.get("items");
			item = (JSONObject) items.get(0);

		} catch (Exception e) {
			System.out.println(e);
		}
		return item;
	}
}
