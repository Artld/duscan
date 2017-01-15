package logic;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class DuOutputStream extends OutputStream
{
	private JTextArea tArea;
	public DuOutputStream (JTextArea tArea)
	{
		this.tArea = tArea;
	}
	@Override
	public void write(byte[] bytes, int offset, int length) throws IOException
	{
		tArea.append (new String(bytes,offset,length));
	}
	@Override
	public void write(int b) throws IOException {}
}
