package com.niolikon.taskboard.framework.data.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PageResponseTest {
    private static final int SAMPLE1_QUERY_PAGE_NUMBER = 1;
    private static final int SAMPLE1_QUERY_PAGE_SIZE = 4;
    private static final Pageable SAMPLE1_QUERY_PAGEABLE = PageRequest.of(SAMPLE1_QUERY_PAGE_NUMBER, SAMPLE1_QUERY_PAGE_SIZE);

    private static final long SAMPLE1_DATA_ELEMENTS_TOTAL = 10;
    private static final List<String> SAMPLE1_RESULT_DATA = List.of("A", "B", "C", "D");
    private static final int SAMPLE1_DATA_PAGE_TOTAL = 3;

    @Test
    void givenPagedData_whenConstructing_thenInstanceIsConstructedProperly() {
        List<String> data = List.of("A", "B", "C", "D");
        Page<String> pagedData = new PageImpl<>(SAMPLE1_RESULT_DATA, SAMPLE1_QUERY_PAGEABLE, SAMPLE1_DATA_ELEMENTS_TOTAL);

        PageResponse<String> response = new PageResponse<>(pagedData);

        assertThat(response)
                .extracting(
                        PageResponse::getContent,
                        PageResponse::getElementsSize,
                        PageResponse::getElementsTotal,
                        PageResponse::getPageNumber,
                        PageResponse::getPageSize,
                        PageResponse::getPageTotal,
                        PageResponse::isFirst,
                        PageResponse::isLast,
                        PageResponse::isEmpty
                )
                .containsExactly(
                        data,
                        data.size(),
                        SAMPLE1_DATA_ELEMENTS_TOTAL,
                        SAMPLE1_QUERY_PAGE_NUMBER,
                        SAMPLE1_QUERY_PAGE_SIZE,
                        SAMPLE1_DATA_PAGE_TOTAL,
                        Boolean.FALSE,
                        Boolean.FALSE,
                        Boolean.FALSE
                );
    }

    @Test
    void givenJsonString_whenDeserialization_thenInstanceIsDeserializedProperly() throws IOException {
        String jsonResponse = """
                {
                  "content": ["Alpha", "Beta", "Gamma"],
                  "elementsSize": 3,
                  "elementsTotal": 10,
                  "pageNumber": 0,
                  "pageSize": 3,
                  "pageTotal": 4,
                  "first": true,
                  "last": false,
                  "empty": false
                }
        """;

        ObjectMapper objectMapper = new ObjectMapper();
        PageResponse<String> deserializedResponse = objectMapper.readValue(
                jsonResponse,
                new TypeReference<>() {}
        );

        assertThat(deserializedResponse)
                .isNotNull()
                .extracting(
                        PageResponse::getContent,
                        PageResponse::getElementsSize,
                        PageResponse::getElementsTotal,
                        PageResponse::getPageNumber,
                        PageResponse::getPageSize,
                        PageResponse::getPageTotal,
                        PageResponse::isFirst,
                        PageResponse::isLast,
                        PageResponse::isEmpty
                )
                .containsExactly(
                        List.of("Alpha", "Beta", "Gamma"),
                        3,
                        10L,
                        0,
                        3,
                        4,
                        Boolean.TRUE,
                        Boolean.FALSE,
                        Boolean.FALSE
                );
    }
}
