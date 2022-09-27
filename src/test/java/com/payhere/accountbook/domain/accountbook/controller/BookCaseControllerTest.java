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
import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseUpdateRequest;
import com.payhere.accountbook.domain.accountbook.service.BookCaseService;
import com.payhere.accountbook.domain.accountbook.service.dto.BookCaseResponse;

@WebMvcTest(controllers = BookCaseController.class)
class BookCaseControllerTest extends ControllerTestConfig {
	@MockBean
	BookCaseService bookCaseService;

	@Test
	@DisplayName("/api/v1/bookcase/{bookCaseId} 에서 가계부(단건)을 조회한다.")
	void getBookCase() throws Exception {
		// given
		BookCaseResponse bookCaseResponse = getBookCaseResponse();
		given(bookCaseService.find(anyLong())).willReturn(bookCaseResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			get("/api/v1/bookcase/{bookCaseId}", 1)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(objectMapper.writeValueAsString(bookCaseResponse)))
			.andDo(print())
			.andDo(document(COMMON_DOCS_NAME,
				pathParameters(
					parameterWithName("bookCaseId").description("요청할 가계부(단건) 아이디")
				),
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("가계부(단건) 아이디"),
					fieldWithPath("income").type(NUMBER).description("단건 수입"),
					fieldWithPath("outcome").type(NUMBER).description("단건 지출"),
					fieldWithPath("title").type(STRING).description("제목"),
					fieldWithPath("place").type(STRING).description("장소")
				)));
	}

	@Test
	@DisplayName("/api/v1/bookcase 에서 가계부(일)을 생성한다")
	void postBookCase() throws Exception {
		// given
		BookCaseRegisterRequest bookCaseRegisterRequest = BookCaseRegisterRequest.builder()
			.bookId(1L)
			.income(10000L)
			.outcome(5000L)
			.title("one coffee")
			.place("mega coffee")
			.build();
		BookCaseResponse bookCaseResponse = getBookCaseResponse();
		given(bookCaseService.register(any())).willReturn(bookCaseResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/v1/bookcase")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookCaseRegisterRequest)));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().json(objectMapper.writeValueAsString(bookCaseResponse)))
			.andDo(print())
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				requestFields(
					fieldWithPath("bookId").type(NUMBER).description("가계부(일일) 아이디"),
					fieldWithPath("income").type(NUMBER).description("단건 수입"),
					fieldWithPath("outcome").type(NUMBER).description("단건 지출"),
					fieldWithPath("title").type(STRING).description("제목"),
					fieldWithPath("place").type(STRING).description("장소")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("가계부(단건) 아이디"),
					fieldWithPath("income").type(NUMBER).description("단건 수입"),
					fieldWithPath("outcome").type(NUMBER).description("단건 지출"),
					fieldWithPath("title").type(STRING).description("제목"),
					fieldWithPath("place").type(STRING).description("장소")
				)));
	}

	@Test
	@DisplayName("/api/v1/bookcase 에서 가계부(단건)을 수정한다")
	void patchBookCase() throws Exception {
		// given
		BookCaseUpdateRequest bookCaseUpdateRequest = BookCaseUpdateRequest.builder()
			.bookId(1L)
			.id(1L)
			.income(10000L)
			.outcome(5000L)
			.title("one coffee")
			.place("mega coffee")
			.build();
		BookCaseResponse bookCaseResponse = getBookCaseResponse();
		given(bookCaseService.update(any())).willReturn(bookCaseResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			patch("/api/v1/bookcase")
				.content(objectMapper.writeValueAsString(bookCaseUpdateRequest))
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().json(objectMapper.writeValueAsString(bookCaseResponse)))
			.andDo(print())
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				requestFields(
					fieldWithPath("bookId").type(NUMBER).description("가계부(일일) 아이디"),
					fieldWithPath("id").type(NUMBER).description("가계부(단건) 아이디"),
					fieldWithPath("income").type(NUMBER).description("단건 수입"),
					fieldWithPath("outcome").type(NUMBER).description("단건 지출"),
					fieldWithPath("title").type(STRING).description("제목"),
					fieldWithPath("place").type(STRING).description("장소")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("가계부(단건) 아이디"),
					fieldWithPath("income").type(NUMBER).description("단건 수입"),
					fieldWithPath("outcome").type(NUMBER).description("단건 지출"),
					fieldWithPath("title").type(STRING).description("제목"),
					fieldWithPath("place").type(STRING).description("장소")
				)));
	}

	@Test
	@DisplayName("/api/v1/bookcase 에서 가계부(단건)을 삭제한다")
	void deleteBookCase() throws Exception {
		// given
		BookCaseDeleteRequest bookCaseDeleteRequest = new BookCaseDeleteRequest(1L,1L);
		BookCaseResponse bookCaseResponse = getBookCaseResponse();
		given(bookCaseService.delete(any())).willReturn(bookCaseResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			delete("/api/v1/bookcase")
				.content(objectMapper.writeValueAsString(bookCaseDeleteRequest))
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().json(objectMapper.writeValueAsString(bookCaseResponse)))
			.andDo(print())
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				requestFields(
					fieldWithPath("bookId").type(NUMBER).description("가계부(일일) 아이디"),
					fieldWithPath("id").type(NUMBER).description("가계부(단건) 아이디")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("가계부(단건) 아이디"),
					fieldWithPath("income").type(NUMBER).description("단건 수입"),
					fieldWithPath("outcome").type(NUMBER).description("단건 지출"),
					fieldWithPath("title").type(STRING).description("제목"),
					fieldWithPath("place").type(STRING).description("장소")
				)));
	}

	private BookCaseResponse getBookCaseResponse() {
		return BookCaseResponse.builder()
			.id(1L)
			.income(10000L)
			.outcome(5000L)
			.title("one coffee")
			.place("mega coffee")
			.build();
	}
}