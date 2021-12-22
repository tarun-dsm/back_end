package toyproject.syxxn.back_end.entity.application;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.BaseCreatedAtEntity;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"applicant_id", "post_id"}))
public class Application extends BaseCreatedAtEntity {

    @Column(nullable = false)
    private Boolean isAccepted;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private Account applicant;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public Application(Boolean isAccepted, Account applicant, Post post) {
        this.isAccepted = isAccepted;
        this.applicant = applicant;
        this.post = post;
    }

    public Application acceptApplication() {
        isAccepted = true;
        return this;
    }

}
