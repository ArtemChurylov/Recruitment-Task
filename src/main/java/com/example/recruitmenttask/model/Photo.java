package com.example.recruitmenttask.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "photo_storage")
public class Photo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_id")
    @SequenceGenerator(name = "photo_id", sequenceName = "photo_id", allocationSize = 1)
    private Long id;

    @NotEmpty
    @Size(min = 3, max = 40, message = "Size should be between 3 and 40 characters.")
    @Column(name = "title")
    private String title;

    @Column(name = "file_name")
    private String fileName;

    public Photo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
