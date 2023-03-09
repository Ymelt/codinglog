# java

## List转Map的方法

~~~java
list.stream().collect(Collectors.groupingBy(Entity::id));
list.stream().collect(Collectors.groupingBy(Entity::id,Collectors.groupingBy(Entity::name)));
~~~



## 获取当前年月的方法

~~~
Calendar cal = Calendar.getInstance();
Integer year = cal.get(Calendar.YEAR);
Integer month = cal.get(Calendar.MONTH);
//如果需要将年月回推，前推 下例往前推6个月
//需要注意的是在月份的29，30，31往前推月份会出现错误，比如3月往前推一个月还是3月，毕竟2月份的天数是在28，29天，所以需要下面这一句，将当前的日期设置为1号，再往前推
cal.set(Calendar.DAY_OF_MONTH, 1);
cal.set(Calendar.MONTH,cal.get(Calendar.MONTH) - 6);
~~~



## listAfterPage

~~~java
public JSONObject afterPage(List<?> list,Integer firstNum,Integer num){
        //将list分页
        int total = list.size();
        int minNum = Math.min(list.size(), firstNum + num);
        if(firstNum > list.size() - 1 ){
            list = new ArrayList<>();
            total = 0;
        }else{
            list = list.subList(firstNum,minNum);
        }
        JSONObject result = new JSONObject();
        result.put("total",total);
        result.put("count",list.size());
        result.put("list",list);
        return result;
    }
~~~



## Null

1. null是关键字，区分大小写

2. null是所有引用类型的默认值，包括object类型

3. null既不能是对象也不是一种类型，可以赋予任何引用类型，也可以将null转换成任意类型

4. null可以赋值给引用变量，但是不能赋值给八大基本数据类型 

​      (byte,int,long,double,faloat,short,char,boolean)

5. 任何含有null值的包装类在java拆箱生成基本数据类型时会抛出空指针异常

6. 如果使用了带有null的引用类型变量。instanceof操作会返回false

## 代码判断是否是数字类型

1. apache 工具类

~~~java
import org.apache.commons.lang3.StringUtils;
 
public class NumberDemo {
    /**
     * 判断是否为数字可以使用工具类 StringUtils
     * 通过方法 isNumeric 进行判断是否为数字
     * @param str
     * @return
     */
    private static boolean isNumber(String str) {
        return StringUtils.isNumeric(str);
    }
}
~~~

2. 使用ACSII码

   ~~~java
   public static boolean isNumeric(String str){
       for(int i=str.length();--i>=0;){
           int chr=str.charAt(i);
           if(chr<48 || chr>57)
               return false;
       }
      return true;
   } 
   ~~~

3. 使用正则表达式（推荐）

   ~~~java
   public class NumberDemo {
       // 根据阿里巴巴代码规范，将Pattern设置为全局常量
       // 通过 -?[0-9]+(\\\\.[0-9]+)? 进行匹配是否为数字
       private static Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");
       
       /**
        * 通过正则表达式判断字符串是否为数字
        * @param str
        * @return
        */
       private static boolean isNumber(String str) {
           // 通过Matcher进行字符串匹配
           Matcher m = pattern.matcher(str);
           // 如果正则匹配通过 m.matches() 方法返回 true ，反之 false
           return m.matches();
       }
   }
   ~~~




# Mysql



## IFNULL的用法

在查询数据库的时候希望直接对null进行判断，使用ifnull的语法

~~~mysq
1. IFNULL(expr1,expr2)
2. ISNULL(expr) --- 如果expr为null,则返回值为1，否则返回值为0
3. NULLIF(expr1,expr2) ---如果expr1 = expr2成立，返回值为NULL，否则返回值为expr1
~~~


## left join 后没有值还是为null

即使右边的字段默认值设为0



## 不建议使用在数据库中使用null作为默认值

确实是在取数和查询的时候需要特别注意

