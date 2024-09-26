package com.wrbi.springbootinit.common;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 删除请求
 *

 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private long id;

    private static final long serialVersionUID = 1L;
}