package toyproject.syxxn.back_end.entity.application;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Application {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd`T`hh:mm:SS")
    private LocalDateTime createdAt;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "post_id")
    private Post post;

}
