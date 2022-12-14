package com.payhere.accountbook.domain.accountbook.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.payhere.accountbook.config.ControllerTestConfig;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookRegisterRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookUpdateRequest;
import com.payhere.accountbook.domain.accountbook.service.BookService;
import com.payhere.accountbook.domain.accountbook.service.dto.BookResponse;
import com.payhere.accountbook.domain.accountbook.service.dto.BookResponses;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest extends ControllerTestConfig {
	@MockBean
	BookService bookService;

	@Test
	@DisplayName("/api/v1/book/{bookId} 에서 가계부(일)을 조회한다.")
	void getBook() throws Exception {
		// given
		BookResponse bookResponse = getBookResponse(1L, 2022L, 9L, 27L, "good day");
		given(bookService.find(anyLong())).willReturn(bookResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			get("/api/v1/book/{bookId}", 1)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(objectMapper.writeValueAsString(bookResponse)))
			.andDo(document(COMMON_DOCS_NAME,
				pathParameters(
					parameterWithName("bookId").description("요청할 가계부(일) 아이디")
				),
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("가계부(일) 아이디"),
					fieldWithPath("year").type(NUMBER).description("년"),
					fieldWithPath("month").type(NUMBER).description("월"),
					fieldWithPath("day").type(NUMBER).description("일"),
					fieldWithPath("income").type(NUMBER).description("일일 수입"),
					fieldWithPath("outcome").type(NUMBER).description("일일 지출"),
					fieldWithPath("title").type(STRING).description("제목"),
					fieldWithPath("memo").type(STRING).description("메모")
				)));
	}

	@Test
	@DisplayName("/api/v1/book 에서 가계부(일)을 전부 조회한다.")
	void getBooks() throws Exception {
		// given
		BookResponse bookResponse1 = getBookResponse(1L, 2022L, 9L, 27L, "good day");
		BookResponse bookResponse2 = getBookResponse(2L, 2022L, 9L, 28L, "good day");
		List<BookResponse> books = new ArrayList<>(List.of(bookResponse1, bookResponse2));
		BookResponses bookResponses = new BookResponses(books);

		given(bookService.findBooks(any())).willReturn(bookResponses);

		// when
		ResultActions resultActions = mockMvc.perform(
			get("/api/v1/book")
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(objectMapper.writeValueAsString(bookResponses)))
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseFields(
					fieldWithPath("books.[]").type(ARRAY).description("가계부(일) 리스트"),
					fieldWithPath("books.[].id").type(NUMBER).description("가계부(일) 아이디"),
					fieldWithPath("books.[].year").type(NUMBER).description("년"),
					fieldWithPath("books.[].month").type(NUMBER).description("월"),
					fieldWithPath("books.[].day").type(NUMBER).description("일"),
					fieldWithPath("books.[].income").type(NUMBER).description("일일 수입"),
					fieldWithPath("books.[].outcome").type(NUMBER).description("일일 지출"),
					fieldWithPath("books.[].title").type(STRING).description("제목"),
					fieldWithPath("books.[].memo").type(STRING).description("메모")
				)));
	}

	@Test
	@DisplayName("/api/v1/book 에서 가계부(일)을 생성한다")
	void postBook() throws Exception {
		// given
		BookRegisterRequest bookRegisterRequest = BookRegisterRequest.builder()
			.year(2022L)
			.month(9L)
			.day(12L)
			.title("good day")
			.memo("memo")
			.build();
		BookResponse bookResponse = getBookResponse(1L, 2022L, 9L, 12L, "good day");
		given(bookService.register(any(), any())).willReturn(bookResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/v1/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookRegisterRequest)));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().json(objectMapper.writeValueAsString(bookResponse)))
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				requestFields(
					fieldWithPath("year").type(NUMBER).description("년"),
					fieldWithPath("month").type(NUMBER).description("월"),
					fieldWithPath("day").type(NUMBER).description("일"),
					fieldWithPath("title").type(STRING).description("제목"),
					fieldWithPath("memo").type(STRING).description("메모")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("가계부(일) 아이디"),
					fieldWithPath("year").type(NUMBER).description("년"),
					fieldWithPath("month").type(NUMBER).description("월"),
					fieldWithPath("day").type(NUMBER).description("일"),
					fieldWithPath("income").type(NUMBER).description("일일 수입"),
					fieldWithPath("outcome").type(NUMBER).description("일일 지출"),
					fieldWithPath("title").type(STRING).description("제목"),
					fieldWithPath("memo").type(STRING).description("메모")
				)));
	}

	@Test
	@DisplayName("/api/v1/book 에서 가계부(일)을 수정한다")
	void patchBook() throws Exception {
		// given
		BookUpdateRequest bookUpdateRequest = new BookUpdateRequest(1L, "changed day", "memo");
		BookResponse bookResponse = getBookResponse(1L, 2022L, 9L, 17L, "changed day");
		given(bookService.update(any())).willReturn(bookResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			patch("/api/v1/book")
				.content(objectMapper.writeValueAsString(bookUpdateRequest))
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().json(objectMapper.writeValueAsString(bookResponse)))
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				requestFields(
					fieldWithPath("id").type(NUMBER).description("가계부(일) 아이디"),
					fieldWithPath("title").type(STRING).description("변경할 제목"),
					fieldWithPath("memo").type(STRING).description("메모")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("가계부(일) 아이디"),
					fieldWithPath("year").type(NUMBER).description("년"),
					fieldWithPath("month").type(NUMBER).description("월"),
					fieldWithPath("day").type(NUMBER).description("일"),
					fieldWithPath("income").type(NUMBER).description("일일 수입"),
					fieldWithPath("outcome").type(NUMBER).description("일일 지출"),
					fieldWithPath("title").type(STRING).description("제목"),
					fieldWithPath("memo").type(STRING).description("메모")
				)));
	}

	private BookResponse getBookResponse(Long id, Long year, Long month, Long day, String title) {
		return BookResponse.builder()
			.id(id)
			.year(year)
			.month(month)
			.day(day)
			.income(2500L)
			.outcome(1200L)
			.title(title)
			.memo("memo")
			.build();
	}
}