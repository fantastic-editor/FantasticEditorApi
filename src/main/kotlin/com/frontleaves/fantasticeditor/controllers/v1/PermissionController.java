package com.frontleaves.fantasticeditor.controllers.v1;

import com.frontleaves.fantasticeditor.models.dto.GetPermissionDTO;
import com.frontleaves.fantasticeditor.models.entity.sql.SqlPermissionDO;
import com.frontleaves.fantasticeditor.services.interfaces.PermissionService;
import com.frontleaves.fantasticeditor.utility.BaseResponse;
import com.frontleaves.fantasticeditor.utility.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制器
 * <p>
 * 用于处理权限相关的请求, 如获取权限列表等
 *
 * @author DC_DC
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/permission")
public class PermissionController {
    private final PermissionService permissionService;

    /**
     * 获取权限列表
     * <p>
     * 获取权限列表
     *
     * @param search 搜索关键字
     * @param page   页码
     * @param size   每页大小
     * @return 权限列表
     */
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<GetPermissionDTO>>> getPermissionList(
            @RequestParam(value = "search", required = false) final String search,
            @RequestParam(value = "page", defaultValue = "1") final String page,
            @RequestParam(value = "size", defaultValue = "20") final String size) {
        // 对page和size进行校验
        final int page1 = Integer.parseInt(page);
        final int size1 = Integer.parseInt(size);
        if (page1 <= 0 || size1 <= 0) {
            throw new RuntimeException("页码和每页大小必须大于0");
        }
        List<SqlPermissionDO> sqlPermissionDOList = permissionService.getPermissionList(search, page1, size1);
        List<GetPermissionDTO> getPermissionDTOList = new ArrayList<>();
        for (SqlPermissionDO sqlPermissionDO : sqlPermissionDOList) {
            GetPermissionDTO getPermissionDTO = new GetPermissionDTO();
            BeanUtils.copyProperties(sqlPermissionDO, getPermissionDTO);
            getPermissionDTOList.add(getPermissionDTO);
        }
        return ResultUtil.success("获取权限列表成功", getPermissionDTOList);
    }
}
