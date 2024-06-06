/*
 * *******************************************************************************
 * Copyright (C) 2024-NOW(至今) 妙笔智编
 * Author: 锋楪技术团队
 *
 * 本文件包含 妙笔智编「FantasticEditor」 的源代码，该项目的所有源代码均遵循MIT开源许可证协议。
 * 本代码仅允许在十三届软件杯比赛授权比赛方可直接使用
 * *******************************************************************************
 * 免责声明：
 * 使用本软件的风险由用户自担。作者或版权持有人在法律允许的最大范围内，
 * 对因使用本软件内容而导致的任何直接或间接的损失不承担任何责任。
 * *******************************************************************************
 */

package com.frontleaves.fantasticeditor.services.interfaces

import com.frontleaves.fantasticeditor.models.vo.api.role.RoleCustomEditVO
import com.frontleaves.fantasticeditor.models.vo.api.role.RoleInfoVO

/**
 * # 角色服务接口
 * 用于定义角色服务接口
 *
 * @since v1.0.0
 * @constructor 创建一个角色服务接口
 */
interface RoleService {

    /**
     * ## 编辑自定义角色
     * 编辑自定义角色，根据传入 VO 信息对角色进行修改；
     * 检查默认（内置）角色不允许被修改，只允许修改自定义得角色信息；若修改的角色名与数据库内容一致，则抛出错误
     * 修改成功后返回操作成功的结果，否则返回失败，不允许操作的内容将会直接抛出异常；
     *
     * @param roleCustomEditVO 自定义角色编辑VO
     * @return 编辑结果
     */
    fun editCustomRole(roleCustomEditVO: RoleCustomEditVO, roleId: String): Boolean

    /**
     * ## 添加角色
     * 添加角色，根据传入 VO 信息对角色进行添加；
     * 检查默认（内置）角色不允许被添加，只允许添加自定义得角色信息；若添加的角色名与数据库内容一致，则抛出错误
     * 添加成功后返回操作成功的结果，否则返回失败，不允许操作的内容将会直接抛出异常；
     *
     * @param roleCustomEditVO 自定义角色编辑VO
     * @return 添加结果
     * */
    fun addRole(roleCustomEditVO: RoleCustomEditVO): Boolean

    /**
     * ## 获取角色信息
     * 通过uuid、username获取用户所属的角色信息
     * 通过ruuid、roleName获取角色信息
     *
     * @param uuid 用户id
     * @param username 用户名
     * @param ruuid 角色id
     * @param roleName 角色名称
     * @return 角色信息
     */
    fun getUserRoleInfo(
        uuids: List<String>,
        usernames: List<String>,
        ruuids: List<String>,
        roleNames: List<String>,
    ): List<RoleInfoVO>
}
