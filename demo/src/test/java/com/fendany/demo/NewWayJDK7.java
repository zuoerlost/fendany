package com.fendany.demo;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created by zhangbaisong083 on 16-8-19.
 * JDK7 新特性
 * 2016年08月30日13:49:45
 * 看来确实实在是这样，JDK7中对代码编辑的提升，并没有多少，真正的变革是发生在JDK8中的
 * JDK只是对
 */
public class NewWayJDK7 {

    /**
     * 测试使用1.7 集合快速创建特性，失败，2016年08月19日11:03:35
     * 应该不是真的。因为连JDK8 都没有提到这个东西
     */
    @Test
    public void test01() {
//        final List<String> list = ["item"];
//        String item = list[0];

//        Set<String> set = {"item"};
//        Map<String, Integer> map = {"key" : 1};
//        int value = map["key"];
    }

    /**
     * 测试try 失败 2016年08月19日11:03:23
     */
    @Test
    public void test02() throws IOException {
        String path = "";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
        }
    }

    @Test
    public void test03() {
        String s = "test";
        switch (s) {
            case "test":
                System.out.println("test");
            case "test1":
                System.out.println("test1");
                break;
            default:
                System.out.println("break");
                break;
        }

    }

    /**
     * 字面分隔符，方便阅读
     * 定义二进制字面值
     */
    @Test
    public void test04() {

        /**
         * 字面值切分
         * 只是为了可读性
         */
        int money = 100_000_000;

        /**
         * 当然这里笔者需要提示你的是，虽然咱们可以直接在程序中定义二进制字面值。
         * 但是在程序运算时，仍然会将其转换成十进制展开运算和输出。
         */
        int test = 0b010101;
    }

    /**
     *
     * 多异常匹配
     * 文件监控
     * 文件复制
     */
    @Test
    public void test07() {
        try {

            Path path = Paths.get("路径：/文件");
            System.out.println("文件节点数:" + path.getNameCount());
            System.out.println("文件名称:" + path.getFileName());
            System.out.println("文件根目录:" + path.getRoot());
            System.out.println("文件上级关联目录:" + path.getParent());

            Files.copy(Paths.get("路径：/源文件"), Paths.get("路径：/新文件"));

            // 文件监控
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            while (true) {
                WatchKey watchKey = watchService.take();
                for (WatchEvent<?> event : watchKey.pollEvents())
                    System.out.println(event.context().toString() + " 事件类型：" + event.kind());
                if (!watchKey.reset())
                    return;
            }
        } catch (IOException | InterruptedException ex) {
            // 多异常匹配
            ex.printStackTrace();
        }
    }
}
