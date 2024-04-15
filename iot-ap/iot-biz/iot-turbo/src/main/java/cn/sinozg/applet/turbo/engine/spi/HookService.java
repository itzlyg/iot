package cn.sinozg.applet.turbo.engine.spi;

import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;

import java.util.List;

/**
 * <p> HookService </p>
 *
 * @author lijinghao
 * @version v1.0
 * @date 2023/2/16 6:59 PM
 */
public interface HookService {

    /**
     * Invoke hook service, used to perform data refresh operations on the gateway node.
     * Usage: Implement the interface and give the instance to Spring for management
     *
     * @param flowInstanceId runtime flow instance id
     * @param nodeInstanceId running node's instance id
     * @param nodeKey        running node's key
     * @param hookInfoParam  some info , you can refresh
     * @return new infos
     */
    List<InstanceDataVo> invoke(String flowInstanceId, String nodeInstanceId, String nodeKey, String hookInfoParam);
}
