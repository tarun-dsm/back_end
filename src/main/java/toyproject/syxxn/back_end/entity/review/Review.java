package toyproject.syxxn.back_end.entity.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.account.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "bigdecimal(1,1)")
    private BigDecimal ratingScore;

    private String comment;

    @NotNull
    private Long writerId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "account_id")
    private Account account;

}