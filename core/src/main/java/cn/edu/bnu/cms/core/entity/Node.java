package cn.edu.bnu.cms.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by dave on 16/7/12.
 */
@Entity
@Table(name = "t_node")
public class Node implements Serializable {
    private static final long serialVersionUID = 1842563435266074465L;

    /**
     * 节点类型: 容器类型, 单页面类型, 链接类型
     */
    public enum NodeType {
        CONTAINER(10), PAGE(20), LINK(30);

        private int code;

        NodeType(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static NodeType parse(int code) {
            for (NodeType t : NodeType.values()) {
                if (t.getCode() == code) {
                    return t;
                }
            }
            return null;
        }
    }

    /**
     * 节点稿件类型: 文章, 组图, 视频, 下载
     */
    public enum ContentModel {
        ARTICLE(10), PHOTO(20), VEDIO(30), DOWNLOAD(40);

        private int code;

        ContentModel(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static ContentModel parse(int code) {
            for (ContentModel t : ContentModel.values()) {
                if (t.getCode() == code) {
                    return t;
                }
            }
            return null;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String engName; // 英文表示, 用于URL中

    @Column private String title;

    @Column private int channelId; // 所属频道ID。 频道也是一个栏目节点, 是栏目节点树的根节点

    @Column private int rootId; // 所属根节点ID

    @Column private int parentId; // 父节点ID

    @Column private int leftVal; // 预排序遍历树左值

    @Column private int rightVal; // 预排序遍历树右值

    @Column private int level; // 节点在节点树中所在的层级

    @Column private int order; // 节点在兄弟节点中的排序

    @Column private int type; // 节点类型, 10为容器栏目，20为单页节点，30为外部链接

    @Column private int itemModelType; // 栏目稿件内容模型类型, 10: 文章类型, 20: 组图, 30: 视频, 40: 下载类型

    @Column private String parentPath; // 导航路径, 格式如: node名:node英文名;历史:history;欧洲史:euro-history

    @Column private String seoKeywords; // 本栏目SEO关键字,不要重复全站SEO关键字

    @Column private String seoDescription; // 本栏目SEO描述文本

    /**
     * 本栏目稿件默认阅读权限。
     * 0:完全开放查看, 10: 会员查看, 20: 仅限作者, 30: 仅限好友, >=100: 会员等级至少达到此值
     */
    @Column private int readPermission;

    /**
     * 本栏目投稿权限。
     * 0: 仅限本栏目编辑或系统管理员投稿, 10: 会员可投稿, >=100: 会员等级至少达到此值
     */
    @Column private int contributePermission;

    @Column private String linkUrl; // 当节点是外部链接时, 外部链接URL

    @Column private String uploadDir; // 上传目录, 默认为栏目英文名

    @Column private String indexTemplate; // 栏目页模板

    @Column private String itemTemplate; // 本栏目稿件模板

    @Column private Date createdAt;

    @Column private Date updatedAt;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getRootId() {
        return rootId;
    }

    public void setRootId(int rootId) {
        this.rootId = rootId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getLeftVal() {
        return leftVal;
    }

    public void setLeftVal(int leftVal) {
        this.leftVal = leftVal;
    }

    public int getRightVal() {
        return rightVal;
    }

    public void setRightVal(int rightVal) {
        this.rightVal = rightVal;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getItemModelType() {
        return itemModelType;
    }

    public void setItemModelType(int itemModelType) {
        this.itemModelType = itemModelType;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public int getReadPermission() {
        return readPermission;
    }

    public void setReadPermission(int readPermission) {
        this.readPermission = readPermission;
    }

    public int getContributePermission() {
        return contributePermission;
    }

    public void setContributePermission(int contributePermission) {
        this.contributePermission = contributePermission;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getIndexTemplate() {
        return indexTemplate;
    }

    public void setIndexTemplate(String indexTemplate) {
        this.indexTemplate = indexTemplate;
    }

    public String getItemTemplate() {
        return itemTemplate;
    }

    public void setItemTemplate(String itemTemplate) {
        this.itemTemplate = itemTemplate;
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
    public NodeType getTypeNode() {
        return NodeType.parse(this.type);
    }

    public void setTypeNode(NodeType t) {
        this.type = t.getCode();
    }

    @JsonIgnore
    public ContentModel getContentModel() {
        return ContentModel.parse(this.itemModelType);
    }

    public void setContentModel(ContentModel t) {
        this.itemModelType = t.getCode();
    }
}
