package com.wrbi.springbootinit.manager;

import com.wrbi.springbootinit.common.ErrorCode;
import com.wrbi.springbootinit.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.SparkSyncChatResponse;
import io.github.briqt.spark4j.model.request.SparkRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于对接 AI 平台
 */
@Service
@Slf4j
public class AiManager {

    @Resource
    private YuCongMingClient yuCongMingClient;

    @Resource
    private SparkClient sparkClient;

    private static final String EXAMPLE = "\n" +
            "【【【【【\n" +
            "{\n" +
            "\"title\": {\n" +
            "\"text\": \"用户趋势分析\"\n" +
            "},\n" +
            "\"tooltip\": {\n" +
            "\"trigger\": \"axis\"\n" +
            "},\n" +
            "\"xAxis\": {\n" +
            "\"type\": \"category\",\n" +
            "\"data\": [\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\"]\n" +
            "},\n" +
            "\"yAxis\": {\n" +
            "\"type\": \"value\"\n" +
            "},\n" +
            "\"series\": [\n" +
            "{\n" +
            "\"name\": \"用户数据\",\n" +
            "\"type\": \"line\",\n" +
            "\"data\": [200, 200, 800, 90, 800, 800, 800]\n" +
            "},\n" +
            "{\n" +
            "\"name\": \"用户增量\",\n" +
            "\"type\": \"line\",\n" +
            "\"data\": [10, 20, 10, 30, 10, 20, 10]\n" +
            "}\n" +
            "]\n" +
            "}\n" +
            "【【【【【\n" +
            "\u200B\n" +
            "明确的数据分析结论：从给定的数据中，我们可以看到用户数据和用户增量的趋势。在初始阶段，用户数据从200逐渐增加到800，然后下降到90，接着又增加到800，最后保持在800左右。而用户增量的趋势与用户数据的波动性增长有关，也受到其他因素的影响，如市场推广活动、竞争对手的策略等。\n";

     private static final String PRECONDITION =
            "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
                    "分析需求：\n" +
                    "{数据分析的需求或者目标}\n" +
                    "原始数据：\n" +
                    "{csv格式的原始数据，用,作为分隔符}\n" +
                    "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
                    "【【【【【\n" +
                    "{前端 Echarts V5 的 option 配置对象json代码（注意不要使用单引号，全部都使用双引号；可以下载为图片），合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\n" +
                    "【【【【【\n" +
                    "{明确的数据分析结论、越详细越好，不要生成多余的注释，至少300字}\n" +
                    "示例如下：\n" +
                    EXAMPLE;

    /**
     * AI 对话
     *
     * @param modelId
     * @param message
     * @return
     */
    public String doChat(long modelId, String message) {
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        return response.getData().getContent();
    }

    /**
     * 向讯飞 AI 发送请求
     *
     * @return
     */
    public String sendMesToAIUseXingHuo(final String content) {
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.userContent(PRECONDITION + content));
        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
        // 消息列表
                .messages(messages)
        // 模型回答的tokens的最大长度,非必传,取值为[1,4096],默认为2048
                .maxTokens(2048)
        // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.2)
        // 指定请求版本 这个版本根据自己的 API 版本进行修改
                .apiVersion(SparkApiVersion.V3_5)
                .build();
        // 同步调用
        SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
        String responseContent = chatResponse.getContent();
        log.info("星火 AI 返回的结果 {}", responseContent);
        return responseContent;
    }
}
