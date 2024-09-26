package com.wrbi.springbootinit.constant;

public interface RegexConstant {
    String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    String VALIDATE_CODE_REGEX = "^[0-9]{6}$";
}
