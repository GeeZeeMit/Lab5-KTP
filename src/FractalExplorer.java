import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO;
public class FractalExplorer
{
    private int m_DisplaySize;
    private FractalGenerator m_Gener;
    private Rectangle2D.Double m_Range;
    private JImageDisplay m_Display;
    private JButton m_ResetBut;
    private JComboBox m_Switch;
    private JButton m_SaveBut;
    private JFrame m_Frm;
    private class actionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            if (actionEvent.getSource() == m_Switch)
            {
                m_Gener = (FractalGenerator) m_Switch.getSelectedItem();
                m_Gener.getInitialRange(m_Range);
                drawFractal();
            }
            else
                if (actionEvent.getSource() == m_ResetBut)
            {
                m_Gener.getInitialRange(m_Range);
                drawFractal();
            }
            else
                if (actionEvent.getSource() == m_SaveBut)
            {
                JFileChooser chooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        ImageIO.write(m_Display.picture,
                                "png",
                                chooser.getSelectedFile());
                    }
                    catch (IOException e)
                    {
                        JOptionPane.showMessageDialog(m_Frm, e.getMessage(),
                                "Cannot Save Image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    private class MouseListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX();
            int y = e.getY();

            double xCoord = m_Gener.getCoord(m_Range.x,
                    m_Range.x + m_Range.width, m_DisplaySize,x);
            double yCoord = m_Gener.getCoord(m_Range.y,
                    m_Range.y + m_Range.height, m_DisplaySize,y);
            m_Gener.recenterAndZoomRange(m_Range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
    public FractalExplorer(int ScreenSize)
    {
        m_DisplaySize = ScreenSize;
        m_Range = new Rectangle2D.Double();
        m_Gener = new Mandelbrot();
        m_Gener.getInitialRange(m_Range);
    }
    public void createAndShowGUI()
    {
        JPanel panel = new JPanel();
        m_Switch = new JComboBox();
        m_Switch.addItem(new Mandelbrot());
        m_Switch.addItem(new Tricorn());
        m_Switch.addItem(new BurningShip());
        m_Switch.addActionListener(new actionListener());
        JLabel label = new JLabel("Fractal type:");
        panel.add(label);
        panel.add(m_Switch);

        m_Display = new JImageDisplay(m_DisplaySize, m_DisplaySize);
        m_Display.addMouseListener(new MouseListener());

        m_ResetBut = new JButton("Reset Image");
        m_ResetBut.addActionListener(new actionListener());
        m_SaveBut = new JButton("Save Image");
        m_SaveBut.addActionListener(new actionListener());
        JPanel panel2 = new JPanel();
        panel2.add(m_ResetBut);
        panel2.add(m_SaveBut);

        m_Frm = new JFrame();
        m_Frm.getContentPane().add(panel, BorderLayout.NORTH);
        m_Frm.getContentPane().add(m_Display, BorderLayout.CENTER);
        m_Frm.getContentPane().add(panel2, BorderLayout.SOUTH);
        m_Frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        m_Frm.pack();
        m_Frm.setVisible(true);
        m_Frm.setResizable(true);
    }
    private void drawFractal()
    {
        for (int x = 0; x < m_DisplaySize; x++)
        {
            for (int y = 0; y < m_DisplaySize; y++)
            {
                double xCoord = FractalGenerator.getCoord
                        (m_Range.x, m_Range.x + m_Range.width, m_DisplaySize, x);
                double yCoord = FractalGenerator.getCoord
                        (m_Range.y, m_Range.y + m_Range.height, m_DisplaySize, y);
                int IterNum = m_Gener.numIterations(xCoord, yCoord);
                if (IterNum == -1) m_Display.drawPixel(x, y, 0);
                else
                    {
                    float hue = 0.7f + (float) IterNum / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    m_Display.drawPixel(x, y, rgbColor);
                }
            }
        }
        m_Display.repaint();
    }
    public static void main(String args[])
    {
        FractalExplorer explorer = new FractalExplorer(800);
        explorer.createAndShowGUI();
        explorer.drawFractal();
    }

}
