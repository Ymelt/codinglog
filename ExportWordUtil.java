
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Auther: chi
 * @Date: 2023-02-15 14:59
 * @Version: V1.0
 */
@Slf4j
public class ExportWordUtil {


    private ExportWordUtil() {
    }

    /**
     * 替换文档中段落文本
     *
     * @param document docx解析对象
     * @param textMap  需要替换的信息集合
     */
    public static void changeParagraphText(XWPFDocument document, Map<String, String> textMap) {
        //获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            //判断此段落时候需要进行替换
            String text = paragraph.getText();
            if (checkText(text)) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    //替换模板原来位置
                    run.setText(changeValue(run.toString(), textMap), 0);
                }
            }
        }
    }

    /**
     * 复制表头 插入行数据，这里样式和表头一样
     *
     * @param document    docx解析对象
     * @param tableList   需要插入数据集合
     * @param headerIndex 表头的行索引，从0开始
     */
    public static void copyHeaderInsertText(XWPFDocument document, List<String[]> tableList, int headerIndex) {
        if (null == tableList) {
            return;
        }
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (XWPFTable table : tables) {
            XWPFTableRow copyRow = table.getRow(headerIndex);
            List<XWPFTableCell> cellList = copyRow.getTableCells();
            if (null == cellList) {
                break;
            }
            //遍历要添加的数据的list
            for (int i = 0; i < tableList.size(); i++) {
                //插入一行
                XWPFTableRow targetRow = table.insertNewTableRow(headerIndex + 1 + i);
                //复制行属性
                targetRow.getCtRow().setTrPr(copyRow.getCtRow().getTrPr());

                String[] strings = tableList.get(i);
                for (int j = 0; j < strings.length; j++) {
                    XWPFTableCell sourceCell = cellList.get(j);
                    //插入一个单元格
                    XWPFTableCell targetCell = targetRow.addNewTableCell();

                    //尝试都使用单元格居中
                    CTTc ctTc = targetCell.getCTTc();
                    CTP ctP = (ctTc.sizeOfPArray() == 0) ? ctTc.addNewP() : ctTc.getPArray(0);
                    XWPFParagraph par = targetCell.getParagraph(ctP);
                    par.setAlignment(ParagraphAlignment.CENTER);


                    //复制列属性
                    targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
                    targetCell.setText(strings[j]);
                }
            }
        }
    }

    /**
     * 复制表头 插入行数据，这里样式和表头一样
     *
     * @param document    docx解析对象
     * @param tableList   需要插入数据集合
     * @param headerIndex 表头的行索引，从0开始
     */
    public static void doRowInsertText(XWPFDocument document, List<String[]> tableList, int headerIndex, int emptySize) {
        if (null == tableList) {
            return;
        }
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (XWPFTable table : tables) {

            if (tableList.size() > emptySize) {

                XWPFTableRow templateRow = table.getRow(headerIndex);
                //在已有的单元格中赋值
                for (int i = 0; i < emptySize;i++){
                    XWPFTableRow copyRow = table.getRow(headerIndex+i);
                    List<XWPFTableCell> cellList = copyRow.getTableCells();
                    for (int j = 1; j < cellList.size(); j++) {
                        cellList.get(j).setText(tableList.get(i)[j]);
                    }
                }

                //数据超出已有行数，新增行
                for (int i = 0; i < tableList.size() - emptySize; i++) {
                    //插入一行
                    XWPFTableRow targetRow = table.insertNewTableRow(headerIndex + emptySize + i);
                    //复制行属性
                    targetRow.getCtRow().setTrPr(templateRow.getCtRow().getTrPr());
                    String[] strings = tableList.get(i+emptySize);
                    //第一个单元格是合并的
                    //插入一个单元格
                    XWPFTableCell firstOne = targetRow.addNewTableCell();
                    firstOne.setText("**");
                    for (int j = 1; j < strings.length; j++) {
                        XWPFTableCell sourceCell = templateRow.getTableCells().get(j);
                        //插入一个单元格
                        XWPFTableCell targetCell = targetRow.addNewTableCell();

                        //尝试都使用单元格居中
                        CTTc ctTc = targetCell.getCTTc();
                        CTP ctP = (ctTc.sizeOfPArray() == 0) ? ctTc.addNewP() : ctTc.getPArray(0);
                        XWPFParagraph par = targetCell.getParagraph(ctP);
                        par.setAlignment(ParagraphAlignment.CENTER);


                        //复制列属性
                        targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
                        targetCell.setText(strings[j]);
                    }
                }


            } else {
                //遍历要添加的数据的list
                for (int i = 0; i < tableList.size(); i++) {
                    XWPFTableRow copyRow = table.getRow(headerIndex + i);
                    List<XWPFTableCell> cellList = copyRow.getTableCells();
                    for (int j = 1; j < cellList.size(); j++) {
                        cellList.get(j).setText(tableList.get(i)[j]);
                    }

                }
            }


        }

    }

    /**
     * 替换表格对象方法
     *
     * @param document docx解析对象
     * @param textMap  需要替换的信息集合
     */
    public static void changeTableText(XWPFDocument document, Map<String, String> textMap) {
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            //只处理行数大于等于2的表格
            XWPFTable table = tables.get(i);
            if (table.getRows().size() > 1) {
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if (checkText(table.getText())) {
                    List<XWPFTableRow> rows = table.getRows();
                    //遍历表格,并替换模板
                    eachTable(rows, textMap);
                }
            }
        }
    }

    /**
     * 遍历表格,并替换模板
     *
     * @param rows    表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(List<XWPFTableRow> rows, Map<String, String> textMap) {
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if (checkText(cell.getText())) {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            run.setText(changeValue(run.toString(), textMap), 0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 匹配传入信息集合与模板
     *
     * @param value   模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String changeValue(String value, Map<String, String> textMap) {
        Set<Map.Entry<String, String>> textSets = textMap.entrySet();
        for (Map.Entry<String, String> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            String key = "${" + textSet.getKey() + "}";
            if (value.indexOf(key) != -1) {
                value = textSet.getValue();
            }
        }
        //模板未匹配到区域替换为空
        if (checkText(value)) {
            value = "";
        }
        return value;
    }

    /**
     * 判断文本中时候包含$
     *
     * @param text 文本
     * @return 包含返回true, 不包含返回false
     */
    public static boolean checkText(String text) {
        boolean check = false;
        if (text.indexOf("$") != -1) {
            check = true;
        }
        return check;
    }


    /**
     * @Description: 跨行合并
     * table要合并单元格的表格
     * col要合并哪一列的单元格
     * fromRow从哪一行开始合并单元格
     * toRow合并到哪一个行
     */
    public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
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



    public static XWPFTableCell getImagesPositionInTable(XWPFTable table, int row, int cellIndex){
        try {
            XWPFTableRow templateRow = table.getRow(row);
            List<XWPFTableCell> cellList = templateRow.getTableCells();
            return cellList.get(cellIndex);
        } catch (Exception e) {
            throw new RuntimeException("图片单元格获取失败");
        }
    }

}