package com.rmj.formatter.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.rmj.formatter.utils.FormatterUtils;

/**
 * @author lperumalm
 * @version 1.0
 * {@link} 
 */
public class DataFormatterUI {

	private JFrame frmDataFormatterPypl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DataFormatterUI window = new DataFormatterUI();
					window.frmDataFormatterPypl.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DataFormatterUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDataFormatterPypl = new JFrame();
		frmDataFormatterPypl.setResizable(false);
		frmDataFormatterPypl.setTitle("Data Formatter");
		frmDataFormatterPypl.setBounds(100, 100, 707, 653);
		frmDataFormatterPypl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDataFormatterPypl.getContentPane().setLayout(null);

		final JTextArea plainText = new JTextArea();
		plainText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ("paste plain json text".equals(plainText.getText())) {
					plainText.setText("");
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if ("".equals(plainText.getText()))
					setDefaultPlainText(plainText);
			}
		});
		plainText.setLineWrap(true);
		setDefaultPlainText(plainText);
		plainText.setToolTipText("paste plain json text");

		JScrollPane scrollPlainText = new JScrollPane(plainText);
		scrollPlainText.setBounds(56, 27, 597, 214);
		frmDataFormatterPypl.getContentPane().add(scrollPlainText);

		final JTextArea formattedData = new JTextArea();
		formattedData.setEditable(false);
		formattedData.setLineWrap(true);
		setDefaultFormattedText(formattedData);
		formattedData.setToolTipText("formatted json");

		JScrollPane scroll = new JScrollPane(formattedData);
		scroll.setBounds(56, 297, 606, 262);

		frmDataFormatterPypl.getContentPane().add(scroll);

		JButton formatButton = new JButton("BEAUTIFY");
		formatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputData = plainText.getText();
				if (inputData != null && inputData.length() > 0) {
					formattedData.setText(FormatterUtils.fomatJSON(inputData));
					formattedData.setCaretPosition(0);
				}

			}
		});
		formatButton.setToolTipText("Format Input Data");
		formatButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		formatButton.setIcon(null);
		formatButton.setSelectedIcon(null);
		formatButton.setBackground(Color.MAGENTA);
		formatButton.setBounds(529, 250, 108, 35);
		frmDataFormatterPypl.getContentPane().add(formatButton);

		JButton clearText = new JButton("CLEAR");
		clearText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDefaultFormattedText(formattedData);
				setDefaultPlainText(plainText);
			}
		});
		clearText.setToolTipText("Clear Text");
		clearText.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		clearText.setBackground(Color.MAGENTA);
		clearText.setBounds(56, 571, 108, 35);
		frmDataFormatterPypl.getContentPane().add(clearText);

		JButton savebutton = new JButton("Save");
		savebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeFormattetOutputToFile(formattedData);
			}
		});
		savebutton.setToolTipText("Save To Computer");
		savebutton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		savebutton.setBackground(Color.MAGENTA);
		savebutton.setBounds(554, 571, 108, 35);
		frmDataFormatterPypl.getContentPane().add(savebutton);

		JButton copyToClibboard = new JButton("COPY");
		copyToClibboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringSelection selection = new StringSelection(formattedData.getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
				JOptionPane.showMessageDialog(null, "Formatted JSON Data Copied to Clipboard", "JSON Copied",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		copyToClibboard.setToolTipText("COPY TO Clipboard");
		copyToClibboard.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		copyToClibboard.setBackground(Color.MAGENTA);
		copyToClibboard.setBounds(278, 571, 108, 35);
		frmDataFormatterPypl.getContentPane().add(copyToClibboard);

		final JLabel lblCurrentPDTTime = new JLabel("PDT ");
		lblCurrentPDTTime.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		lblCurrentPDTTime.setBackground(Color.CYAN);
		lblCurrentPDTTime.setToolTipText("Current PDT Time");
		lblCurrentPDTTime.setBounds(66, 257, 395, 26);
		frmDataFormatterPypl.getContentPane().add(lblCurrentPDTTime);

		displayTime(lblCurrentPDTTime);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(34, 267, 647, 358);
		frmDataFormatterPypl.getContentPane().add(tabbedPane);

	}

	protected void writeFormattetOutputToFile(JTextArea formattedData) {
		if (!getDefaultFormattedText().equals(formattedData.getText())) {
			String filename = "formatted-" + System.currentTimeMillis() + ".json";
			try (BufferedWriter fileOut = new BufferedWriter(new FileWriter(filename))) {
				formattedData.write(fileOut);
				JOptionPane.showMessageDialog(null, "File Saved Successfully, Location: "
						+ System.getProperty("user.dir") + " File name " + filename, "File Saved",
						JOptionPane.INFORMATION_MESSAGE);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Problem with write to file ", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "no formatted Data", "No Data", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	protected String getDefaultPlainText() {
		return "paste plain json text";
	}

	protected String getDefaultFormattedText() {
		return "formatted json";
	}

	protected void setDefaultPlainText(JTextArea plainText) {
		plainText.setText("paste plain json text");
	}

	protected void setDefaultFormattedText(JTextArea formattedData) {
		formattedData.setText("formatted json");
	}

	private void displayTime(final JLabel lblCurrentPDTTime) {
		// Display PDT Time in UI Wed, 06 Jan 2016 12:47:01
		ActionListener timeUpdate = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Date localTime = new Date();
				DateFormat converter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
				// getting GMT timezone, you can get any timezone e.g. UTC
				converter.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
				lblCurrentPDTTime.setText("Current PDT Time - " + converter.format(localTime).toString());
			}
		};

		new javax.swing.Timer(1000, timeUpdate).start();
	}
}
