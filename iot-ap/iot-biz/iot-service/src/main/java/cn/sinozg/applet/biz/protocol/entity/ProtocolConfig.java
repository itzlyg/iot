package cn.sinozg.applet.biz.protocol.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 协议配置表表
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_iot_protocol_config")
@Schema(name = "ProtocolConfig", description = "协议配置表")
public class ProtocolConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 协议id */
    @Schema(description = "协议id")
    @TableField("protocol_id")
    private String protocolId;


    /** 端口 */
    @Schema(description = "端口")
    @TableField("port")
    private Integer port;


    /** 模式;01 客户端 02 服务端 */
    @Schema(description = "模式;01 客户端 02 服务端")
    @TableField("mode")
    private String mode;


    /** 认证端口 */
    @Schema(description = "认证端口")
    @TableField("auth_port")
    private Integer authPort;


    /** ip */
    @Schema(description = "ip")
    @TableField("ip")
    private String ip;


    /** 客户端 */
    @Schema(description = "客户端")
    @TableField("client_id")
    private String clientId;


    /** 用户名 */
    @Schema(description = "用户名")
    @TableField("user_name")
    private String userName;


    /** 密码 */
    @Schema(description = "密码")
    @TableField("password")
    private String password;


    /** 订阅主题 */
    @Schema(description = "订阅主题")
    @TableField("subscribe")
    private String subscribe;


    /** 是否使用WebSocket;01 是 02否 */
    @Schema(description = "是否使用WebSocket;01 是 02否")
    @TableField("websocket")
    private String websocket;


    /** WebSocket模式下的 地址 */
    @Schema(description = "WebSocket模式下的 地址")
    @TableField("websocket_url")
    private String websocketUrl;


    /** 心跳 */
    @Schema(description = "心跳")
    @TableField("beat_time")
    private Integer beatTime;


    /** 心跳数据 */
    @Schema(description = "心跳数据")
    @TableField("beat_data")
    private String beatData;


    /** 分割方式 */
    @Schema(description = "分割方式")
    @TableField("parser_type")
    private String parserType;


    /** 其他配置 */
    @Schema(description = "其他配置")
    @TableField("other_config")
    private String otherConfig;

}
