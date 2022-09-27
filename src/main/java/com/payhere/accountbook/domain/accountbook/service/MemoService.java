package com.payhere.accountbook.domain.accountbook.service;

import org.springframework.stereotype.Service;

import com.payhere.accountbook.domain.accountbook.repository.MemoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoService {
	private final MemoRepository memoRepository;
}
