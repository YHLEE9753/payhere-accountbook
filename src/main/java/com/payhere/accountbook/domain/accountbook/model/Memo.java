package com.payhere.accountbook.domain.accountbook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.payhere.accountbook.global.base.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "memo_id", unique = true, nullable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY) // n:1 매핑
	@JoinColumn(name = "book_id")
	private Book book;

	@Column(name = "title", length = 32, nullable = false)
	private String title;

	@Column(name = "content", length = 1024, nullable = false)
	private String content;

	@Builder
	public Memo(Book book, String title, String content) {
		this.book = book;
		this.title = title;
		this.content = content;
	}
}
