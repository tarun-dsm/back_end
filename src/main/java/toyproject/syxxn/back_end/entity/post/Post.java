package toyproject.syxxn.back_end.entity.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends BaseCreatedAtEntity {

    @NotNull
    private Boolean isWithinADay;

    @NotNull
    private LocalDate tripStartDate;

    @NotNull
    private LocalDate tripEndDate;

    @NotNull
    private LocalDate applicationEndDate;

    @NotNull
    private String description;

    @NotNull
    @Column(length = 100)
    private String contactInfo;

    @NotNull
    private Boolean isUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY)
    @JsonManagedReference
    private PetInfo petInfo;

    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private List<PetImage> petImages;

    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private List<Application> applications;

}
