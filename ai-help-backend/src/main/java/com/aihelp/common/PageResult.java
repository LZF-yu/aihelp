package com.aihelp.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private long total;
    private List<T> records;
    private long current;
    private long size;

    public static <T> PageResult<T> of(long total, List<T> records, long current, long size) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setRecords(records);
        result.setCurrent(current);
        result.setSize(size);
        return result;
    }
}
