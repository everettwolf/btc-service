package com.btc.web.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by Eric on 10/22/14.
 */
@Data
@Entity
@Table(name = "properties")
public class Properties implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "cp_id_generator", sequenceName = "CP_ID_SEQUENCE", allocationSize = 2)
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "cp_id_generator")
    private Long id;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "property_name")
    private String propertyName;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "value")
    private String propertyValue;

    @Size(max = 100)
    @Column(name = "lable")
    private String propertyLabel;

    @Column(name = "admin_accessible")
    private int adminAccessible;

}
