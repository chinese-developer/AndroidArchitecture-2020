package com.example.architecture.home.ui.lyrics.parser;

import java.util.List;

/**
 * 歌词解析器
 *
 */
public interface ILrcParser {

    List<LrcRow> getLrcRows(String str);
}
