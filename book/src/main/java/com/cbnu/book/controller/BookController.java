package com.cbnu.book.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbnu.book.dto.BookDTO;
import com.cbnu.book.service.NaverAPI;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = ("*"), maxAge = 6000) // 혹시나 에러나면 maxAge 빼기
@RequestMapping("/api") // url과 컨트롤러의 메서드 매핑할때 사용하는 스프링 프레임워크 어노테이션
@RestController // 메서드마다 @ResponseBody 안붙여도 전송가능
public class BookController {

	@Autowired
	private NaverAPI naverservice;

	@GetMapping("/book/request/info/{bookName}")
	@ApiOperation(value = "이름으로 책 검색")
	public ResponseEntity<Map<String, Object>> getRequestBookInof(@PathVariable String bookName) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			List<BookDTO> book = naverservice.getBookByBookName(bookName);

			resultMap.put("status", true);
			resultMap.put("info", book);
			status = HttpStatus.ACCEPTED;

		} catch (RuntimeException e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/user/test")
	public ResponseEntity<Map<String, Object>> test() {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		try {
			System.out.println("test");
			JSONObject item = naverservice.getBookByISBN("9791162241998");
			System.out.println(item);
			System.out.println("controller : " + item.get("description"));
			System.out.println("controller : " + item.get("link"));
			status = HttpStatus.ACCEPTED;
		} catch (RuntimeException e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/book/detail/{bookISBN}")
	public ResponseEntity<Map<String, Object>> getBookDetail(@PathVariable String bookISBN) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		try {
			System.out.println("북 디테일 시작");
			JSONObject item = naverservice.getBookByISBN(bookISBN);
			String description = (String) item.get("description");
			String link = (String) item.get("link");

			status = HttpStatus.OK;
			resultMap.put("description", description);
			resultMap.put("link", link);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

}
