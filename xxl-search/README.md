
1、spring mvc 4.3.2
2、log4j2
3、jsonp

...
jdk1.7 (lucene5.5.2 需要 jdk1.7)
servlet3 (springmvc4 需要 servlet3.0)

---
[官网](http://lucene.apache.org/core/)
[github地址](https://github.com/xuxueli/xxl-incubator/tree/master/xxl-search)
[索引文件查询luck](https://github.com/DmitryKey/luke/releases)

[lucene使用与优化](http://my.oschina.net/lushuifa/blog/198690)
[lucene学习笔记](http://my.oschina.net/kkrgwbj/blog/513362)

# Lucene 的用法
---
apache lucene是apache下一个著名的开源搜索引擎内核，基于Java技术，处理索引，拼写检查，点击高亮和其他分析，分词等技术。

### 作用
- 1、性能上：大数据量的情况下，用数据库来做文本的搜索是很可怕的事情，但是Lucene轻松毫秒杀
- 2、功能上：Lucene 可以做全文匹配搜索
    - 假设："开源中国"是一个以开源软件为核心的技术社区网站……，
    - 搜索关键字："开源社区"
    - 如果这个内容是存在数据库的，就搜不到，如果使用Lucene就可以搜到
    
### 使用流程
- 1、建立索引:新建索引库、分词Writer;
- 2、查询:定位索引库、分词Reader、处理查询结果;

### 索引文件分析
- Index
- Segment
- Document
- Field
- Term


# Elasticsearch应用
---

