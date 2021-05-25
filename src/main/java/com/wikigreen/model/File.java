package com.wikigreen.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size_in_bits")
    private Long fileSizeInBits;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User owner;

    @Column(name = "file_status")
    @Enumerated(value = EnumType.STRING)
    private FileStatus status;

    @Column(name = "upload_date")
    private Date uploadDate;

    @Column(name = "update_date")
    private Date updateDate;

    public File(Long id, String fileName, Long fileSizeInBits, User owner, FileStatus status, Date uploadDate, Date updateDate) {
        this.id = id;
        this.fileName = fileName;
        this.fileSizeInBits = fileSizeInBits;
        this.owner = owner;
        this.status = status;
        this.uploadDate = uploadDate;
        this.updateDate = updateDate;
    }

    public File(String fileName, Long fileSizeInBits, User owner, FileStatus status, Date uploadDate, Date updateDate) {
        this.fileName = fileName;
        this.fileSizeInBits = fileSizeInBits;
        this.owner = owner;
        this.status = status;
        this.uploadDate = uploadDate;
        this.updateDate = updateDate;
    }

    public File() {
    }

    public File(String fileName, Long fileSizeInBits, User owner) {
        this.fileName = fileName;
        this.fileSizeInBits = fileSizeInBits;
        this.owner = owner;
        this.uploadDate = new Date();
        this.updateDate = this.uploadDate;
        status = FileStatus.ACTIVE;
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

    public void setFileSizeInBits(long fileSizeInBits) {
        this.fileSizeInBits = fileSizeInBits;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
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

        File file = (File) o;

        if (fileSizeInBits != file.fileSizeInBits) return false;
        if (!Objects.equals(id, file.id)) return false;
        if (!Objects.equals(fileName, file.fileName)) return false;
        if (!Objects.equals(owner.getId(), file.owner.getId())) return false;
        if (status != file.status) return false;
        if (!Objects.equals(uploadDate, file.uploadDate)) return false;
        return Objects.equals(updateDate, file.updateDate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (int) (fileSizeInBits ^ (fileSizeInBits >>> 32));
        result = 31 * result + (owner.getId() != null ? owner.getId().hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (uploadDate != null ? uploadDate.hashCode() : 0);
        result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + fileName + '\'' +
                ", fileSizeInBits=" + fileSizeInBits +
                ", owner="  + " id: " + owner.getId() + " " + owner.getFirstName() + " " + owner.getLastName() +
                ", status=" + status +
                ", uploadDate=" + uploadDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
