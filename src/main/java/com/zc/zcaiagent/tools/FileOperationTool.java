package com.zc.zcaiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.zc.zcaiagent.constent.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * @author zcnovice
 * @data 2025/7/14 下午4:50
 */

/* 文件操作工具类 */
public class FileOperationTool {

    /* FileConstant.FILE_SAVE_DIR  是文件固定路径(是一个常量) */
    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "/file";

    /* Tool是向AI说明这个工具是干什么的 */
    @Tool(description = "Read content from a file")
    /* ToolParam是向AI描述变量是什么(是要读取的文件夹的名字) */
    public String readFile(@ToolParam(description = "Name of the file to read") String fileName) {
        String filePath = FILE_DIR + "/" + fileName;

        /* 万一没有成功不返回消息AI是不知道的(所以要抛出异常告诉AI) */
        try {
            // readUtf8String  --  Hutool 工具库提供的一个方法，用于以 UTF-8 编码读取文件内容并返回字符串。
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    /* 与上面相似 */
    @Tool(description = "Write content to a file")
    public String writeFile(
            @ToolParam(description = "Name of the file to write") String fileName,
            @ToolParam(description = "Content to write to the file") String content) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            // 创建目录
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to: " + filePath;
        } catch (Exception e) {
            return "Error writing to file: " + e.getMessage();
        }
    }
}
