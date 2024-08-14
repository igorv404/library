package io.igorv404.library.dto.response;

import java.util.List;

public record MemberBorrowedBooksDto (Integer id, String name, List<String> borrowedBooks) {}
