// YEncoder
// Andrew Pennebaker
// July 2, 2004 - November 30, 2004

import java.io.*;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class YEncoder extends JPanel implements ActionListener {
	JTextArea contentArea;

	static JButton encodeButton;

	JButton decodeButton;

	public YEncoder() {
		super(new BorderLayout());

		contentArea=new JTextArea(25, 40);
		contentArea.setMargin(new Insets(5, 5, 5, 5));
		contentArea.setEditable(false);
		JScrollPane contentScrollPane=new JScrollPane(contentArea);

		encodeButton=new JButton("Encode");
		encodeButton.setMnemonic('e'); // ALT+e
		encodeButton.addActionListener(this);

		decodeButton=new JButton("Decode");
		decodeButton.setMnemonic('d'); // ALT+d
		decodeButton.addActionListener(this);

		JPanel buttonPanel=new JPanel();
		buttonPanel.add(encodeButton);
		buttonPanel.add(decodeButton);

		add(contentScrollPane, BorderLayout.PAGE_START);
		add(buttonPanel, BorderLayout.PAGE_END);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==encodeButton) {
			yEncode();
		}
		else if (ae.getSource()==decodeButton) {
			yDecode();
		}
	}

	public void yEncode() {
		// open binary file
		JFileChooser fc=new JFileChooser(".");
		fc.setDialogTitle("Open Binary File");
		int valOpen=fc.showOpenDialog(YEncoder.this);
		if (valOpen!=JFileChooser.APPROVE_OPTION)
			return;

		File binaryFile=fc.getSelectedFile();

		DataInputStream fis=null;
		try {
			fis=new DataInputStream(new FileInputStream(binaryFile));
		}
		catch(IOException e) {}

		byte[] raw=AIO.getAllData(fis);

		BASE64Encoder encoder=new BASE64Encoder();
		String u=encoder.encode(raw);

		contentArea.setText(u);

		// save encoded file
		fc=new JFileChooser(".");
		fc.setDialogTitle("Save Encoded File");
		fc.setFileFilter(new DataFilter());
		int valSave=fc.showSaveDialog(YEncoder.this);
		if (valSave!=JFileChooser.APPROVE_OPTION)
			return;

		File encodedFile=fc.getSelectedFile();

		DataOutputStream out=null;
		try {
			if (getExtension(encodedFile).equals(".dat"))
				out=new DataOutputStream(
					new FileOutputStream(encodedFile)
				);
			else
				out=new DataOutputStream(
					new FileOutputStream(encodedFile.getPath()+".dat")
				);
		}
		catch(IOException e) {}

		try {
			byte[] buf=new byte[u.length()];
			for (int i=0; i<u.length(); i++)
				buf[i]=(byte) u.charAt(i);

			out.write(buf);
			out.close();
		}
		catch(IOException e) {}
	}

	public void yDecode() {
		// open encoded file
		JFileChooser fc=new JFileChooser(".");
		fc.setDialogTitle("Open Encoded File");
		fc.setFileFilter(new DataFilter());
		int valOpen=fc.showOpenDialog(YEncoder.this);
		if (valOpen!=JFileChooser.APPROVE_OPTION)
			return;

		File encodedFile=fc.getSelectedFile();

		DataInputStream in=null;
		try {
			in=new DataInputStream(
				new FileInputStream(encodedFile)
			);
		}
		catch(IOException e) {}

		// get string
		String u=new String(AIO.getAllData(in));

		contentArea.setText(u);

		byte[] raw=getDecoded(u);

		// save binary file
		fc=new JFileChooser(".");
		fc.setDialogTitle("Save Binary File");
		int valSave=fc.showSaveDialog(YEncoder.this);
		if (valSave!=JFileChooser.APPROVE_OPTION)
			return;

		File binaryFile=fc.getSelectedFile();

		DataOutputStream out=null;

		try {
			out=new DataOutputStream(new FileOutputStream(binaryFile));
			out.write(raw);
			out.close();
		}
		catch(IOException e) {}
	}

	public static String getExtension(File f) {
		String s=f.getName();
		int i=s.lastIndexOf('.');
		if (i>0 && i<s.length()-1)
			return s.substring(i+1).toLowerCase();
		return "";
	}

	public static byte[] getDecoded(String u) {
		BASE64Decoder decoder=new BASE64Decoder();

		try {
			return decoder.decodeBuffer(u);
		}
		catch(IOException e) {
			byte[] temp={(byte) 0};
			return temp;
		}
	}

	public static void main(String args[]) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		JFrame frame=new JFrame("YEncoder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		JComponent newContentPane=new YEncoder();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		frame.getRootPane().setDefaultButton(encodeButton);

		frame.pack();
		Windows.centerOnScreen(frame);
		frame.show();
	}
}