package ua.kpi.fict.cms.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {

    private String meta;

    private String header;

    private String subheader;

    private String title;

    private String imageUrl;

    private String content;

    private String footer;
}
