package edu.sharif.ce.mir.console.io.impl;

import edu.sharif.ce.mir.console.io.Output;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 4:31)
 */
public class TableOutput implements Output {

    private List<List<Output>> outputs = new ArrayList<List<Output>>();
    private char lineChar = '-';
    private int cellPadding = 0;

    public void addRow(Output ... row) {
        final ArrayList<Output> list = new ArrayList<Output>(row.length);
        for (Output cell : row) {
            list.add(cell);
        }
        outputs.add(list);
    }

    public void addLine() {
        outputs.add(null);
    }

    public void setLineChar(char lineChar) {
        this.lineChar = lineChar;
    }

    public void setCellPadding(int cellPadding) {
        this.cellPadding = cellPadding;
    }

    @Override
    public String getValue() {
        int cols = 0;
        for (List<Output> output : outputs) {
            if (output == null) {
                continue;
            }
            if (cols < output.size()) {
                cols = output.size();
            }
        }
        Integer[] lengths = new Integer[cols];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = 0;
        }
        int length = cellPadding * cols;
        for (List<Output> row : outputs) {
            if (row == null) {
                continue;
            }
            for (int i = 0; i < row.size(); i++) {
                Output cell = row.get(i);
                if (lengths[i] < cell.getValue().length()) {
                    length -= lengths[i];
                    lengths[i] = cell.getValue().length();
                    length += lengths[i];
                }
            }
        }
        final StringBuilder builder = new StringBuilder();
        for (List<Output> row : outputs) {
            if (row == null) {
                for (int i = 0; i < length; i ++) {
                    builder.append(lineChar);
                }
                builder.append("\n");
                continue;
            }
            for (int i = 0; i < row.size(); i++) {
                Output cell = row.get(i);
                int current = cell.getValue().length();
                builder.append(cell.getValue());
                while (current < lengths[i]) {
                    builder.append(" ");
                    current ++;
                }
                current = 0;
                while (current < cellPadding) {
                    builder.append(" ");
                    current ++;
                }
            }
            builder.append("\n");
        }
        String result = builder.toString();
        if (result.endsWith("\n")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
