package toyproject.syxxn.back_end.entity.comment;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.BaseCreatedAtEntity;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@QueryEntity
public class Comment extends BaseCreatedAtEntity {

    @Column(length = 100, nullable = false)
    private String comment;

    private Boolean isUpdated;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account writer;

    @Builder
    public Comment(String comment, Post post, Account account) {
        this.comment = comment;
        this.post = post;
        this.writer = account;
        this.isUpdated = false;
    }

    public Comment updateComment(String comment) {
        this.comment = comment;
        this.isUpdated = true;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public String getUpdatedAtToString() {
        return this.updatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }

}
