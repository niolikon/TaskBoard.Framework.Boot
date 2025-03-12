package com.niolikon.taskboard.framework.exceptions;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExceptionHandlerProperties {
    private boolean enabled = false;
}
