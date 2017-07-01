### 书籍
***
* 《Java编程思想》
* 《Java并发编程实践》

### 开发环境
---
-- | windows | mac
-- | -- | --
jdk | jdk7 | jdk7
web server | tomcat7 | tomcat7
maven | maven3 | maven3
git | git(eclipse的egit插件可替代之) | git
ide | eclipse mars(jdk7+maven3+egit)(freemarker-ide+openexplorer) | idea
database | mysql5.5 + navicat(oracle11g + pl/sql) | mysql5.5 + navicat
linux | secureFX + secureCRT + vbox + centos6.5 | secureFX + secureCRT + vbox + centos6.5（terminal + scp）
etc | jd-jui | sublime
（搜狗输入法，有道笔记，QQ浏览器书签， 百度云，office，WPS, RAR）


brew：http://brew.sh/index_zh-cn.html

### DEV-Mac：常用操作
***
sudo mkdir data     (root权限目录) 
sudo chown xuxueli data     （目录权限分配给xuxueli）




##### sudo vi /etc/hosts
xue.**ping.com localhost

##### u代表所有者，x代表执行权限
chmod u+x *.sh

##### 左上角箭头，右下角肩头

    Mac 快捷键符号 斜箭头
    ↖︎  Home键 对应  fn + 左方向键
    ↘︎  End键 对应  fn + 右方向键

##### 浏览器
command + R ：刷新
command + option + I ：打开开发者工具；

##### Mac键盘图标与对应快捷按键
    
    ⌘——Command ()
    ⌃ ——Control
    ⌥——Option (alt)
    ⇧——Shift
    ⇪——Caps Lock
    fn——功能键就是fn
    *.m*.h切换 ⌘+⌃ +↓or↑
    前进后退文本文件⌘+⌃ +←or→
    关闭当前文本文件⌘+⌃+w
    自动排版代码 ⌃+i
    左右缩进 ⌘+[or]
    注释 ⌘+/
    查看名称定义，进入头文件 ⌘+鼠标左键
    查看名称api文档 ⌥+鼠标左键

##### temp
command + 空格： spotlight搜索
item2 on my zsh
下载dmp，安装，推出，删除
活动监控器
fiddler
shaowsocket：
command + H：隐藏窗口


##### intellij
- command + x ：剪切整行；
- option + enter : 提示；
- command + option + B : 跳转到service 实现方法；
- command + shift + O:打开文件
- command + shift + 上/下 ：上线移动代码；
- command + N：生成构造；
- Alt + Enter ：快速提示；
- Ctrl + Option + O : 更新Import；
- Ctrl + Shift + ? ：快速注解
- Command + F12 ：快速搜索类中的方法
- Command + [/] ： 返回上个光标处；
- Shift + F6：文件更名、变量更名
- F6 ：文件目录修改


##### Mac 硬盘占用

    df -h  ：命令查看整个硬盘的大小 ，-h表示人可读的
    du -d 1 -h  ：命令查看当前目录下所有文件夹的大小 -d 指深度，后面加一个数值
    ls -all :查看文件大小
    du -h --max-depth=1 /data：linux可用，查看目录大小；