···sql语句在NOT IN、!= 等负向条件查询在有 NULL 值的情况下返回永远为空结果，查询容易出错


## 传入字符串匹配数据库中的数字类型

mysql会隐式转换，int类型的字段传入字符串会截取从第一位int型开始到第一个非int型的值作为条件。

例如：where a=35abc 相当于 where a = 35,再如where a = b35c 相当于where a = 0

# Springboot

## @Component和@Configuration

Spring 注解中 @Configuration 和 @Component 的区别总结为一句话就是：

 @Configuration 中所有带 @Bean 注解的方法都会被动态代理（cglib），因此调用该方法返回的都是同一个实例。而 @Conponent 修饰的类不会被代理，每实例化一次就会创建一个新的对象。

# git

## 常用命令

~~~git
git status  查看当前状态
git log  查看提交日志
git merge dev  合并dev分支至当前分支
git add .      添加当前目录全部文件至暂存区
git commit -m '测试'     提交，提交信息为测试
git push origin master  推送至远端分支（master为需要推送分支，按实际需要选择）
git pull origin master  合并远端分支至本地 (git pull 等于 git fetch + git merge)
git pull --rebase origin master rebase方式合并远端分支至本地
git branch 查看当前分支
git branch dev 创建dev分支  （dev可选）
git branch -d dev 删除dev分支
git branch -r 查看远程分支
git branch -a 查看所有分支 （包括远程分支）
git checkout master 切换至master分支
git checkout -b dev 创建dev分支并切换至dev分支
git checkout -b dev origin/dev 创建远程分支到本地
git restore file 丢弃工作区修改（file为具体文件名称）
git restore * 丢弃所有工作区修改
git restore --staged file  回退暂存区文件 不会更改文件内容
git rebase --continue   rebase后继续操作
git rebase --abort 退出rebase 操作


~~~



## git pull代码冲突

没有commit修改的代码，直接进行了git pull 代码冲突

先commit本地的代码,然后查看文件，修改文件，解决冲突后再次提交

## 回退本地的代码版本并推送线上

~~~
// 查看分支
git branch
 
// ******为你要回退到哪一次提交的那个版本号
git reset --hard ******
 
// 查看当前分支
git status
 
// 将当前分支push上去
git push origin 分支名称 --force
 
// 验证下，log中错误提交的那次记录不见了
git log
~~~






# Telnet



## 什么是Telnet？

​	对于Telnet的认识，不同的人持有不同的观点，可以把Telnet当成一种通信协议，但是对于入侵者而言，Telnet只是一种远程登录的工具。一旦入侵者与远程主机建立了Telnet连接，入侵者便可以使用目标主机上的软、硬件资源，而入侵者的本地机只相当于一个只有键盘和显示器的终端而已。

    代码内容

## 为什么需要Telnet？

​	telnet就是查看某个端口是否可访问。我们在搞开发的时候，经常要用的端口就是 8080。那么你可以启动服务器，用telnet 去查看这个端口是否可用。

​	Telnet协议是TCP/IP协议家族中的一员，是Internet远程登陆服务的标准协议和主要方式。它为用户提供了在本地计算机上完成远程主机工作的能力。在终端使用者的电脑上使用telnet程序，用它连接到服务器。终端使用者可以在telnet程序中输入命令，这些命令会在服务器上运行，就像直接在服务器的控制台上输入一样。可以在本地就能控制服务器。要开始一个telnet会话，必须输入用户名和密码来登录服务器。Telnet是常用的远程控制Web服务器的方法。



# curl

curl是一个非常实用的、用来与服务器之间传输数据的工具；支持的协议包括 (DICT, FILE, FTP, FTPS, GOPHER, HTTP, HTTPS, IMAP, IMAPS, LDAP, LDAPS, POP3, POP3S, RTMP, RTSP, SCP, SFTP, SMTP, SMTPS, TELNET and TFTP)，curl设计为无用户交互下完成工作；curl提供了一大堆非常有用的功能，包括代理访问、用户认证、ftp上传下载、HTTP POST、SSL连接、cookie支持、断点续传...。

