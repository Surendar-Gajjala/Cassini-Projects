package com.cassinisys.platform.model.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

/**
 * Created by reddy on 7/1/15.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "AUTONUMBER")
public class AutoNumber {

    @Id
    @SequenceGenerator(name = "AUTONUMBER_ID_GEN", sequenceName = "AUTONUMBER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTONUMBER_ID_GEN")
    @Column(name = "AUTONUMBER_ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description = "";
    @Column(name = "NUMBERS")
    private Integer numbers = 5;
    @Column(name = "START")
    private Integer start = 1;
    @Column(name = "INCREMENT")
    private Integer increment = 1;
    @Column(name = "PADWITH")
    private String padwith = "0";
    @Column(name = "PREFIX")
    private String prefix = "";
    @Column(name = "SUFFIX")
    private String suffix = "";
    @Column(name = "NEXT_NUMBER")
    private Integer nextNumber = -1;

    public AutoNumber() {

    }

    public AutoNumber(String name, String description, Integer numbers, Integer start, Integer increment, String padwith, String prefix, Integer nextNumber) {
        this.name = name;
        this.description = description;
        this.numbers = numbers;
        this.start = start;
        this.increment = increment;
        this.padwith = padwith;
        this.prefix = prefix;
        this.nextNumber = nextNumber;
    }

    @Transient
    private Boolean itemsExist = false;



    public String next() {
        String next = "";

        if (prefix != null && !prefix.trim().isEmpty()) {
            next += prefix;
        }

        int n = getNextNumber();

        String s = "" + n;
        if ((numbers - s.length()) >= 0) {
            next += StringUtils.repeat(padwith, (numbers - s.length()));
            next += n;
        }

        n = n + increment;
        setNextNumber(n);

        if (suffix != null && !suffix.trim().isEmpty()) {
            next += suffix;
        }

        return next;
    }

    public String nextNumber() {
        String next = "";

        if (prefix != null && !prefix.trim().isEmpty()) {
            next += prefix;
        }

        int n = getNextNumber();

        String s = "" + n;

        n = n + increment;
        setNextNumber(n);

        return s;
    }


    public String readOnlyNext() {
        String next = "";

        if (prefix != null && !prefix.trim().isEmpty()) {
            next += prefix;
        }

        int n = getNextNumber();

        String s = "" + n;
        if ((numbers - s.length()) >= 0) {
            next += StringUtils.repeat(padwith, (numbers - s.length()));
            next += n;
        }

        if (suffix != null && !suffix.trim().isEmpty()) {
            next += suffix;
        }

        return next;
    }


    public String current() {
        String next = "";

        if (prefix != null && !prefix.trim().isEmpty()) {
            next += prefix;
        }

        int n = getNextNumber();

        String s = "" + (n - 1);
        if ((numbers - s.length()) >= 0) {
            next += StringUtils.repeat(padwith, (numbers - s.length()));
            next += (n - 1);
        }

        return next;
    }
}
