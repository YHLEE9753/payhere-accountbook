package com.payhere.accountbook.domain.accountbook.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseDeleteRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseRegisterRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseReviveRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseUpdateRequest;
import com.payhere.accountbook.domain.accountbook.service.BookCaseService;
import com.payhere.accountbook.domain.accountbook.service.dto.BookCaseResponse;
import com.payhere.accountbook.domain.accountbook.service.dto.BookCaseResponses;
import com.payhere.accountbook.domain.accountbook.service.dto.BookResponse;
import com.payhere.accountbook.domain.accountbook.service.dto.BookResponses;

@WebMvcTest(controllers = BookCaseController.class)
class BookCaseControllerTest extends ControllerTestConfig {
	@MockBean
	BookCaseService bookCaseService;

	@Test
	@DisplayName("/api/v1/book/{bookId}/bookcase/{bookCaseId} ?????? ?????????(??????)??? ????????????.")
	void getBookCase() throws Exception {
		// given
		BookCaseResponse bookCaseResponse = getBookCaseResponse(1L);
		given(bookCaseService.find(anyLong())).willReturn(bookCaseResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			get("/api/v1/book/{bookId}/bookcase/{bookCaseId}", 1, 1)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(objectMapper.writeValueAsString(bookCaseResponse)))
			.andDo(document(COMMON_DOCS_NAME,
				pathParameters(
					parameterWithName("bookId").description("????????? ?????????(??????) ?????????"),
					parameterWithName("bookCaseId").description("????????? ?????????(??????) ?????????")
				),
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("?????????(??????) ?????????"),
					fieldWithPath("income").type(NUMBER).description("?????? ??????"),
					fieldWithPath("outcome").type(NUMBER).description("?????? ??????"),
					fieldWithPath("title").type(STRING).description("??????"),
					fieldWithPath("place").type(STRING).description("??????"),
					fieldWithPath("memo").type(STRING).description("??????")
				)));
	}

	@Test
	@DisplayName("/api/v1/book/{bookId}/bookcase ?????? ?????????(??????)??? ?????? ????????????.")
	void getBookCases() throws Exception {
		// given
		BookCaseResponse bookCaseResponse1 = getBookCaseResponse(1L);
		BookCaseResponse bookCaseResponse2 = getBookCaseResponse(2L);
		List<BookCaseResponse> bookCases = new ArrayList<>(List.of(bookCaseResponse1, bookCaseResponse2));
		BookCaseResponses bookCaseResponses = new BookCaseResponses(bookCases);

		given(bookCaseService.findBookCases(any())).willReturn(bookCaseResponses);

		// when
		ResultActions resultActions = mockMvc.perform(
			get("/api/v1/book/{bookId}/bookcase", 1)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(objectMapper.writeValueAsString(bookCaseResponses)))
			.andDo(document(COMMON_DOCS_NAME,
				pathParameters(
					parameterWithName("bookId").description("????????? ?????????(??????) ?????????")
				),
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				responseFields(
					fieldWithPath("bookCases").type(ARRAY).description("?????????(??????) ?????????"),
					fieldWithPath("bookCases.[].id").type(NUMBER).description("?????????(??????) ?????????"),
					fieldWithPath("bookCases.[].income").type(NUMBER).description("?????? ??????"),
					fieldWithPath("bookCases.[].outcome").type(NUMBER).description("?????? ??????"),
					fieldWithPath("bookCases.[].title").type(STRING).description("??????"),
					fieldWithPath("bookCases.[].place").type(STRING).description("??????"),
					fieldWithPath("bookCases.[].memo").type(STRING).description("??????")
				)));
	}

	@Test
	@DisplayName("/api/v1/book/{bookId}/bookcase ?????? ?????????(???)??? ????????????")
	void postBookCase() throws Exception {
		// given
		BookCaseRegisterRequest bookCaseRegisterRequest = BookCaseRegisterRequest.builder()
			.income(10000L)
			.outcome(5000L)
			.title("one coffee")
			.place("mega coffee")
			.memo("memo")
			.build();
		BookCaseResponse bookCaseResponse = getBookCaseResponse(1L);
		given(bookCaseService.register(any(), any())).willReturn(bookCaseResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/v1/book/{bookId}/bookcase", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookCaseRegisterRequest)));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().json(objectMapper.writeValueAsString(bookCaseResponse)))
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				pathParameters(
					parameterWithName("bookId").description("????????? ?????????(??????) ?????????")
				),
				requestFields(
					fieldWithPath("income").type(NUMBER).description("?????? ??????"),
					fieldWithPath("outcome").type(NUMBER).description("?????? ??????"),
					fieldWithPath("title").type(STRING).description("??????"),
					fieldWithPath("place").type(STRING).description("??????"),
					fieldWithPath("memo").type(STRING).description("??????")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("?????????(??????) ?????????"),
					fieldWithPath("income").type(NUMBER).description("?????? ??????"),
					fieldWithPath("outcome").type(NUMBER).description("?????? ??????"),
					fieldWithPath("title").type(STRING).description("??????"),
					fieldWithPath("place").type(STRING).description("??????"),
					fieldWithPath("memo").type(STRING).description("??????")
				)));
	}

	@Test
	@DisplayName("/api/v1/book/{bookId}/bookcase ?????? ?????????(??????)??? ????????????")
	void patchBookCase() throws Exception {
		// given
		BookCaseUpdateRequest bookCaseUpdateRequest = BookCaseUpdateRequest.builder()
			.id(1L)
			.income(10000L)
			.outcome(5000L)
			.title("one coffee")
			.place("mega coffee")
			.memo("memo")
			.build();
		BookCaseResponse bookCaseResponse = getBookCaseResponse(1L);
		given(bookCaseService.update(any())).willReturn(bookCaseResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			patch("/api/v1/book/{bookId}/bookcase", 1)
				.content(objectMapper.writeValueAsString(bookCaseUpdateRequest))
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().json(objectMapper.writeValueAsString(bookCaseResponse)))
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				pathParameters(
					parameterWithName("bookId").description("????????? ?????????(??????) ?????????")
				),
				requestFields(
					fieldWithPath("id").type(NUMBER).description("?????????(??????) ?????????"),
					fieldWithPath("income").type(NUMBER).description("?????? ??????"),
					fieldWithPath("outcome").type(NUMBER).description("?????? ??????"),
					fieldWithPath("title").type(STRING).description("??????"),
					fieldWithPath("place").type(STRING).description("??????"),
					fieldWithPath("memo").type(STRING).description("??????")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("?????????(??????) ?????????"),
					fieldWithPath("income").type(NUMBER).description("?????? ??????"),
					fieldWithPath("outcome").type(NUMBER).description("?????? ??????"),
					fieldWithPath("title").type(STRING).description("??????"),
					fieldWithPath("place").type(STRING).description("??????"),
					fieldWithPath("memo").type(STRING).description("??????")
				)));
	}

	@Test
	@DisplayName("/api/v1/book/{bookId}/bookcase ?????? ?????????(??????)??? ????????????")
	void deleteBookCase() throws Exception {
		// given
		BookCaseDeleteRequest bookCaseDeleteRequest = new BookCaseDeleteRequest(1L);
		BookCaseResponse bookCaseResponse = getBookCaseResponse(1L);
		given(bookCaseService.delete(any())).willReturn(bookCaseResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			delete("/api/v1/book/{bookId}/bookcase", 1)
				.content(objectMapper.writeValueAsString(bookCaseDeleteRequest))
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().json(objectMapper.writeValueAsString(bookCaseResponse)))
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				pathParameters(
					parameterWithName("bookId").description("????????? ?????????(??????) ?????????")
				),
				requestFields(
					fieldWithPath("id").type(NUMBER).description("?????????(??????) ?????????")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("?????????(??????) ?????????"),
					fieldWithPath("income").type(NUMBER).description("?????? ??????"),
					fieldWithPath("outcome").type(NUMBER).description("?????? ??????"),
					fieldWithPath("title").type(STRING).description("??????"),
					fieldWithPath("place").type(STRING).description("??????"),
					fieldWithPath("memo").type(STRING).description("??????")
				)));
	}

	@Test
	@DisplayName("/api/v1/book/{bookId}/bookcase/is-deleted ?????? ????????? ?????????(??????)??? ?????? ????????????.")
	void getDeletedBookCases() throws Exception {
		// given
		BookCaseResponse bookCaseResponse1 = getBookCaseResponse(1L);
		BookCaseResponse bookCaseResponse2 = getBookCaseResponse(2L);
		List<BookCaseResponse> bookCases = new ArrayList<>(List.of(bookCaseResponse1, bookCaseResponse2));
		BookCaseResponses bookCaseResponses = new BookCaseResponses(bookCases);

		given(bookCaseService.findDeletedBookCases(any())).willReturn(bookCaseResponses);

		// when
		ResultActions resultActions = mockMvc.perform(
			get("/api/v1/book/{bookId}/bookcase/is-deleted", 1)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(objectMapper.writeValueAsString(bookCaseResponses)))
			.andDo(document(COMMON_DOCS_NAME,
				pathParameters(
					parameterWithName("bookId").description("????????? ?????????(??????) ?????????")
				),
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				responseFields(
					fieldWithPath("bookCases").type(ARRAY).description("?????????(??????) ?????????"),
					fieldWithPath("bookCases.[].id").type(NUMBER).description("?????????(??????) ?????????"),
					fieldWithPath("bookCases.[].income").type(NUMBER).description("?????? ??????"),
					fieldWithPath("bookCases.[].outcome").type(NUMBER).description("?????? ??????"),
					fieldWithPath("bookCases.[].title").type(STRING).description("??????"),
					fieldWithPath("bookCases.[].place").type(STRING).description("??????"),
					fieldWithPath("bookCases.[].memo").type(STRING).description("??????")
				)));
	}

	@Test
	@DisplayName("/api/v1/book/{bookId}//bookcase/is-deleted ?????? ????????? ?????????(??????)??? ????????????")
	void patchDeletedBookCase() throws Exception {
		// given
		BookCaseReviveRequest bookCaseReviveRequest = new BookCaseReviveRequest(1L);
		BookCaseResponse bookCaseResponse = getBookCaseResponse(1L);
		given(bookCaseService.revive(any())).willReturn(bookCaseResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			patch("/api/v1/book/{bookId}/bookcase/is-deleted", 1)
				.content(objectMapper.writeValueAsString(bookCaseReviveRequest))
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().json(objectMapper.writeValueAsString(bookCaseResponse)))
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				pathParameters(
					parameterWithName("bookId").description("????????? ?????????(??????) ?????????")
				),
				requestFields(
					fieldWithPath("id").type(NUMBER).description("?????????(??????) ?????????")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json ?????? ??????")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("?????????(??????) ?????????"),
					fieldWithPath("income").type(NUMBER).description("?????? ??????"),
					fieldWithPath("outcome").type(NUMBER).description("?????? ??????"),
					fieldWithPath("title").type(STRING).description("??????"),
					fieldWithPath("place").type(STRING).description("??????"),
					fieldWithPath("memo").type(STRING).description("??????")
				)));
	}

	private BookCaseResponse getBookCaseResponse(Long id) {
		return BookCaseResponse.builder()
			.id(id)
			.income(10000L)
			.outcome(5000L)
			.title("one coffee")
			.place("mega coffee")
			.memo("memo")
			.build();
	}
}