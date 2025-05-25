package com.project.musicwebbe.util;

import org.springframework.data.domain.Sort;

public interface SortList {
    /**
     * Creates a Sort.Order instance based on the given sort field and direction.
     *
     * @param sortBy       The field to sort by.
     * @param sortDirection The sort direction ('asc' or 'desc').
     * @return A Sort.Order instance representing the sort criteria.
     */
    static Sort.Order createSortOrder(String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        return new Sort.Order(direction, sortBy);
    }
}
