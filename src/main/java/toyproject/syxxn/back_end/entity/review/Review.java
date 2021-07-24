package toyproject.syxxn.back_end.entity.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.BaseCreatedAtEntity;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review extends BaseCreatedAtEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Double ratingScore;

    @NotNull
    private String comment;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "writer_id", nullable = false)
    private Account writer;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "target_id", nullable = false)
    private Account target;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

}
