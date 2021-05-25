package com.wikigreen.dto;

import com.wikigreen.model.FileStatus;

import java.util.Date;

public class FileDTO {
    private Long id;
    private String fileName;
    private Long fileSizeInBits;
    private UserDTO owner;
    private FileStatus status;
    private Date uploadDate;
    private Date updateDate;

    public FileDTO(String fileName, Long fileSizeInBits, UserDTO owner, FileStatus status, Date uploadDate, Date updateDate) {
        this.fileName = fileName;
        this.fileSizeInBits = fileSizeInBits;
        this.owner = owner;
        this.status = status;
        this.uploadDate = uploadDate;
        this.updateDate = updateDate;
    }

    public FileDTO(Long id, String fileName, Long fileSizeInBits, UserDTO owner, FileStatus status, Date uploadDate, Date updateDate) {
        this.id = id;
        this.fileName = fileName;
        this.fileSizeInBits = fileSizeInBits;
        this.owner = owner;
        this.status = status;
        this.uploadDate = uploadDate;
        this.updateDate = updateDate;
    }

    public FileDTO() {
    }

    public FileDTO(Long id, String fileName, Long fileSizeInBits, UserDTO owner) {
        this.id = id;
        this.fileName = fileName;
        this.fileSizeInBits = fileSizeInBits;
        this.owner = owner;
        this.status = FileStatus.ACTIVE;
        this.uploadDate = new Date();
        this.updateDate = new Date();
    }

    public FileDTO(String fileName, Long fileSizeInBits, UserDTO owner) {
        this.fileName = fileName;
        this.fileSizeInBits = fileSizeInBits;
        this.owner = owner;
        this.status = FileStatus.ACTIVE;
        this.uploadDate = new Date();
        this.updateDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSizeInBits() {
        return fileSizeInBits;
    }

    public void setFileSizeInBits(Long fileSizeInBits) {
        this.fileSizeInBits = fileSizeInBits;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public FileStatus getStatus() {
        return status;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileDTO fileDTO = (FileDTO) o;

        if (!id.equals(fileDTO.id)) return false;
        if (!fileName.equals(fileDTO.fileName)) return false;
        if (!fileSizeInBits.equals(fileDTO.fileSizeInBits)) return false;
        if (!owner.equals(fileDTO.owner)) return false;
        if (status != fileDTO.status) return false;
        if (!uploadDate.equals(fileDTO.uploadDate)) return false;
        return updateDate.equals(fileDTO.updateDate);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + fileName.hashCode();
        result = 31 * result + fileSizeInBits.hashCode();
        result = 31 * result + owner.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + uploadDate.hashCode();
        result = 31 * result + updateDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FileDTO{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileSizeInBits=" + fileSizeInBits +
                ", owner=" + owner +
                ", status=" + status +
                ", uploadDate=" + uploadDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
