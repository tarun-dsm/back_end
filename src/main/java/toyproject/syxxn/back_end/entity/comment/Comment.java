package toyproject.syxxn.back_end.entity.comment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.BaseLastModifiedAtEntity;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseLastModifiedAtEntity {

    @Column(length = 100, nullable = false)
    private String comment;

    private Boolean isUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account writer;

    @Builder
    public Comment(String comment, Post post, Account writer) {
        this.comment = comment;
        this.post = post;
        this.writer = writer;
        this.isUpdated = false;
    }

    public Comment updateComment(String comment) {
        this.comment = comment;
        this.isUpdated = true;
        return this;
    }

}
