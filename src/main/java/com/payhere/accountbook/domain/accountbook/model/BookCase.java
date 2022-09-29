package com.payhere.accountbook.domain.accountbook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.payhere.accountbook.global.base.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE book_case SET is_delete = true WHERE book_case_id = ?")
@Where(clause = "is_delete = false")
public class BookCase extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_case_id", unique = true, nullable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY) // n:1 매핑
	@JoinColumn(name = "book_id")
	private Book book;

	@Column(name = "income", nullable = false)
	private Long income;

	@Column(name = "outcome", nullable = false)
	private Long outcome;

	@Column(name = "title", length = 32, nullable = false)
	private String title;

	@Column(name = "place", length = 128, nullable = false)
	private String place;

	@Column(name = "memo", length = 256)
	private String memo;

	@Column(name = "is_delete")
	private boolean isDelete = Boolean.FALSE;

	@Builder
	public BookCase(Book book, Long income, Long outcome, String title, String place, String memo) {
		this.book = book;
		this.income = income;
		this.outcome = outcome;
		this.title = title;
		this.place = place;
		this.memo = memo;
	}

	public void change(Long income, Long outcome, String title, String place, String memo) {
		this.income = income;
		this.outcome = outcome;
		this.title = title;
		this.place = place;
		this.memo = memo;
	}

	public void revive() {
		this.isDelete = Boolean.FALSE;
	}
}
