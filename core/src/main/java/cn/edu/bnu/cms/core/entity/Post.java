package cn.edu.bnu.cms.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by dave on 16/7/12.
 */
@Entity
@Table(name = "t_post")
public class Post implements Serializable {
    private static final long serialVersionUID = -6077733725384900206L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String title;

    @Column private int nodeId; // 所属栏目ID

    @Column private int creatorId; // 创建者ID

    @Column private String creatorName; // 创建者名称

    @Column private String tags; // 文章标签, 用逗号隔开

    @Column private int modelType; // 稿件内容类型, 10: 文章类型, 20: 组图, 30: 视频, 40: 下载类型

    @Column private int status; // 稿件状态, -3为删除，-2为退稿，-1为草稿，0为待审核，99为终审通过，其它为自定义

    @Column private int eliteLevel; // 推荐类型

    /**
     * 稿件阅读权限。
     * 0:完全开放查看, 10: 会员查看, 20: 仅限作者, 30: 仅限好友, >=100: 会员等级至少达到此值
     */
    @Column private int readPermission;

    @Column private String thumbnail; // 稿件缩略图

    @Column private String excerpt; // 内容摘要

    @Column private int likeCount; // 点赞次数

    @Column private int starCount; // 评星级次数

    @Column private double starRate; // 平均星级

    @Column private boolean hasAttachment; // 是否有附件

    @Column private int commentAudited; // 已审核评论数

    @Column private int commentUnaudited; // 未审核评论数

    @Column private Date createdAt;

    @Column private Date updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEliteLevel() {
        return eliteLevel;
    }

    public void setEliteLevel(int eliteLevel) {
        this.eliteLevel = eliteLevel;
    }

    public int getReadPermission() {
        return readPermission;
    }

    public void setReadPermission(int readPermission) {
        this.readPermission = readPermission;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public double getStarRate() {
        return starRate;
    }

    public void setStarRate(double starRate) {
        this.starRate = starRate;
    }

    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public int getCommentAudited() {
        return commentAudited;
    }

    public void setCommentAudited(int commentAudited) {
        this.commentAudited = commentAudited;
    }

    public int getCommentUnaudited() {
        return commentUnaudited;
    }

    public void setCommentUnaudited(int commentUnaudited) {
        this.commentUnaudited = commentUnaudited;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonIgnore
    public Node.ContentModel getContentModel() {
        return Node.ContentModel.parse(this.modelType);
    }

    public void setContentModel(Node.ContentModel t) {
        this.modelType = t.getCode();
    }

}
