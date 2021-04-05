package ua.kpi.fict.cms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.NaturalId;

import ua.kpi.fict.cms.entity.enums.ContainerType;
import ua.kpi.fict.cms.entity.enums.Language;
import ua.kpi.fict.cms.entity.enums.OrderType;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pages")
public class Page implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pages_generator")
    @SequenceGenerator(name = "pages_generator", sequenceName = "pages_sequence")
    private Long id;
    /*
     * unique code of page
     */
    @NaturalId
    @Column(unique = true, nullable = false)
    private String code;
    /*
     * title, h1
     */
    @Column(name = "caption_ua", nullable = false)
    private String captionUa;

    @Column(name = "caption_en", nullable = false)
    private String captionEn;
    /*
     * short description
     */
    @Column(name = "intro_ua", nullable = false)
    private String introUa;

    @Column(name = "intro_en", nullable = false)
    private String introEn;

    @Column(name = "content_ua", length = 2000, nullable = false)
    private String contentUa;

    @Column(name = "content_en", length = 2000, nullable = false)
    private String contentEn;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "update_date", nullable = false)
    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "parent_code", referencedColumnName = "code")
    private Page parentPage;

    @OneToMany(mappedBy = "parentPage", cascade = CascadeType.ALL)
    private List<Page> childPages = new ArrayList<>();
    /*
     * to determine current page position in parent container
     */
    @Column(name = "order_num")
    private Integer orderNum;
    /*
     * to determine order type of children in current container
     */
    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "container_type")
    @Enumerated(EnumType.STRING)
    private ContainerType containerType;

    @ManyToOne
    @JoinColumn(name = "alias_of", referencedColumnName = "code")
    private Page aliasOf;

    public String getCode(Language language) {
        return language == Language.UA
                ? "" + code
                : "/en/" + code;
    }

    public String getCaption(Language language) {
        return language == Language.UA
                ? captionUa
                : captionEn;
    }

    public String getIntro(Language language) {
        return language == Language.UA
                ? introUa
                : introEn;
    }

    public String getContent(Language language) {
        return language == Language.UA
                ? contentUa
                : contentEn;
    }
}
