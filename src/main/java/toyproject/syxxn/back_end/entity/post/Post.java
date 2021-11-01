package toyproject.syxxn.back_end.entity.post;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.entity.BaseCreatedAtEntity;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.comment.Comment;
import toyproject.syxxn.back_end.entity.pet.PetImage;
import toyproject.syxxn.back_end.entity.pet.PetInfo;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@QueryEntity
public class Post extends BaseCreatedAtEntity {

    @Column(length = 30, nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate protectionStartDate;

    @Column(nullable = false)
    private LocalDate protectionEndDate;

    @Column(nullable = false)
    private LocalDate applicationEndDate;

    @Column(nullable = false)
    private String description;

    @Column(length = 100, nullable = false)
    private String contactInfo;

    @Column(nullable = false)
    private Boolean isApplicationEnd;

    @Column(nullable = false)
    private Boolean isUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToOne(mappedBy = "post", cascade = CascadeType.REMOVE)
    private PetInfo petInfo;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PetImage> petImages;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Application> applications;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Builder
    public Post(String title, String description, Account account, String protectionStartDate, String protectionEndDate, String applicationEndDate, String contactInfo, Boolean isApplicationEnd, Boolean isUpdated) {
        this.title = title;
        this.description = description;
        this.account = account;
        this.protectionStartDate = LocalDate.parse(protectionStartDate);
        this.protectionEndDate = LocalDate.parse(protectionEndDate);
        this.applicationEndDate = LocalDate.parse(applicationEndDate);
        this.contactInfo = contactInfo;
        this.isApplicationEnd = isApplicationEnd;
        this.isUpdated = isUpdated;
    }

    public void isEnd() {
        this.isApplicationEnd = true;
    }

    public void update(PostRequest post) {
        this.title = post.getTitle();
        this.protectionStartDate = LocalDate.parse(post.getProtectionStartDate());
        this.protectionEndDate = LocalDate.parse(post.getProtectionEndDate());
        this.applicationEndDate = LocalDate.parse(post.getApplicationEndDate());
        this.description = post.getDescription();
        this.contactInfo = post.getContactInfo();
        this.isUpdated = true;
    }

}
