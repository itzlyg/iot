#### 协议实现方式
##### 对设备协议、上报下发数据解析，各种协议实现方式的模块
###### JAVA 实现方式
 实现`cn.sinozg.applet.biz.qt.service.impl.JavaScriptServiceImpl` 实现的接口为 `DataAnalysisService`，bean name为 `ProtocolContext.JAVA_ANALYSIS_BEAN`
 - DataAnalysisService 实现对协议的编码和解码功能
 - DataAnalysisService.decode 解码
 - DataAnalysisService.encode 编码
 - DataAnalysisService.onReceive 用来接收mqtt上报上来的消息，实现对mqtt的订阅，接收mq 上报上来的数据 处理 逻辑为：

   - mqtt 订阅对应的topic 后 在此方法处理 上报上来的指令
   - 根据不同的类型 封装数据 返回对应的结果  对应的bean的 的map
   - 如果是上报，ota等 回回调解码decode数据
   - 再根据 实际类型回调，behaviourService 业务实现回调逻辑
   - 先处理返回的数据，如果实体为空，回调订阅函数直接返回，否则 回调业务函数 如果有ack 再发送ack消息， 再回调订阅的函数
 
