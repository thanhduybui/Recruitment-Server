package com.edu.hcmute.response;

import lombok.Builder;
import lombok.Data;



@Builder
@Data
public class PagingResponseData {
    private Object listData;
    private Integer totalPage;
    private Long totalItem;
    private Integer currentPage;
    private Integer pageSize;
}
