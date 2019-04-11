package com.projtec.slingcontrol.web.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.click.Context;
import org.apache.click.control.Column;
import org.apache.click.element.Element;
import org.apache.click.util.ClickUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PDFTableExporter extends AbstractTableExporter 
{

	public PDFTableExporter(String label, String imageSrc)
	{
		 super(label, imageSrc);
	}

	@Override
	protected void export(ExportTable table, Context context,
			OutputStream output) throws IOException
	{
		try
		{

			Document document = new Document();
			PdfWriter.getInstance(document, output);
			document.open();
			
			
			final List tableColumns = table.getExportColumnList();
			PdfPTable tablePDF = new PdfPTable(tableColumns.size());

			
            for (int i = 0, size = tableColumns.size(); i < size; i++) {
                
                Column column = (Column) tableColumns.get(i);
                String title = column.getHeaderTitle();
             
                //celula.set
         
                tablePDF.addCell(title);
            }
            
            
            //celulas
            int start = table.getFirstExportRow();
            int end = table.getLastExportRow();
            if (end == ExportTable.ALL_ROWS) {
                end = table.getRowList().size();
            }

          

            for (int rowIndex = start; rowIndex < end; rowIndex++) {
                           
                Object row = table.getRowList().get(rowIndex);

                for (int columnIndex = 0, size = tableColumns.size(); columnIndex <
                    size; columnIndex++) {
                   
                    Column column = (Column) tableColumns.get(columnIndex);
                    

                    Object columnValue = getColumnValue(row, column, context);

                   
                    tablePDF.addCell(columnValue.toString());
                }
            }
          


			document.add(tablePDF);

			document.close();

		} catch (DocumentException e)
		{
			throw new IOException(e);
		}

	}
	
	
    protected Object getColumnValue(Object row, Column column, Context context) {
        Object value = null;
        if (column.getDecorator() != null) {
            value = column.getDecorator().render(row, context);
        } else {
            value = column.getProperty(row);
        }
        return value;
    }

	@Override
	protected String getFilename()
	{
		return "report.pdf";
	}

	@Override
	protected String getMimeType()
	{
		return ClickUtils.getMimeType(".pdf");
	}

	@Override
	public void setName(String name)
	{
		getExportLink().setName(name + "-pdf");
		
	}

}
