package com.example.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class configFileExport {
    private List lstData;
    private String sheetName;
    private String title;
    private String subTitle;
    private int startRow;
    private int cellTitleIndex;
    private int mergeTitleEndIndex;
    private boolean createHeader;
    private String headerPrefix;
    private List<ConfigHeaderExport> header;
    private Map<String, String> fieldSplit;
    private String splitChar;
    private List<CellConfigExport> lstCreateCell;
    private String langKey;
    private String firstLeftHeaderTitle;
    private String secondLeftHeaderTitle;
    private String firstRightHeaderTitle;
    private String secondRightHeaderTitle;
    private List<CellConfigExport> lstCellMerge;
    private List<Integer> lstColumnHidden;
    private boolean isAutoSize;
    private Map<Integer, Integer> mapCustomColumnWidth;

    public configFileExport(
            List lstData,
            String sheetName,
            String title,
            String subTitle,
            int startRow,
            int cellTitleIndex,
            int mergeTitleEndIndex,
            boolean createHeader,
            String headerPrefix,
            List<ConfigHeaderExport> header,
            Map<String, String> fieldSplit,
            String splitChar,
            List<CellConfigExport> lstCreateCell,
            String langKey,
            String firstLeftHeaderTitle,
            String secondLeftHeaderTitle,
            String firstRightHeaderTitle,
            String secondRightHeaderTitle,
            List<CellConfigExport> lstCellMerge,
            List<Integer> lstColumnHidden,
            boolean isAutoSize,
            Map<Integer, Integer> mapCustomColumnWidth
    ) {
        this.lstData = lstData;
        this.sheetName = sheetName;
        this.title = title;
        this.subTitle = subTitle;
        this.startRow = startRow;
        this.cellTitleIndex = cellTitleIndex;
        this.mergeTitleEndIndex = mergeTitleEndIndex;
        this.createHeader = createHeader;
        this.headerPrefix = headerPrefix;
        this.header = header;
        this.fieldSplit = fieldSplit;
        this.splitChar = splitChar;
        this.lstCreateCell = lstCreateCell;
        this.langKey = langKey;
        this.firstLeftHeaderTitle = firstLeftHeaderTitle;
        this.secondLeftHeaderTitle = secondLeftHeaderTitle;
        this.firstRightHeaderTitle = firstRightHeaderTitle;
        this.secondRightHeaderTitle = secondRightHeaderTitle;
        this.lstCellMerge = lstCellMerge;
        this.lstColumnHidden = lstColumnHidden;
        this.isAutoSize = isAutoSize;
        this.mapCustomColumnWidth = mapCustomColumnWidth;
    }
}
