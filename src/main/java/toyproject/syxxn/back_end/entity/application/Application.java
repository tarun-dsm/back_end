package toyproject.syxxn.back_end.entity.application;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.BaseCreatedAtEntity;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.review.Review;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@QueryEntity
public class Application extends BaseCreatedAtEntity {

    @Column(columnDefinition = "bit(1)", nullable = false)
    private Boolean isAccepted;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @Builder
    public Application(Boolean isAccepted, Account account, Post post) {
        this.isAccepted = isAccepted;
        this.account = account;
        this.post = post;
    }

    public void acceptApplication() {
        isAccepted = true;
    }

}