# 生成word

## 1-引用依赖

~~~
<org.apache.poi.version>4.1.2</org.apache.poi.version>		
		<dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <org.apache.poi.version>4.1.2</org.apache.poi.version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <org.apache.poi.version>4.1.2</org.apache.poi.version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml-schemas</artifactId>
            <org.apache.poi.version>4.1.2</org.apache.poi.version>
        </dependency>
~~~

## 2-官网地址

~~~
https://poi.apache.org/apidocs/4.1/
~~~

## 3-使用说明



### 使用案例

~~~java
InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("word/PictureHaveTry.docx");
        XWPFDocument document = null;
        try {
            //解析docx模板并获取document对象
            assert resourceAsStream != null;
            document = new XWPFDocument(resourceAsStream);

            //word模板中空行为4
            int emptySize = 4;

            ExportWordUtil.changeTableText(document, tableMap);
            ExportWordUtil.doRowInsertText(document, familyList, 5, emptySize);
            int oversize = 0;
            if (familyList.size() > emptySize) {
                oversize = familyList.size() - emptySize;
            }
            ExportWordUtil.doRowInsertText(document, reportList, 12 + oversize, emptySize);
            ExportWordUtil.mergeCellsVertically(document.getTables().get(0), 0, 4, 8 + oversize);
            ExportWordUtil.mergeCellsVertically(document.getTables().get(0), 0, 11 + oversize, 15 + oversize + reportList.size() - emptySize);

            //插入图片
            if (StringUtils.isNotEmpty(imagesUrl)){
                XWPFTableCell cell = ExportWordUtil.getImagesPositionInTable(document.getTables().get(0), 17, 1);
                fileService.downFileWriteIntoCell(cell,imagesUrl);
            }


            //excel数据完成，生成文件，导出
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-download");
            response.addHeader("Content-disposition", "attachment;fileName=" + URLEncoder.encode("台州再生金属-质检检测报告.docx", "utf-8"));
            response.flushBuffer();
            ServletOutputStream outputStream = response.getOutputStream();
            document.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
~~~

### 前言

1. HWPF是处理 Microsoft Word 97（-2007） .doc文件格式，它还为较旧的Word 6和Word 95文件格式提供了有限的只读支持。包含在poi-scratchpad-XXX.jar中。
2. XWPF是处理 Word 2007 .docx文件格式，包含在poi-ooxml-XXX.jar中
3. 虽然HWPF和XWPF提供类似的功能，但目前两者之间没有公共接口。
4. 这里POI使用 4.1.2版本，处理 .docx文件格式，word文件中只包含普通数据和表格数据。在Java POI通用导入导出Excel 的项目基础上简单实现的



### 实现步骤

#### a. 新建word模板文件(.docx)

在表格中需要填充的部分，表格的单元格中填充上${变量名}。注意标签名不能重复

word模板文件定义好放在项目的资源路径下。在代码中使用资源路径来获取对应的模板文件。

~~~
注意：如果遇到poi读取如${name}不能识别为一个整体时，可以使用
1）使用word的域操作
2）在text文档中写好，如${name}，然后再复制到word中，不要在word中一个一个敲，不然可能不会被poi识别为一个整体
~~~

#### b.常用类说明及获取模板中的内容

~~~
 XWPFDocument：用于处理.docx文件。

 XWPFParagraph：保存文本的段落 。在文档、表格、标题、页眉或页脚等都可存在段落。一个段落有很多样式化信息，但是实际的文本(可能还有更多的样式化)保存在子XWPFRuns上。

 XWPFRun：XWPFRun对象用一组公共属性定义一个文本区域。对文本的内容，字体，颜色，大小，样式等操作。

 XWPFTable：指定文档中出现的表的内容。表是一组按行和列排列的段落(和其他块级内容)。

 XWPFTableRow：表示XWPFTable中的一行。行大多只有大小和样式，更多的内容存在于子XWPFTableCells中。

  XWPFTableCell：表示XWPFTable中的单元格。单元格是存放实际内容(段落等)的东西。
~~~

1)获取文件中文档的文本内容

