//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ConsigneeDto implements Serializable {

    private String consignee;

    private String address;

    private Long regionId;

    private String mobile;
}
