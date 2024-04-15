package cn.sinozg.applet.turbo.engine.executor;

import cn.sinozg.applet.common.utils.SpringUtil;
import cn.sinozg.applet.turbo.engine.common.Constants;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.common.FlowElementEnum;
import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExecutorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorFactory.class);

    private static final Map<Integer, AbstractElementExecutor> MAP = new ConcurrentHashMap<>();

    public AbstractElementExecutor getElementExecutor(FlowElement flowElement) throws ProcessException {
        int elementType = flowElement.getType();
        AbstractElementExecutor elementExecutor;
        if (elementType == FlowElementType.CALL_ACTIVITY) {
            elementExecutor = getCallActivityExecutor(flowElement);
        } else {
            elementExecutor = executorByType(elementType);
        }
        if (elementExecutor == null) {
            LOGGER.warn("getElementExecutor failed: unsupported elementType.|elementType={}", flowElement.getType());
            throw new ProcessException(ErrorEnum.UNSUPPORTED_ELEMENT_TYPE, MessageFormat.format(Constants.NODE_INFO_FORMAT, flowElement.getKey(), FlowModelUtil.getElementName(flowElement), flowElement.getType()));
        }
        return elementExecutor;
    }

    private AbstractElementExecutor executorByType (int type){
        if (MapUtils.isEmpty(MAP)) {
            Map<String, AbstractElementExecutor> map = SpringUtil.beansOfType(AbstractElementExecutor.class);
            for (Map.Entry<String, AbstractElementExecutor> e : map.entrySet()) {
                FlowElementEnum en = FlowElementEnum.ofName(e.getKey());
                if (en != null) {
                    MAP.put(en.getCode(), e.getValue());
                }
            }
        }
        return MAP.get(type);
    }

    private AbstractElementExecutor getCallActivityExecutor(FlowElement flowElement) {
        Map<String, Object> properties = flowElement.getProperties();
        String callActivityExecuteType = Constants.CallActivityExecuteType.SYNC;
        if (properties.containsKey(Constants.ElementProperties.CALL_ACTIVITY_EXECUTE_TYPE)) {
            callActivityExecuteType = properties.get(Constants.ElementProperties.CALL_ACTIVITY_EXECUTE_TYPE).toString();
        }
        String callActivityInstanceType = Constants.CallActivityInstanceType.SINGLE;
        if (properties.containsKey(Constants.ElementProperties.CALL_ACTIVITY_INSTANCE_TYPE)) {
            callActivityInstanceType = properties.get(Constants.ElementProperties.CALL_ACTIVITY_INSTANCE_TYPE).toString();
        }
        if (callActivityExecuteType.equals(Constants.CallActivityExecuteType.SYNC)
            && callActivityInstanceType.equals(Constants.CallActivityInstanceType.SINGLE)) {
            return executorByType(flowElement.getType());
        }
        return null;
    }
}
