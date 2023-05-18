package com.baihe.lihepro.entity;

/**
 * Author：xubo
 * Time：2020-09-10
 * Description：
 */
public class AttachmentEntity {
    public static final int TYPE_LOCAL = 1;
    public static final int TYPE_REMOTE = 2;

    private String url;
    private int type;

    public static AttachmentEntity createLocal(String url) {
        AttachmentEntity attachmentEntity = new AttachmentEntity();
        attachmentEntity.setType(TYPE_LOCAL);
        attachmentEntity.setUrl(url);
        return attachmentEntity;
    }

    public static AttachmentEntity createRemote(String url) {
        AttachmentEntity attachmentEntity = new AttachmentEntity();
        attachmentEntity.setType(TYPE_REMOTE);
        attachmentEntity.setUrl(url);
        return attachmentEntity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
