package com.baihe.http;

import java.io.File;

/**
 * Author：xubo
 * Time：2018-12-28
 * Description：文件上传包
 */
public class UploadFileWrap {
    private File uploadFile;
    private String uploadFileParam;
    private String contentTpye;

    public UploadFileWrap(File uploadFile, String uploadFileParam, String contentTpye) {
        if (uploadFile == null || !uploadFile.exists()) {
            throw new IllegalStateException("UploadFileWrap不能指定空或不存在的上传文件");
        }
        this.uploadFile = uploadFile;
        this.uploadFileParam = uploadFileParam;
        this.contentTpye = contentTpye;
    }

    public UploadFileWrap(String uploadFilePath, String uploadFileParam, String contentTpye) {
        if (uploadFilePath == null || "".equals(uploadFilePath.trim())) {
            throw new IllegalStateException("UploadFileWrap不能指定空文件路径或不存在的上传文件路径");
        }
        File uploadFile = new File(uploadFilePath);
        if (!uploadFile.exists()) {
            throw new IllegalStateException("UploadFileWrap不能指定不存在的上传文件");
        }
        this.uploadFile = uploadFile;
        this.uploadFileParam = uploadFileParam;
        this.contentTpye = contentTpye;
    }

    public UploadFileWrap(File uploadFile) {
        this(uploadFile, "", "application/octet-stream");
    }

    public UploadFileWrap(File uploadFile, String uploadFileParam) {
        this(uploadFile, uploadFileParam, "application/octet-stream");
    }

    public UploadFileWrap(String uploadFilePath) {
        this(uploadFilePath, "", "application/octet-stream");
    }

    public UploadFileWrap(String uploadFilePath, String uploadFileParam) {
        this(uploadFilePath, uploadFileParam, "application/octet-stream");
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        if (uploadFile == null || !uploadFile.exists()) {
            throw new IllegalStateException("UploadFileWrap不能指定空或不存在的上传文件");
        }
        this.uploadFile = uploadFile;
    }

    public String getUploadFileParam() {
        return uploadFileParam;
    }

    public void setUploadFileParam(String uploadFileParam) {
        if (uploadFileParam == null) {
            uploadFileParam = "";
        }
        this.uploadFileParam = uploadFileParam;
    }

    public String getContentTpye() {
        return contentTpye;
    }

    public void setContentTpye(String contentTpye) {
        if (contentTpye == null || "".equals(contentTpye.trim())) {
            contentTpye = "application/octet-stream";
        }
        this.contentTpye = contentTpye;
    }

}
