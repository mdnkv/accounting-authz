package dev.mednikov.accounting.authz.shared.domain;

public record PaginationRequest(SortingOrder sortingOrder, int page, int count) {

}
