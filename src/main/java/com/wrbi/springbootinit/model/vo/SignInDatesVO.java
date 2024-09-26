package com.wrbi.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SignInDatesVO implements Serializable {
    private List<Integer> signInDates;
}
