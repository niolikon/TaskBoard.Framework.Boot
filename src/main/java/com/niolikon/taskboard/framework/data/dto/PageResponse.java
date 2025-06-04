package com.niolikon.taskboard.framework.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PageResponse<T> {
    @JsonProperty("content")
    private List<T> content;

    @JsonProperty("elementsSize")
    private int elementsSize;

    @JsonProperty("elementsTotal")
    private long elementsTotal;


    @JsonProperty("pageNumber")
    private int pageNumber;

    @JsonProperty("pageSize")
    private int pageSize;

    @JsonProperty("pageTotal")
    private int pageTotal;


    @JsonProperty("first")
    private boolean first;

    @JsonProperty("last")
    private boolean last;

    @JsonProperty("empty")
    private boolean empty;


    public PageResponse(Page<T> pagedData) {
        this.content = pagedData.getContent();
        this.elementsSize = pagedData.getNumberOfElements();
        this.elementsTotal = pagedData.getTotalElements();
        this.pageNumber = pagedData.getNumber();
        this.pageSize = pagedData.getSize();
        this.pageTotal = pagedData.getTotalPages();
        this.first = pagedData.isFirst();
        this.last = pagedData.isLast();
        this.empty = pagedData.isEmpty();
    }
}
