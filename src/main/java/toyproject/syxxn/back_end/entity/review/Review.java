package toyproject.syxxn.back_end.entity.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;
import toyproject.syxxn.back_end.entity.account.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @NotNull
    private String comment;

    @NotNull
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd`T`hh:mm:SS")
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "writer_id")
    private Account writer;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "target_id")
    private Account target;

    public void update(String comment, BigDecimal ratingScore) {
        this.comment = comment;
        this.ratingScore = ratingScore;
    }

}
