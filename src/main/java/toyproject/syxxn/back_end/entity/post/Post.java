package toyproject.syxxn.back_end.entity.post;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.entity.BaseCreatedAtEntity;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.pet.PetImage;
import toyproject.syxxn.back_end.entity.pet.PetInfo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@QueryEntity
public class Post extends BaseCreatedAtEntity {

    @NotNull
    @Column(length = 30)
    private String title;

    @NotNull
    private LocalDate protectionStartDate;

    @NotNull
    private LocalDate protectionEndDate;

    @NotNull
    private LocalDate applicationEndDate;

    @NotNull
    private String description;

    @NotNull
    @Column(length = 100)
    private String contactInfo;

    @NotNull
    @Column(columnDefinition = "bit(1)")
    private Boolean isApplicationEnd;

    @NotNull
    @Column(columnDefinition = "bit(1)")
    private Boolean isUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private PetInfo petInfo;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PetImage> petImages;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Application> applications;

    @Builder
    public Post(String title, String description, Account account, LocalDate protectionStartDate, LocalDate protectionEndDate, LocalDate applicationEndDate, String contactInfo, Boolean isApplicationEnd, Boolean isUpdated) {
        this.title = title;
        this.description = description;
        this.account = account;
        this.protectionStartDate = protectionStartDate;
        this.protectionEndDate = protectionEndDate;
        this.applicationEndDate = applicationEndDate;
        this.contactInfo = contactInfo;
        this.isApplicationEnd = isApplicationEnd;
        this.isUpdated = isUpdated;
    }

    public void isEnd() {
        this.isApplicationEnd = true;
    }

    public void update(PostRequest post) {
        this.title = post.getTitle();
        this.protectionStartDate = LocalDate.parse(post.getProtectionstartdate());
        this.protectionEndDate = LocalDate.parse(post.getProtectionenddate());
        this.applicationEndDate = LocalDate.parse(post.getApplicationenddate());
        this.description = post.getDescription();
        this.contactInfo = post.getContactinfo();
        this.isUpdated = true;
    }

}