~~~java
    public static void main(String[] args) throws Exception {
       StringBuffer tableText = new StringBuffer();
        String templatePath = PoiSerivce.class.getResource("/static/template/person.docx").getPath();
        //解析docx模板并获取document对象
        XWPFDocument document = new XWPFDocument(new FileInputStream(templatePath));
        //获取整个文本对象
        List<XWPFParagraph> paragraphs = document.getParagraphs();
 
        //获取XWPFRun对象输出整个文档中的文本内容
        paragraphs.forEach(xwpfParagraph -> {
            List<XWPFRun> runs = xwpfParagraph.getRuns();
            for (XWPFRun run : runs) {
                tableText.append(run.toString());
            }
        });
        System.out.println(tableText.toString());
    }
~~~

2)获取文件中表格的文本内容

~~~java
    public static void main(String[] args) throws Exception {
        StringBuffer tableText = new StringBuffer();
        String templatePath = PoiSerivce.class.getResource("/static/template/person.docx").getPath();
        //解析docx模板并获取document对象
        XWPFDocument document = new XWPFDocument(new FileInputStream(templatePath));
        //获取全部表格对象
        List<XWPFTable> tables = document.getTables();
 
        //获取表格中的文本内容
        tables.forEach(xwpfTable -> {
            //获取表格行数据
            List<XWPFTableRow> rows = xwpfTable.getRows();
            for (XWPFTableRow row : rows) {
                //获取表格单元格数据
                List<XWPFTableCell> tableCells = row.getTableCells();
                for (XWPFTableCell tableCell : tableCells) {
                    List<XWPFParagraph> paragraphs = tableCell.getParagraphs();
                    paragraphs.forEach(xwpfParagraph -> {
                        List<XWPFRun> runs = xwpfParagraph.getRuns();
                        for (XWPFRun run : runs) {
                            tableText.append(run.toString());
                        }
                    });
                }
            }
        });
        System.out.println(tableText.toString());
    }
~~~

#### c.填充模板内容(导出形式参考使用案例)

~~~java
    public byte[] exportWord() {
        // 模拟点数据
        Map<String, String> paragraphMap = new HashMap<>();
        Map<String, String> tableMap = new HashMap<>();
        List<String[]> familyList = new ArrayList<>();
        paragraphMap.put("number", "10000");
        paragraphMap.put("date", "2020-03-25");
        tableMap.put("name", "赵云");
        tableMap.put("sexual", "男");
        tableMap.put("birthday", "2020-01-01");
        tableMap.put("identify", "123456789");
        tableMap.put("phone", "18377776666");
        tableMap.put("address", "王者荣耀");
        tableMap.put("domicile", "中国-腾讯");
        tableMap.put("QQ", "是");
        tableMap.put("chat", "是");
        tableMap.put("blog", "是");
        familyList.add(new String[]{"露娜", "女", "野友", "666", "6660"});
        familyList.add(new String[]{"鲁班", "男", "射友", "222", "2220"});
        familyList.add(new String[]{"程咬金", "男", "肉友", "999", "9990"});
        familyList.add(new String[]{"太乙真人", "男", "辅友", "111", "1110"});
        familyList.add(new String[]{"貂蝉", "女", "法友", "888", "8880"});
 
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        String templatePath = this.getClass().getClassLoader().getResource("static/template/person.docx").getPath();
        XWPFDocument document = null;
        try {
            //解析docx模板并获取document对象
            document = new XWPFDocument(new FileInputStream(templatePath));
 
            ExportWordUtil.changeParagraphText(document,paragraphMap);
            ExportWordUtil.changeTableText(document, tableMap);
            ExportWordUtil.copyHeaderInsertText(document, familyList , 7);
 
            document.write(byteOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteOut.toByteArray();
    }
~~~

#### d.合并单元格

跨列合并单元格

~~~java
	/** 
     * @Description: 跨列合并 
     * table要合并单元格的表格
     * row要合并哪一行的单元格
     * fromCell开始合并的单元格
     * toCell合并到哪一个单元格
     */  
    public  void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {  
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {  
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);  
            if ( cellIndex == fromCell ) {  
                // The first merged cell is set with RESTART merge value  
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);  
            } else {  
                // Cells which join (merge) the first one, are set with CONTINUE  
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);  
            }  
        }  
    }

