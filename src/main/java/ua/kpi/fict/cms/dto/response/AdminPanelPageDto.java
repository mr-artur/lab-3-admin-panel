package ua.kpi.fict.cms.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminPanelPageDto {

    private String header;

    private String content;

    private String footer;
}
