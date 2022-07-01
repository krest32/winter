package com.krest.utils.myexception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class myException extends RuntimeException {

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "错误信息")
    private String msg;

    @Override
    public String toString() {
        return "myException{" +
                "message=" + this.getMsg() +
                ", code=" + code +
                '}';
    }
}