~~~

跨行合并单元格

~~~java
	/** 
     * @Description: 跨行合并 
     * table要合并单元格的表格
     * col要合并哪一列的单元格
     * fromRow从哪一行开始合并单元格
     * toRow合并到哪一个行
     */  
    public  void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {  
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {  
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);  
            if ( rowIndex == fromRow ) {  
                // The first merged cell is set with RESTART merge value  
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);  
            } else {  
                // Cells which join (merge) the first one, are set with CONTINUE  
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);  
            }  
        }  
    }
~~~

#### e.插入图片

~~~JAVA
//单元格写入图片
private static  void setCellImage(XWPFTableCell cell,String urls)  {
        if(StringUtils.isBlank(urls))
            return;
        String [] urlArray = urls.split(",");
        String ctxPath=ResourceUtil.getConfigByName("webUploadpath");
        List<XWPFParagraph> paragraphs = cell.getParagraphs();
        XWPFParagraph newPara = paragraphs.get(0);
        XWPFRun imageCellRunn = newPara.createRun();
        for(String url:urlArray){
            String downLoadPath = ctxPath+File.separator + url;
            File image = new File(downLoadPath);
            if(!image.exists()){
                continue;
            }

            int format;
            if (url.endsWith(".emf")) {
                format = XWPFDocument.PICTURE_TYPE_EMF;
            } else if (url.endsWith(".wmf")) {
                format = XWPFDocument.PICTURE_TYPE_WMF;
            } else if (url.endsWith(".pict")) {
                format = XWPFDocument.PICTURE_TYPE_PICT;
            } else if (url.endsWith(".jpeg") || url.endsWith(".jpg")) {
                format = XWPFDocument.PICTURE_TYPE_JPEG;
            } else if (url.endsWith(".png")) {
                format = XWPFDocument.PICTURE_TYPE_PNG;
            } else if (url.endsWith(".dib")) {
                format = XWPFDocument.PICTURE_TYPE_DIB;
            } else if (url.endsWith(".gif")) {
                format = XWPFDocument.PICTURE_TYPE_GIF;
            } else if (url.endsWith(".tiff")) {
                format = XWPFDocument.PICTURE_TYPE_TIFF;
            } else if (url.endsWith(".eps")) {
                format = XWPFDocument.PICTURE_TYPE_EPS;
            } else if (url.endsWith(".bmp")) {
                format = XWPFDocument.PICTURE_TYPE_BMP;
            } else if (url.endsWith(".wpg")) {
                format = XWPFDocument.PICTURE_TYPE_WPG;
            } else {
                logger.error("Unsupported picture: " + url +
                        ". Expected emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg");
                continue;
            }

            try (FileInputStream is = new FileInputStream(downLoadPath)) {
                imageCellRunn.addPicture(is, format, image.getName(), Units.toEMU(100), Units.toEMU(100)); // 200x200 pixels
            }catch (Exception e){
                logger.error(e.getMessage());
                e.printStackTrace();
            }
//            imageCellRunn.addBreak();

        }


    }

~~~

注意：可以插入多张，但是插入多张之后word读取时有无法读取的错误提示，但是还是能看到导出的文件，需要解决
