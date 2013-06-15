
package view;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import javax.faces.model.DataModel;

/**
 * Code adopted from Core Java Server Faces 3rd Edition, by David Geary and Cay Horstman
 * Gives the ability of order a <code>DataModel<code/> used in the JSF <code>DataTable<code/>.
 */
public final class SortFilterModel<E> extends DataModel<E> implements Serializable {

    private DataModel<E> model;
    private Integer[] rows;

    public SortFilterModel() { // mandated by JSF spec
        setWrappedData(null);
    }

    public SortFilterModel(E[] names) { // recommended by JSF spec
        setWrappedData(names);
    }

    public SortFilterModel(DataModel<E> model) {
        this.model = model;
        initializeRows();
    }
    /**
     * Change the data model
     * @param model 
     */
    public void changeModel(DataModel<E> model) {
        this.model = model;
        initializeRows();
    }

    private E getData(int row) {
        int originalIndex = model.getRowIndex();
        model.setRowIndex(row);
        E thisRowData = model.getRowData();
        model.setRowIndex(originalIndex);
        return thisRowData;
    }
/**
     * 
     * @param dataComp Object implementing the <code>Comparator<code/> interface 
     */
    public void sortBy(final Comparator<E> dataComp) {
        Comparator<Integer> rowComp = new Comparator<Integer>() {

            public int compare(Integer r1, Integer r2) {
                E e1 = getData(r1);
                E e2 = getData(r2);
                return dataComp.compare(e1, e2);
            }
        };
        Arrays.sort(rows, rowComp);
    }

    public void setRowIndex(int rowIndex) {
        if (0 <= rowIndex && rowIndex < rows.length) {
            model.setRowIndex(rows[rowIndex]);
        } else {
            model.setRowIndex(rowIndex);
        }
    }

    // The following methods delegate to the decorated model
    public boolean isRowAvailable() {
        return model.isRowAvailable();
    }

    public int getRowCount() {
        return model.getRowCount();
    }

    public E getRowData() {
        return model.getRowData();
    }

    public int getRowIndex() {
        return model.getRowIndex();
    }

    public Object getWrappedData() {
        return model.getWrappedData();
    }

    public void setWrappedData(Object data) {
        model.setWrappedData(data);
        initializeRows();
    }

    private void initializeRows() {
        int rowCnt = model.getRowCount();
        if (rowCnt != -1) {
            rows = new Integer[rowCnt];
            for (int i = 0; i < rowCnt; ++i) {
                rows[i] = i;
            }
        }
    }
}
