package com.payhere.accountbook.domain.accountbook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.payhere.accountbook.domain.member.model.Member;
import com.payhere.accountbook.global.base.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "book_id", unique = true, nullable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY) // n:1 매핑
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "year", nullable = false)
	private Long year;

	@Column(name = "month", nullable = false)
	private Long month;

	@Column(name = "day", nullable = false)
	private Long day;

	@Column(name = "income", nullable = false)
	private Long income;

	@Column(name = "outcome", nullable = false)
	private Long outcome;

	@Column(name = "title", length = 32, nullable = false)
	private String title;

	@Column(name = "is_delete")
	private boolean isDelete;

	@Builder
	public Book(Member member, Long year, Long month, Long day, String title) {
		this.member = member;
		this.year = year;
		this.month = month;
		this.day = day;
		this.income = 0L;
		this.outcome = 0L;
		this.title = title;
		this.isDelete = false;
	}

	public void changeTitle(String title){
		this.title = title;
	}

	public void changeIncomeAndOutcome(Long income, Long outcome) {
		this.income += income;
		this.outcome += outcome;
	}
}
