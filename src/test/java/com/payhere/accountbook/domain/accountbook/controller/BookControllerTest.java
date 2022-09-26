package com.payhere.accountbook.domain.accountbook.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.payhere.accountbook.config.ControllerTestConfig;
import com.payhere.accountbook.domain.accountbook.service.BookService;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest extends ControllerTestConfig {
	@MockBean
	BookService bookService;
}