package com.cbnu.book.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookDTO {
	private String bookId;
	private String bookISBN;
	private String bookName;
	private int bookPrice;
	private String publisher;
	private String publishDate;
	private String author;
	private String bookImg;
	private String categoryId;
	private String bookSummary;

}
