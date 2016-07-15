package cn.edu.bnu.cms.core.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dave on 16/7/13.
 */

public class VedioClip implements Serializable {
    private static final long serialVersionUID = 2312881049871140052L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String url;

    @Column private String format;

    @Column private int size;
}
