package toyproject.syxxn.back_end.entity;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseIdEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
