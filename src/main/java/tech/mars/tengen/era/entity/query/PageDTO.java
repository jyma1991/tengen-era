package tech.mars.tengen.era.entity.query;
/**
 * @DESCRIPTION:
 * @author majunyang
 * @since 2022年9月7日 下午5:06:37
 */

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 类PageDTO的实现描述：TODO 类实现描述
 * @author majunyang 2022/9/7 17:06
 */
@Data
public class PageDTO {
    @Parameter(description="pageSize")
    private Integer pageSize = 20;
    @Parameter(description="pageNo")
    private Integer pageNum = 1;

    public Integer getPageSize() {
        return this.pageSize == null ? 20 : this.pageSize;
    }

    public Integer getPageNum() {
        return this.pageNum == null ? 1 : this.pageNum;
    }
}
