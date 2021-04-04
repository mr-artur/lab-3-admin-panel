package ua.kpi.fict.cms.entity.enums;

import ua.kpi.fict.cms.entity.Page;

import java.util.Comparator;

public enum OrderType {

    CREATION_DATE(
            (page1, page2) -> page1.getCreationDate().compareTo(page2.getCreationDate())
    ),

    UPDATE_DATE(
            (page1, page2) -> page1.getUpdateDate().compareTo(page2.getUpdateDate())
    ),

    DEFAULT(
            (page1, page2) -> page1.getOrderNum().compareTo(page2.getOrderNum())
    );

    private final Comparator<Page> comparator;

    OrderType(Comparator<Page> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Page> getComparator() {
        return comparator;
    }
}