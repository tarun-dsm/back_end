package toyproject.syxxn.back_end.entity.application;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.BaseCreatedAtEntity;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.review.Review;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Application extends BaseCreatedAtEntity {

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "bit(1)")
    private Boolean isAccepted = false;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @JsonManagedReference
    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    public void acceptApplication() {
        isAccepted = true;
    }

}
