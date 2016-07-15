package cn.edu.bnu.cms.core.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dave on 16/7/12.
 */
@Entity
@Table(name = "t_article")
public class Article implements Serializable {
    private static final long serialVersionUID = 1684328549624882655L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String author;

    @Column private String copyFrom;

    @Column private String referUrl;

    @Lob
    @Column private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCopyFrom() {
        return copyFrom;
    }

    public void setCopyFrom(String copyFrom) {
        this.copyFrom = copyFrom;
    }

    public String getReferUrl() {
        return referUrl;
    }

    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
