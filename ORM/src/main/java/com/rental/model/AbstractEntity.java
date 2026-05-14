package com.rental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@MappedSuperclass
@Access(AccessType.FIELD)
@Embeddable
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Version
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected long version;
}
